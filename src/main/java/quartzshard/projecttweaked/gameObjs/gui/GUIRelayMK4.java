package quartzshard.projecttweaked.gameObjs.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.gameObjs.container.RelayMK4Container;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK4Tile;
import quartzshard.projecttweaked.utils.Constants;

public class GUIRelayMK4 extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(PECore.MODID.toLowerCase(), "textures/gui/relay4.png");
	private final RelayMK4Tile tile;
	private final RelayMK4Container container;
	
	public GUIRelayMK4(InventoryPlayer invPlayer, RelayMK4Tile tile)
	{
		super(new RelayMK4Container(invPlayer, tile));
		this.tile = tile;
		this.xSize = 212;
		this.ySize = 194;
		this.container = (RelayMK4Container) inventorySlots;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString(I18n.format("pe.relay.mk4"), 38, 6, 4210752);
		this.fontRenderer.drawString(Constants.EMC_FORMATTER.format(container.emc), 125, 39, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		//Emc bar progress
		int progress = (int) ((double) container.emc / tile.getMaximumEmc() * 102);
		this.drawTexturedModalRect(x + 105, y + 6, 30, 195, progress, 10);
		
		//Klein start bar progress. Max is 30.
		progress = (int) (container.kleinChargeProgress * 30);
		this.drawTexturedModalRect(x + 153, y + 82, 0, 195, progress, 10);
				
		//Burn Slot bar progress. Max is 30.
		progress = (int) (container.inputBurnProgress * 30);
		drawTexturedModalRect(x + 101, y + 82, 0, 195, progress, 10);
	}	
}
