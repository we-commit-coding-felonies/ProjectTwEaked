package moze_intel.projecte.gameObjs.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import moze_intel.projecte.PECore;
import moze_intel.projecte.gameObjs.container.CollectorMK1Container;
import moze_intel.projecte.gameObjs.tiles.CollectorMK1Tile;
import moze_intel.projecte.utils.Constants;

public class GUICollectorMK1 extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(PECore.MODID.toLowerCase(), "textures/gui/collector1.png");
	private final CollectorMK1Tile tile;
	private final CollectorMK1Container container;
	
	public GUICollectorMK1(InventoryPlayer invPlayer, CollectorMK1Tile tile)
	{
		super(new CollectorMK1Container(invPlayer, tile));
		this.container = ((CollectorMK1Container) inventorySlots);
		this.tile = tile;
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
		this.fontRenderer.drawString(Long.toString(container.emc), 60, 32, 4210752);
		
		long kleinCharge = container.kleinEmc;

		if (kleinCharge > 0)
			this.fontRenderer.drawString(Constants.EMC_FORMATTER.format(kleinCharge), 60, 44, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		//Light Level. Max is 12
		int progress = (int) (container.sunLevel * 12.0 / 16);
		this.drawTexturedModalRect(x + 126, y + 49 - progress, 177, 13 - progress, 12, progress);
		
		//EMC storage. Max is 48
		progress = (int) ((double) container.emc / tile.getMaximumEmc() * 48);
		this.drawTexturedModalRect(x + 64, y + 18, 0, 166, progress, 10);
		
		//Klein Star Charge Progress. Max is 48
		progress = (int) (container.kleinChargeProgress * 48);
		this.drawTexturedModalRect(x + 64, y + 58, 0, 166, progress, 10);
		
		//Fuel Progress. Max is 24.
		progress = (int) (container.fuelProgress * 24);
		this.drawTexturedModalRect(x + 138, y + 55 - progress, 176, 38 - progress, 10, progress + 1);
	}
}
