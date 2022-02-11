package quartzshard.projecttweaked.utils;

import com.google.common.collect.ImmutableSet;
import quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import quartzshard.projecttweaked.gameObjs.container.inventory.EternalDensityInventory;
import quartzshard.projecttweaked.gameObjs.container.inventory.MercurialEyeInventory;
import quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import quartzshard.projecttweaked.gameObjs.container.AlchBagContainer;
import quartzshard.projecttweaked.gameObjs.container.AlchChestContainer;
import quartzshard.projecttweaked.gameObjs.container.CollectorMK0Container;
import quartzshard.projecttweaked.gameObjs.container.CollectorMK1Container;
import quartzshard.projecttweaked.gameObjs.container.CollectorMK2Container;
import quartzshard.projecttweaked.gameObjs.container.CollectorMK3Container;
import quartzshard.projecttweaked.gameObjs.container.CollectorMK4Container;
import quartzshard.projecttweaked.gameObjs.container.CondenserContainer;
import quartzshard.projecttweaked.gameObjs.container.CondenserMK2Container;
import quartzshard.projecttweaked.gameObjs.container.DMFurnaceContainer;
import quartzshard.projecttweaked.gameObjs.container.EternalDensityContainer;
import quartzshard.projecttweaked.gameObjs.container.MercurialEyeContainer;
import quartzshard.projecttweaked.gameObjs.container.PhilosStoneContainer;
import quartzshard.projecttweaked.gameObjs.container.RMFurnaceContainer;
import quartzshard.projecttweaked.gameObjs.container.RelayMK0Container;
import quartzshard.projecttweaked.gameObjs.container.RelayMK1Container;
import quartzshard.projecttweaked.gameObjs.container.RelayMK2Container;
import quartzshard.projecttweaked.gameObjs.container.RelayMK3Container;
import quartzshard.projecttweaked.gameObjs.container.RelayMK4Container;
import quartzshard.projecttweaked.gameObjs.container.TransmutationContainer;
import quartzshard.projecttweaked.gameObjs.gui.GUIAlchChest;
import quartzshard.projecttweaked.gameObjs.gui.GUICollectorMK0;
import quartzshard.projecttweaked.gameObjs.gui.GUICollectorMK1;
import quartzshard.projecttweaked.gameObjs.gui.GUICollectorMK2;
import quartzshard.projecttweaked.gameObjs.gui.GUICollectorMK3;
import quartzshard.projecttweaked.gameObjs.gui.GUICollectorMK4;
import quartzshard.projecttweaked.gameObjs.gui.GUICondenser;
import quartzshard.projecttweaked.gameObjs.gui.GUICondenserMK2;
import quartzshard.projecttweaked.gameObjs.gui.GUIDMFurnace;
import quartzshard.projecttweaked.gameObjs.gui.GUIEternalDensity;
import quartzshard.projecttweaked.gameObjs.gui.GUIMercurialEye;
import quartzshard.projecttweaked.gameObjs.gui.GUIPhilosStone;
import quartzshard.projecttweaked.gameObjs.gui.GUIRMFurnace;
import quartzshard.projecttweaked.gameObjs.gui.GUIRelayMK0;
import quartzshard.projecttweaked.gameObjs.gui.GUIRelayMK1;
import quartzshard.projecttweaked.gameObjs.gui.GUIRelayMK2;
import quartzshard.projecttweaked.gameObjs.gui.GUIRelayMK3;
import quartzshard.projecttweaked.gameObjs.gui.GUIRelayMK4;
import quartzshard.projecttweaked.gameObjs.gui.GUITransmutation;
import quartzshard.projecttweaked.gameObjs.tiles.AlchChestTile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK0Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK1Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK2Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK3Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK4Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserMK2Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserTile;
import quartzshard.projecttweaked.gameObjs.tiles.DMFurnaceTile;
import quartzshard.projecttweaked.gameObjs.tiles.RMFurnaceTile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK0Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK1Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK2Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK3Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK4Tile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Set;

public class GuiHandler implements IGuiHandler
{

	private static final Set<Integer> ITEM_IDS = ImmutableSet.of(
			Constants.ALCH_BAG_GUI,
			Constants.MERCURIAL_GUI,
			Constants.PHILOS_STONE_GUI,
			Constants.TRANSMUTATION_GUI,
			Constants.ETERNAL_DENSITY_GUI
	);

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = !ITEM_IDS.contains(ID) ? world.getTileEntity(new BlockPos(x, y, z)) : null;

