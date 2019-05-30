package de.maltesermailo.tigmod;

import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;

import de.maltesermailo.tigmod.commands.TIGModCommands;
import de.maltesermailo.tigmod.gauntlet.InfinityGauntlet;
import de.maltesermailo.tigmod.ingredients.ItemCompressedStar;
import de.maltesermailo.tigmod.ingredients.ItemCompressedStar.ItemDualCompressedStar;
import de.maltesermailo.tigmod.proxy.Proxy;
import de.maltesermailo.tigmod.stones.BasicStone;
import de.maltesermailo.tigmod.stones.EnumPowers;
import de.maltesermailo.tigmod.stones.InfinityShard;
import de.maltesermailo.tigmod.stones.MindStone;
import de.maltesermailo.tigmod.stones.PowerStone;
import de.maltesermailo.tigmod.stones.RealityStone;
import de.maltesermailo.tigmod.stones.Snap;
import de.maltesermailo.tigmod.stones.SoulStone;
import de.maltesermailo.tigmod.stones.SpaceStone;
import de.maltesermailo.tigmod.stones.TimeStone;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

@Mod(modid = TIGMod.MODID, name = TIGMod.NAME, version = TIGMod.VERSION)
public class TIGMod
{
    public static final String MODID = "thanosinfinitygauntlet";
    public static final String NAME = "TIG MOD";
    public static final String VERSION = "1.0";

    private static Logger logger;
    
    @SidedProxy(clientSide = "de.maltesermailo.tigmod.proxy.ProxyClient", serverSide = "de.maltesermailo.tigmod.proxy.Proxy")
    public static Proxy proxy;
    
    public static TIGMod INSTANCE;
    
    public SpaceStone spaceStoneItem;
    public PowerStone powerStoneItem;
    public TimeStone timeStoneItem;
    public SoulStone soulStoneItem;
    public MindStone mindStoneItem;
    public RealityStone realityStoneItem;
    public Snap snap = new Snap();
    public InfinityGauntlet infinityGauntletItem;
    public ItemCompressedStar compressedStarItem;
    public ItemCompressedStar.ItemDualCompressedStar dualCompressedStarItem;
    public InfinityShard infinityShardItem;
    
    public Set<String> stonesCrafted;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	INSTANCE = this;
        logger = event.getModLog();
        
        this.stonesCrafted = Sets.newHashSet(ConfigTIGMod.craftedStones);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        proxy.preInit(event);
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
    	logger.info("TIG MOD >> Registered items");
    	this.spaceStoneItem = new SpaceStone();
    	this.powerStoneItem = new PowerStone();
    	this.timeStoneItem = new TimeStone();
    	this.soulStoneItem = new SoulStone();
    	this.mindStoneItem = new MindStone();
    	this.realityStoneItem = new RealityStone();
    	this.infinityGauntletItem = new InfinityGauntlet();
    	this.compressedStarItem = new ItemCompressedStar();
    	this.dualCompressedStarItem = new ItemDualCompressedStar();
    	this.infinityShardItem = new InfinityShard();
    	
