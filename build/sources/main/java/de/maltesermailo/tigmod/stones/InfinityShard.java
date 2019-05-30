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
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfinityShard extends Item {
	
	public InfinityShard() {
		setUnlocalizedName("infinity_shard");
		setRegistryName("infinity_shard");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("An infinity shard is a resemblent of an infinity stone. It doesn't have any powers over the universe however.");
	}

}
