package de.maltesermailo.tigmod.gauntlet;

import de.maltesermailo.tigmod.ChangeModeMessage;
import de.maltesermailo.tigmod.TIGMod;
import de.maltesermailo.tigmod.proxy.ProxyClient;
import de.maltesermailo.tigmod.stones.BasicStone;
import de.maltesermailo.tigmod.stones.EnumPowers;
import de.maltesermailo.tigmod.stones.PowerStone;
import de.maltesermailo.tigmod.stones.RealityStone;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class InfinityGauntlet extends Item {
	
	public InfinityGauntlet() {
		setUnlocalizedName("infinity_gauntlet");
		setRegistryName("infinity_gauntlet");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.COMBAT);
		
		addPropertyOverride(new ResourceLocation("thanosinfinitygauntlet", "full"), new IItemPropertyGetter() {
			
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if(stack.getTagCompound().getBoolean("full")) {
					return 1F;
				}
				return 0F;
			}
		});
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("currentSelectedPower", 0);
		tag.setInteger("currentSelectedStone", EnumStones.NONE.ordinal());
		tag.setTag("stones", new NBTTagList());
		tag.setBoolean("full", false);
		for(EnumStones stone : EnumStones.values()) {
			tag.setBoolean(stone.name().toLowerCase(), false);
		}
		stack.setTagCompound(tag);
	}
	
	@Override
	public int getMetadata(ItemStack stack) {
		if(stack.getTagCompound() == null) return 0;
		return this.checkHasAllStones(stack) ? 1 : 0;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(isSelected) {
			if(stack.getTagCompound() == null) {
				this.onCreated(stack, worldIn, null);
			}
			if(entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;
				player.sendStatusMessage(new TextComponentString("§aSelected " + EnumStones.values()[stack.getTagCompound().getInteger("currentSelectedStone")].getCaption() + ": " + this.getCaption(stack)), true);
				
				if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
					if(((ProxyClient) TIGMod.proxy).keyBindingChangeMode.isPressed()) {
						SimpleNetworkWrapper network = TIGMod.proxy.INSTANCE;
						network.sendToServer(new ChangeModeMessage());
					}
				}
				if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
					if(((ProxyClient) TIGMod.proxy).keyBindingChangeStone.isPressed()) {
						SimpleNetworkWrapper network = TIGMod.proxy.INSTANCE;
						network.sendToServer(new ChangeModeMessage(1));
					}
				}
			}
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(getCurrentStone(player.getHeldItemMainhand()) instanceof RealityStone) {
			int currentSelected = player.getHeldItemMainhand().getTagCompound().getInteger("currentSelectedPower");
			
			EnumPowers.RealityStone currentPower = EnumPowers.RealityStone.values()[currentSelected];
			
			if(currentPower == EnumPowers.RealityStone.SET_BLOCK) {
				Item item = player.getHeldItemOffhand().getItem();
				
				if(item instanceof ItemBlock) {
					Block block = ((ItemBlock)item).getBlock();
					
					worldIn.setBlockState(pos, block.getDefaultState());
				}
			}
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@SubscribeEvent
	public void onRightClick(RightClickBlock e) {
		if(!(e.getEntityPlayer().getHeldItem(e.getHand()).getItem() instanceof InfinityGauntlet))
			return;
		
		if(getCurrentStone(e.getEntityPlayer().getHeldItem(e.getHand())) instanceof RealityStone) {
			int currentSelected = e.getEntityPlayer().getHeldItem(e.getHand()).getTagCompound().getInteger("currentSelectedPower");
			
			EnumPowers.RealityStone currentPower = EnumPowers.RealityStone.values()[currentSelected];
			
			if(currentPower == EnumPowers.RealityStone.SET_BLOCK) {
				Item item = e.getEntityPlayer().getHeldItemOffhand().getItem();
				
				if(item instanceof ItemBlock) {
					e.setCanceled(true);
					e.setResult(Result.DENY);
				}
			}
		}
	}
	
	public int changeStone(ItemStack stack, EntityPlayer player) {
		int currentSelected = stack.getTagCompound().getInteger("currentSelectedStone");
		
		if(!checkHasAStoneAtAll(stack)) {
			stack.getTagCompound().setInteger("currentSelectedStone", EnumStones.NONE.ordinal());
			
			return EnumStones.NONE.ordinal();
		}
		
		EnumStones nextStone = null;
		
		if(EnumStones.values().length < (currentSelected+2)) {
			if(checkStone(stack, EnumStones.SPACE_STONE)) {
				nextStone = EnumStones.SPACE_STONE;
			} else {
				nextStone = EnumStones.SPACE_STONE;
				stack.getTagCompound().setInteger("currentSelectedStone", nextStone.ordinal());
				
				return changeStone(stack, player);
			}
		} else {
			nextStone = EnumStones.values()[currentSelected+1];
			
			if(!checkStone(stack, nextStone)) {
				stack.getTagCompound().setInteger("currentSelectedStone", nextStone.ordinal());
				return changeStone(stack, player);
			}
		}
		
		stack.getTagCompound().setInteger("currentSelectedStone", nextStone.ordinal());
		stack.getTagCompound().setInteger("currentSelectedPower", 0);
		
		return nextStone.ordinal();
	}

	private String getCaption(ItemStack stack) {
		switch(EnumStones.values()[stack.getTagCompound().getInteger("currentSelectedStone")]) {
		case SPACE_STONE:
			return EnumPowers.SpaceStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")].getCaption();
		case POWER_STONE:
			return EnumPowers.PowerStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")].getCaption();
		case TIME_STONE:
			return EnumPowers.TimeStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")].getCaption();
		case MIND_STONE:
			return EnumPowers.MindStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")].getCaption();
		case REALITY_STONE:
			return EnumPowers.RealityStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")].getCaption();
		case SOUL_STONE:
			return EnumPowers.SoulStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")].getCaption();
		case SNAP:
			return "The Snap (Kill all Entities in 50 meters range)";
		default:
			return "None";
		}
	}

	public void changeMode(ItemStack stack, EntityPlayer player) {
		switch(EnumStones.values()[stack.getTagCompound().getInteger("currentSelectedStone")]) {
		case SPACE_STONE:
			TIGMod.INSTANCE.spaceStoneItem.changeMode(stack, player, checkStone(stack, EnumStones.POWER_STONE), true);
			break;
		case POWER_STONE:
			TIGMod.INSTANCE.powerStoneItem.changeMode(stack, player, true, true);
			break;
		case TIME_STONE:
			TIGMod.INSTANCE.timeStoneItem.changeMode(stack, player, checkStone(stack, EnumStones.POWER_STONE), true);
			break;
		case MIND_STONE:
			TIGMod.INSTANCE.mindStoneItem.changeMode(stack, player, checkStone(stack, EnumStones.POWER_STONE), true);
			break;
		case REALITY_STONE:
			TIGMod.INSTANCE.realityStoneItem.changeMode(stack, player, checkStone(stack, EnumStones.POWER_STONE), true);
			break;
		case SOUL_STONE:
			this.changeModeSoul(stack, checkStone(stack, EnumStones.POWER_STONE));
			break;
		case SNAP:
			stack.getTagCompound().setInteger("currentSelectedPower", 0);
		default:
			break;
		}
	}
	
	private int changeModeSoul(ItemStack stack, boolean powerStone) {
		int currentSelected = stack.getTagCompound().getInteger("currentSelectedPower");
		
		EnumPowers.SoulStone nextPower = null;
		
		if(EnumPowers.SoulStone.values().length < (currentSelected+2)) {
			nextPower = EnumPowers.SoulStone.KILL;
		} else {
			nextPower = EnumPowers.SoulStone.values()[currentSelected+1];
			
			if(nextPower.isPowerStoneNeeded()) {
				if(!powerStone) {
					stack.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
					return changeModeSoul(stack, powerStone);
				}
			}
		}
		stack.getTagCompound().setInteger("currentSelectedPower", nextPower.ordinal());
		currentSelected = stack.getTagCompound().getInteger("currentSelectedPower");
		
		return nextPower.ordinal();
	}
	
	public void updateModel(ItemStack stack) {
		if(hasAllStones(stack)) {
			stack.getTagCompound().setBoolean("full", true);
		} else {
			stack.getTagCompound().setBoolean("full", false);
		}
		
		for(EnumStones stone : EnumStones.values()) {
			stack.getTagCompound().setBoolean(stone.name().toLowerCase(), hasStone(stack, stone));
		}
	}
	
	public boolean hasStone(ItemStack stack, EnumStones stone) {
		if(stone.equals(EnumStones.SNAP)) return hasAllStones(stack);
		
		InfinityGauntletInventory inv = new InfinityGauntletInventory(stack);
		inv.openInventory(null);
		
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			if(inv.getStackInSlot(i).getItem() instanceof BasicStone) {
				if(((BasicStone) inv.getStackInSlot(i).getItem()).getType().equals(stone)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean checkStone(ItemStack stack, EnumStones stone) {
		return stack.getTagCompound().getBoolean(stone.name().toLowerCase());
	}
	
	public boolean hasAllStones(ItemStack stack) {
		return hasStone(stack, EnumStones.SPACE_STONE) &&
				hasStone(stack, EnumStones.POWER_STONE) &&
				hasStone(stack, EnumStones.REALITY_STONE) &&
				hasStone(stack, EnumStones.TIME_STONE) &&
				hasStone(stack, EnumStones.MIND_STONE) &&
				hasStone(stack, EnumStones.SOUL_STONE);
	}
	
	public boolean hasAStoneAtAll(ItemStack stack) {
		return hasStone(stack, EnumStones.SPACE_STONE) ||
				hasStone(stack, EnumStones.POWER_STONE) ||
				hasStone(stack, EnumStones.REALITY_STONE) ||
				hasStone(stack, EnumStones.TIME_STONE) ||
				hasStone(stack, EnumStones.MIND_STONE) ||
				hasStone(stack, EnumStones.SOUL_STONE);
	}
	
	public boolean checkHasAllStones(ItemStack stack) {
		return checkStone(stack, EnumStones.SPACE_STONE) &&
				checkStone(stack, EnumStones.POWER_STONE) &&
				checkStone(stack, EnumStones.REALITY_STONE) &&
				checkStone(stack, EnumStones.TIME_STONE) &&
				checkStone(stack, EnumStones.MIND_STONE) &&
				checkStone(stack, EnumStones.SOUL_STONE);
	}
	
	public boolean checkHasAStoneAtAll(ItemStack stack) {
		return checkStone(stack, EnumStones.SPACE_STONE) ||
				checkStone(stack, EnumStones.POWER_STONE) ||
				checkStone(stack, EnumStones.REALITY_STONE) ||
				checkStone(stack, EnumStones.TIME_STONE) ||
				checkStone(stack, EnumStones.MIND_STONE) ||
				checkStone(stack, EnumStones.SOUL_STONE);
	}
	
	public BasicStone getCurrentStone(ItemStack stack) {
		switch(EnumStones.values()[stack.getTagCompound().getInteger("currentSelectedStone")]) {
		case SPACE_STONE:
			if(!checkStone(stack, EnumStones.SPACE_STONE)) return null;
			return TIGMod.INSTANCE.spaceStoneItem;
		case POWER_STONE:
			if(!checkStone(stack, EnumStones.POWER_STONE)) return null;
			return TIGMod.INSTANCE.powerStoneItem;
		case TIME_STONE:
			if(!checkStone(stack, EnumStones.TIME_STONE)) return null;
			return TIGMod.INSTANCE.timeStoneItem;
		case MIND_STONE:
			if(!checkStone(stack, EnumStones.MIND_STONE)) return null;
			return TIGMod.INSTANCE.mindStoneItem;
		case REALITY_STONE:
			if(!checkStone(stack, EnumStones.REALITY_STONE)) return null;
			return TIGMod.INSTANCE.realityStoneItem;
		case SOUL_STONE:
			if(!checkStone(stack, EnumStones.SOUL_STONE)) return null;
			return TIGMod.INSTANCE.soulStoneItem;
		case SNAP:
			if(!hasAllStones(stack)) return null;
			return TIGMod.INSTANCE.snap;
		default:
			return null;
		}
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if(stack.getTagCompound().getBoolean(EnumStones.POWER_STONE.name().toLowerCase()) && getCurrentStone(stack) instanceof PowerStone) {
			EnumPowers.PowerStone currentSelected = EnumPowers.PowerStone.values()[stack.getTagCompound().getInteger("currentSelectedPower")];
			
			if(currentSelected == EnumPowers.PowerStone.ELECTROCUTE) {
				if(entity instanceof EntityLivingBase) {
					entity.attackEntityFrom(DamageSource.MAGIC, 20F);
					((EntityLivingBase) entity).knockBack(entity, 0.5F, player.posX - entity.posX, player.posZ - entity.posZ);
				}
			}
		}
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		//h�?
		
		if(playerIn.isSneaking()) {
			playerIn.openGui(TIGMod.INSTANCE, 0, worldIn, 0, 0, 0);
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
		
		BasicStone stone = this.getCurrentStone(playerIn.getHeldItem(handIn));
		if(stone != null) {
			stone.onClickGauntlet(this, playerIn.getHeldItem(handIn), playerIn);
		} else {
			this.changeStone(playerIn.getHeldItem(handIn), playerIn);
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
}
