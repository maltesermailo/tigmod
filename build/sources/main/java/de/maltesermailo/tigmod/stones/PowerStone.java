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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PowerStone extends Item implements BasicStone {

	public PowerStone() {
		setUnlocalizedName("power_stone");
		setRegistryName("power_stone");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("The Power Stone is the manifestation of all energy in the universe. You can electrocute players with it.");
		tooltip.add("When in main hand:");
		tooltip.add("  +20 Magic Damage");
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("currentSelectedPower", EnumPowers.PowerStone.ELECTROCUTE.ordinal());
		stack.setTagCompound(tag);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(isSelected) {
			if(stack.getTagCompound() == null) {
				this.onCreated(stack, worldIn, null);
			}
			if(entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;
				player.sendStatusMessage(new TextComponentString("§aSelected: " + this.getCaption(stack)), true);
				
				if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
					if(((ProxyClient) TIGMod.proxy).keyBindingChangeMode.isPressed()) {
						SimpleNetworkWrapper network = TIGMod.proxy.INSTANCE;
						network.sendToServer(new ChangeModeMessage());
					}
				}
			}
		}
	}
	
	public String getCaption(ItemStack item) {
		return EnumPowers.PowerStone.values()[item.getTagCompound().getInteger("currentSelectedPower")].getCaption();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack item = playerIn.getHeldItem(handIn);
		
		EnumPowers.PowerStone currentSelected = EnumPowers.PowerStone.values()[item.getTagCompound().getInteger("currentSelectedPower")];
		
		if(currentSelected == EnumPowers.PowerStone.ELECTROCUTE) {
			electrocute(worldIn, playerIn);
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		EnumPowers.PowerStone currentSelected = EnumPowers.PowerStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")];
		
		if(currentSelected == EnumPowers.PowerStone.ELECTROCUTE) {
			if(entity instanceof EntityLivingBase) {
				entity.attackEntityFrom(DamageSource.MAGIC, 20F);
				((EntityLivingBase) entity).knockBack(entity, 0.5F, player.posX - entity.posX, player.posZ - entity.posZ);
			}
		}
		return super.onLeftClickEntity(stack, player, entity);
	}

	public void electrocute(World world, EntityPlayer player) {
		RayTraceResult result = player.rayTrace(100, Minecraft.getMinecraft().getRenderPartialTicks());
		if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = result.getBlockPos();
			SimpleNetworkWrapper network = TIGMod.proxy.INSTANCE;
			network.sendToServer(new TeleportMessage(pos.getX(), pos.getY(), pos.getZ()));
		}
	}

	@Override
	public int changeMode(ItemStack item, EntityPlayer player, boolean powerStone, boolean gauntlet) {
		int currentSelected = item.getTagCompound().getInteger("currentSelectedPower");
		
		EnumPowers.PowerStone nextPower = null;
		
		if(EnumPowers.PowerStone.values().length < (currentSelected+2)) {
			nextPower = EnumPowers.PowerStone.ELECTROCUTE;
		} else {
			nextPower = EnumPowers.PowerStone.values()[currentSelected+1];
			
			if(nextPower.isGauntledNeeded() && !gauntlet) {
				item.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
				return changeMode(item, player, powerStone, gauntlet);
			}
		}
		
		item.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
		
		return nextPower.ordinal();
	}

	@Override
	public EnumStones getType() {
		return EnumStones.POWER_STONE;
	}

	@Override
	public void onClickGauntlet(InfinityGauntlet infinityGauntlet, ItemStack stack, EntityPlayer player) {
		EnumPowers.PowerStone currentSelected = EnumPowers.PowerStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")];
		
		if(currentSelected == EnumPowers.PowerStone.SHIELD) {
			if(player.isPotionActive(Potion.getPotionById(22))) {
				player.removePotionEffect(Potion.getPotionById(22));
			} else {
				player.addPotionEffect(new PotionEffect(Potion.getPotionById(22), Integer.MAX_VALUE, 3));
			}
		}
	}

}
