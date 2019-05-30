package de.maltesermailo.tigmod.stones;

import java.util.List;

import de.maltesermailo.tigmod.ChangeModeMessage;
import de.maltesermailo.tigmod.TIGMod;
import de.maltesermailo.tigmod.TeleportMessage;
import de.maltesermailo.tigmod.gauntlet.EnumStones;
import de.maltesermailo.tigmod.gauntlet.InfinityGauntlet;
import de.maltesermailo.tigmod.proxy.ProxyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoulStone extends Item implements BasicStone {
	
	public SoulStone() {
		setUnlocalizedName("soul_stone");
		setRegistryName("soul_stone");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("The Soul Stone is the soul of the universe contained in a stone. You can kill players with it.");
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("currentSelectedPower", EnumPowers.SoulStone.KILL.ordinal());
		stack.setTagCompound(tag);
		stack.setStackDisplayName("§cSoul Stone (Mode: Kill Player)");
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(isSelected) {
			if(stack.getTagCompound() == null) {
				this.onCreated(stack, worldIn, null);
			}
			if(entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;
				
				if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
					if(((ProxyClient) TIGMod.proxy).keyBindingChangeMode.isPressed()) {
						SimpleNetworkWrapper network = TIGMod.proxy.INSTANCE;
						network.sendToServer(new ChangeModeMessage());
					}
				}
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack item = playerIn.getHeldItem(handIn);
		
		EnumPowers.SoulStone currentSelected = EnumPowers.SoulStone.values()[item.getTagCompound().getInteger("currentSelectedPower")];
		
		if(currentSelected == EnumPowers.SoulStone.KILL_ALL) {
			if(!playerIn.inventory.hasItemStack(new ItemStack(TIGMod.INSTANCE.powerStoneItem))) {
				this.changeMode(item, playerIn, false, false);
				return super.onItemRightClick(worldIn, playerIn, handIn);
			}
			for(EntityLivingBase entity : playerIn.world.getEntitiesWithinAABB(EntityLivingBase.class, playerIn.getEntityBoundingBox().grow(10, 10, 10))) {
				if(!entity.isEntityEqual(playerIn)) {
					entity.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
				}
			}
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	public String getCaption(ItemStack item) {
		return EnumPowers.SoulStone.values()[item.getTagCompound().getInteger("currentSelectedPower")].getCaption();
	}

	@Override
	public int changeMode(ItemStack item, EntityPlayer player, boolean powerStone, boolean gauntlet) {
		int currentSelected = item.getTagCompound().getInteger("currentSelectedPower");
		
		EnumPowers.SoulStone nextPower = null;
		
		if(EnumPowers.SoulStone.values().length < (currentSelected+2)) {
			nextPower = EnumPowers.SoulStone.KILL;
		} else {
			nextPower = EnumPowers.SoulStone.values()[currentSelected+1];
			
			if(nextPower.isGauntledNeeded() && !gauntlet) {
				item.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
				return changeMode(item, player, powerStone, gauntlet);
			}
			if(nextPower.isPowerStoneNeeded() && !powerStone) {
				if(!player.inventory.hasItemStack(new ItemStack(TIGMod.INSTANCE.powerStoneItem))) {
					item.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
					return changeMode(item, player, powerStone, gauntlet);
				}
			}
		}
		
		item.setStackDisplayName("§cSoul Stone (Mode: " + nextPower.getCaption() + ")");
		player.sendStatusMessage(new TextComponentString("§aSelected: " + nextPower.getCaption()), true);
		item.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
		
		return nextPower.ordinal();
	}

	@Override
	public EnumStones getType() {
		return EnumStones.SOUL_STONE;
	}

	@Override
	public void onClickGauntlet(InfinityGauntlet infinityGauntlet, ItemStack stack, EntityPlayer player) {
		EnumPowers.SoulStone currentSelected = EnumPowers.SoulStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")];
		
		if(currentSelected == EnumPowers.SoulStone.KILL_ALL) {
			for(EntityLivingBase entity : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(10, 10, 10))) {
				if(!entity.isEntityEqual(player)) {
					entity.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
				}
			}
		} else if(currentSelected == EnumPowers.SoulStone.HEAL) {
			player.heal(4F);
		}
	}

}