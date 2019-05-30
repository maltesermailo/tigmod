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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpaceStone extends Item implements BasicStone {
	
	public SpaceStone() {
		setUnlocalizedName("space_stone");
		setRegistryName("space_stone");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("The Space Stone is the universe's equivalent of space. You can travel far distances with it");
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("currentSelectedPower", EnumPowers.SpaceStone.TELEPORT.ordinal());
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
		return EnumPowers.SpaceStone.values()[item.getTagCompound().getInteger("currentSelectedPower")].getCaption();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack item = playerIn.getHeldItem(handIn);
		
		EnumPowers.SpaceStone currentSelected = EnumPowers.SpaceStone.values()[item.getTagCompound().getInteger("currentSelectedPower")];
		
		if(currentSelected == EnumPowers.SpaceStone.TELEPORT) {
			powerTeleport(worldIn, playerIn);
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@SideOnly(Side.CLIENT)
	public void powerTeleport(World world, EntityPlayer player) {
		RayTraceResult result = player.rayTrace(100, Minecraft.getMinecraft().getRenderPartialTicks());
		if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = result.getBlockPos();
			SimpleNetworkWrapper network = TIGMod.proxy.INSTANCE;
			network.sendToServer(new TeleportMessage(pos.getX(), pos.getY(), pos.getZ()));
		}
	}
	
	public void powerTeleportServer(World world, EntityPlayer player, BlockPos pos) {
		if(player.getDistanceSq(pos) < 10001) {
			player.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
			player.playSound(SoundEvents.ENTITY_PLAYER_SPLASH, 1.0F, 0F);
		} else {
			player.sendMessage(new TextComponentString("�cToo far away!"));
		}
	}

	@Override
	public int changeMode(ItemStack item, EntityPlayer player, boolean powerStone, boolean gauntlet) {
		int currentSelected = item.getTagCompound().getInteger("currentSelectedPower");
		
		EnumPowers.SpaceStone nextPower = null;
		
		if(EnumPowers.SpaceStone.values().length < (currentSelected+2)) {
			nextPower = EnumPowers.SpaceStone.TELEPORT;
		} else {
			nextPower = EnumPowers.SpaceStone.values()[currentSelected+1];
			
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
		
		item.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
		
		return nextPower.ordinal();
	}

	@Override
	public EnumStones getType() {
		return EnumStones.SPACE_STONE;
	}

	@Override
	public void onClickGauntlet(InfinityGauntlet gauntlet, ItemStack stack, EntityPlayer player) {
		EnumPowers.SpaceStone currentSelected = EnumPowers.SpaceStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")];
		
		if(currentSelected == EnumPowers.SpaceStone.TELEPORT) {
			powerTeleport(player.world, player);
		} else if(currentSelected == EnumPowers.SpaceStone.SPEED_BOST) {
			if(player.isPotionActive(Potion.getPotionById(1))) {
				player.removePotionEffect(Potion.getPotionById(1));
			} else {
				player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), Integer.MAX_VALUE, 2));
			}
		} else if(currentSelected == EnumPowers.SpaceStone.JUMP_BOOST) {
			if(player.isPotionActive(Potion.getPotionById(8))) {
				player.removePotionEffect(Potion.getPotionById(8));
			} else {
				player.addPotionEffect(new PotionEffect(Potion.getPotionById(8), Integer.MAX_VALUE, 2));
			}
		}
	}

}