        event.getRegistry().registerAll(this.spaceStoneItem, this.powerStoneItem, this.timeStoneItem, this.soulStoneItem, this.mindStoneItem, this.realityStoneItem, this.infinityGauntletItem, this.compressedStarItem, this.dualCompressedStarItem, this.infinityShardItem);
    }
    
    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
    	Ingredient dualCompressedStar = Ingredient.fromItem(this.dualCompressedStarItem);
    	Ingredient eyeOfEnder = Ingredient.fromItem(Item.getByNameOrId("minecraft:ender_eye"));
    	Ingredient diamondSword = Ingredient.fromItem(Item.getByNameOrId("minecraft:diamond_sword"));
    	Ingredient endCrystal = Ingredient.fromItem(Item.getByNameOrId("minecraft:end_crystal"));
    	Ingredient clock = Ingredient.fromItem(Item.getByNameOrId("minecraft:clock"));
    	Ingredient skull = Ingredient.fromItem(Item.getByNameOrId("minecraft:skull"));
    	Ingredient goldenApple = Ingredient.fromStacks(new ItemStack(Item.getByNameOrId("minecraft:golden_apple"), 1, 1));
    	
    	NonNullList recipeSpaceStone = NonNullList.from(Ingredient.EMPTY, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar, eyeOfEnder, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar);
    	NonNullList recipePowerStone = NonNullList.from(Ingredient.EMPTY, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar, diamondSword, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar);
    	NonNullList recipeRealityStone = NonNullList.from(Ingredient.EMPTY, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar, endCrystal, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar);
    	NonNullList recipeTimeStone = NonNullList.from(Ingredient.EMPTY, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar, clock, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar);
    	NonNullList recipeSoulStone = NonNullList.from(Ingredient.EMPTY, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar, skull, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar);
    	NonNullList recipeMindStone = NonNullList.from(Ingredient.EMPTY, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar, goldenApple, dualCompressedStar, dualCompressedStar, dualCompressedStar, dualCompressedStar);
    	
    	event.getRegistry().registerAll(new TIGShapedRecipe("", 3, 3, recipeSpaceStone, new ItemStack(this.spaceStoneItem)).setRegistryName("space_stone"),
    			new TIGShapedRecipe("", 3, 3, recipePowerStone, new ItemStack(this.powerStoneItem)).setRegistryName("power_stone"),
    			new TIGShapedRecipe("", 3, 3, recipeRealityStone, new ItemStack(this.realityStoneItem)).setRegistryName("reality_stone"),
    			new TIGShapedRecipe("", 3, 3, recipeTimeStone, new ItemStack(this.timeStoneItem)).setRegistryName("time_stone"),
    			new TIGShapedRecipe("", 3, 3, recipeSoulStone, new ItemStack(this.soulStoneItem)).setRegistryName("soul_stone"),
    			new TIGShapedRecipe("", 3, 3, recipeMindStone, new ItemStack(this.mindStoneItem)).setRegistryName("mind_stone"));
    }
    
    public void sync() {
    	ConfigTIGMod.craftedStones = this.stonesCrafted.toArray(new String[this.stonesCrafted.size()]);
    	ConfigManager.sync("thanosinfinitygauntlet", Config.Type.INSTANCE);
    }
    
    @SubscribeEvent
    public void itemCrafted(ItemCraftedEvent event) {
    	if(event.crafting.getItem() instanceof BasicStone) {
    		this.stonesCrafted.add(((BasicStone) event.crafting.getItem()).getType().name());
			this.sync();
    	}
    }
    
    @SubscribeEvent
	public void registerRenders(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(this.spaceStoneItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:space_stone"));
		ModelLoader.setCustomModelResourceLocation(this.powerStoneItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:power_stone"));
		ModelLoader.setCustomModelResourceLocation(this.timeStoneItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:time_stone"));
		ModelLoader.setCustomModelResourceLocation(this.soulStoneItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:soul_stone"));
		ModelLoader.setCustomModelResourceLocation(this.mindStoneItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:mind_stone"));
		ModelLoader.setCustomModelResourceLocation(this.realityStoneItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:reality_stone"));
		ModelLoader.setCustomModelResourceLocation(this.compressedStarItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:compressed_star"));
		ModelLoader.setCustomModelResourceLocation(this.dualCompressedStarItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:dual_compressed_star"));
		ModelLoader.setCustomModelResourceLocation(this.dualCompressedStarItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:dual_compressed_star"));
		ModelLoader.setCustomModelResourceLocation(this.infinityGauntletItem, 0, new ModelResourceLocation("thanosinfinitygauntlet:empty"));
		ModelLoader.setCustomModelResourceLocation(this.infinityGauntletItem, 1, new ModelResourceLocation("thanosinfinitygauntlet:full"));
    }
    
    @SubscribeEvent
	public void onEntityInteract(EntityInteract event) {
		if(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof TimeStone) {
			if(((EntityLivingBase)event.getTarget()).isPotionActive(Potion.getPotionById(2))) {
				((EntityLivingBase)event.getTarget()).removePotionEffect(Potion.getPotionById(2));
			} else {
				((EntityLivingBase)event.getTarget()).addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, 20));
			}
		} else if(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof SoulStone) {
			int currentSelected = event.getEntityPlayer().getHeldItemMainhand().getTagCompound().getInteger("currentSelectedPower");
			
			EnumPowers.SoulStone currentPower = EnumPowers.SoulStone.values()[currentSelected];
			
			if(currentPower == EnumPowers.SoulStone.KILL) {
				((EntityLivingBase)event.getTarget()).attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
			}
		} else if(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof InfinityGauntlet) {
			if(this.infinityGauntletItem.getCurrentStone(event.getEntityPlayer().getHeldItemMainhand()) instanceof SoulStone) {
				int currentSelected = event.getEntityPlayer().getHeldItemMainhand().getTagCompound().getInteger("currentSelectedPower");
				
				EnumPowers.SoulStone currentPower = EnumPowers.SoulStone.values()[currentSelected];
				
				if(currentPower == EnumPowers.SoulStone.KILL) {
					((EntityLivingBase)event.getTarget()).attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
				}
			} else if(this.infinityGauntletItem.getCurrentStone(event.getEntityPlayer().getHeldItemMainhand()) instanceof TimeStone) {
				if(((EntityLivingBase)event.getTarget()).isPotionActive(Potion.getPotionById(2))) {
					((EntityLivingBase)event.getTarget()).removePotionEffect(Potion.getPotionById(2));
				} else {
					((EntityLivingBase)event.getTarget()).addPotionEffect(new PotionEffect(Potion.getPotionById(2), 200000, 5));
				}
			}
		}
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	proxy.postInit(e);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent e) {
    	e.registerServerCommand(new TIGModCommands());
    }
}