		// if not a TE, x will hold which hand it came from. 1 for off, 0 otherwise
		EnumHand hand = ITEM_IDS.contains(ID) ? (x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND) : null;
		
		switch (ID)
		{
			case Constants.ALCH_CHEST_GUI:
				if (tile != null && tile instanceof AlchChestTile)
					return new AlchChestContainer(player.inventory, (AlchChestTile) tile);
				break;
			case Constants.ALCH_BAG_GUI: {
				EnumDyeColor color = EnumDyeColor.byMetadata(player.getHeldItem(hand).getItemDamage());
				IItemHandlerModifiable inventory = (IItemHandlerModifiable) player.getCapability(ProjectTwEakedAPI.ALCH_BAG_CAPABILITY, null).getBag(color);
				return new AlchBagContainer(player.inventory, hand, inventory);
			}
			case Constants.CONDENSER_GUI:
				if (tile != null && tile instanceof CondenserTile)
					return new CondenserContainer(player.inventory, (CondenserTile) tile);
				break;
			case Constants.TRANSMUTE_STONE_GUI:
				return new TransmutationContainer(player.inventory, new TransmutationInventory(player), null);
			case Constants.RM_FURNACE_GUI:
				if (tile != null && tile instanceof RMFurnaceTile)
					return new RMFurnaceContainer(player.inventory, (RMFurnaceTile) tile);
				break;
			case Constants.DM_FURNACE_GUI:
				if (tile != null && tile instanceof DMFurnaceTile)
					return new DMFurnaceContainer(player.inventory, (DMFurnaceTile) tile);
				break;
			case Constants.COLLECTOR0_GUI:
				if (tile != null && tile instanceof CollectorMK0Tile)
					return new CollectorMK0Container(player.inventory, (CollectorMK0Tile) tile);
				break;
			case Constants.COLLECTOR1_GUI:
				if (tile != null && tile instanceof CollectorMK1Tile)
					return new CollectorMK1Container(player.inventory, (CollectorMK1Tile) tile);
				break;
			case Constants.COLLECTOR2_GUI:
				if (tile != null && tile instanceof CollectorMK2Tile)
					return new CollectorMK2Container(player.inventory, (CollectorMK2Tile) tile);
				break;
			case Constants.COLLECTOR3_GUI:
				if (tile != null && tile instanceof CollectorMK3Tile)
					return new CollectorMK3Container(player.inventory, (CollectorMK3Tile) tile);
				break;
			case Constants.COLLECTOR4_GUI:
				if (tile != null && tile instanceof CollectorMK4Tile)
					return new CollectorMK4Container(player.inventory, (CollectorMK4Tile) tile);
				break;
			case Constants.RELAY0_GUI:
				if (tile != null && tile instanceof RelayMK0Tile)
					return new RelayMK0Container(player.inventory, (RelayMK0Tile) tile);
				break;
			case Constants.RELAY1_GUI:
				if (tile != null && tile instanceof RelayMK1Tile)
					return new RelayMK1Container(player.inventory, (RelayMK1Tile) tile);
				break;
			case Constants.RELAY2_GUI:
				if (tile != null && tile instanceof RelayMK2Tile)
					return new RelayMK2Container(player.inventory, (RelayMK2Tile) tile);
				break;
			case Constants.RELAY3_GUI:
				if (tile != null && tile instanceof RelayMK3Tile)
					return new RelayMK3Container(player.inventory, (RelayMK3Tile) tile);
				break;
			case Constants.RELAY4_GUI:
				if (tile != null && tile instanceof RelayMK4Tile)
					return new RelayMK4Container(player.inventory, (RelayMK4Tile) tile);
				break;
			case Constants.MERCURIAL_GUI:
				return new MercurialEyeContainer(player.inventory, new MercurialEyeInventory(player.getHeldItem(hand)));
			case Constants.PHILOS_STONE_GUI:
				return new PhilosStoneContainer(player.inventory);
			case Constants.TRANSMUTATION_GUI:
				return new TransmutationContainer(player.inventory, new TransmutationInventory(player), hand);
			case Constants.ETERNAL_DENSITY_GUI:
				return new EternalDensityContainer(player.inventory, new EternalDensityInventory(player.getHeldItem(hand), player));
			case Constants.CONDENSER_MK2_GUI:
				return new CondenserMK2Container(player.inventory, (CondenserMK2Tile) tile);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tile = !ITEM_IDS.contains(ID) ? world.getTileEntity(new BlockPos(x, y, z)) : null;

		// if not a TE, x will hold which hand it came from. 1 for off, 0 otherwise
		EnumHand hand = ITEM_IDS.contains(ID) ? (x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND) : null;

		switch (ID)
		{
			case Constants.ALCH_CHEST_GUI:
				if (tile != null && tile instanceof AlchChestTile)
					return new GUIAlchChest(player.inventory, (AlchChestTile) tile);
				break;
			case Constants.ALCH_BAG_GUI: {
				EnumDyeColor color = EnumDyeColor.byMetadata(player.getHeldItem(hand).getItemDamage());
				IItemHandlerModifiable inventory = (IItemHandlerModifiable) player.getCapability(ProjectTwEakedAPI.ALCH_BAG_CAPABILITY, null).getBag(color);
				return new GUIAlchChest(player.inventory, hand, inventory);
			}
			case Constants.CONDENSER_GUI:
				if (tile != null && tile instanceof CondenserTile)
					return new GUICondenser(player.inventory, (CondenserTile) tile);
				break;
			case Constants.TRANSMUTE_STONE_GUI:
				return new GUITransmutation(player.inventory, new TransmutationInventory(player), null);
			case Constants.RM_FURNACE_GUI:
				if (tile != null && tile instanceof RMFurnaceTile)
					return new GUIRMFurnace(player.inventory, (RMFurnaceTile) tile);
				break;
			case Constants.DM_FURNACE_GUI:
				if (tile != null && tile instanceof DMFurnaceTile)
					return new GUIDMFurnace(player.inventory, (DMFurnaceTile) tile);
				break;
			case Constants.COLLECTOR0_GUI:
				if (tile != null && tile instanceof CollectorMK0Tile)
					return new GUICollectorMK0(player.inventory, (CollectorMK0Tile) tile);
				break;
			case Constants.COLLECTOR1_GUI:
				if (tile != null && tile instanceof CollectorMK1Tile)
					return new GUICollectorMK1(player.inventory, (CollectorMK1Tile) tile);
				break;
			case Constants.COLLECTOR2_GUI:
				if (tile != null && tile instanceof CollectorMK2Tile)
					return new GUICollectorMK2(player.inventory, (CollectorMK2Tile) tile);
				break;
			case Constants.COLLECTOR3_GUI:
				if (tile != null && tile instanceof CollectorMK3Tile)
					return new GUICollectorMK3(player.inventory, (CollectorMK3Tile) tile);
				break;
			case Constants.COLLECTOR4_GUI:
				if (tile != null && tile instanceof CollectorMK4Tile)
					return new GUICollectorMK4(player.inventory, (CollectorMK4Tile) tile);
				break;
			case Constants.RELAY0_GUI:
				if (tile != null && tile instanceof RelayMK0Tile)
					return new GUIRelayMK0(player.inventory, (RelayMK0Tile) tile);
				break;
			case Constants.RELAY1_GUI:
				if (tile != null && tile instanceof RelayMK1Tile)
					return new GUIRelayMK1(player.inventory, (RelayMK1Tile) tile);
				break;
			case Constants.RELAY2_GUI:
				if (tile != null && tile instanceof RelayMK2Tile)
					return new GUIRelayMK2(player.inventory, (RelayMK2Tile) tile);
				break;
			case Constants.RELAY3_GUI:
				if (tile != null && tile instanceof RelayMK3Tile)
					return new GUIRelayMK3(player.inventory, (RelayMK3Tile) tile);
				break;
			case Constants.RELAY4_GUI:
				if (tile != null && tile instanceof RelayMK4Tile)
					return new GUIRelayMK4(player.inventory, (RelayMK4Tile) tile);
				break;
			case Constants.MERCURIAL_GUI:
				return new GUIMercurialEye(player.inventory, new MercurialEyeInventory(player.getHeldItem(hand)));
			case Constants.PHILOS_STONE_GUI:
				return new GUIPhilosStone(player.inventory);
			case Constants.TRANSMUTATION_GUI:
				return new GUITransmutation(player.inventory, new TransmutationInventory(player), hand);
			case Constants.ETERNAL_DENSITY_GUI:
				return new GUIEternalDensity(player.inventory, new EternalDensityInventory(player.getHeldItem(hand), player));
			case Constants.CONDENSER_MK2_GUI:
				return new GUICondenserMK2(player.inventory, (CondenserMK2Tile) tile);
		}
		
		return null;
	}
}
