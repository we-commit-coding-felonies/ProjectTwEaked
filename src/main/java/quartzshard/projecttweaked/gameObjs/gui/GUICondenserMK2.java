package quartzshard.projecttweaked.gameObjs.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.gameObjs.container.CondenserMK2Container;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserMK2Tile;

public class GUICondenserMK2 extends GUICondenser
{
	public GUICondenserMK2(InventoryPlayer invPlayer, CondenserMK2Tile tile)
	{
		super(new CondenserMK2Container(invPlayer, tile), new ResourceLocation(PECore.MODID.toLowerCase(), "textures/gui/condenser_mk2.png"));
	}
}
