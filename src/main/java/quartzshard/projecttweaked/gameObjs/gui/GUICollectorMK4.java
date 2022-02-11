package quartzshard.projecttweaked.gameObjs.gui;

import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.gameObjs.container.CollectorMK4Container;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK4Tile;
import quartzshard.projecttweaked.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUICollectorMK4 extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(PECore.MODID.toLowerCase(), "textures/gui/collector4.png");
	private final CollectorMK4Tile tile;
	private final CollectorMK4Container container;
	
	public GUICollectorMK4(InventoryPlayer invPlayer, CollectorMK4Tile tile)
	{
		super(new CollectorMK4Container(invPlayer, tile));
		this.tile = tile;
		this.container = ((CollectorMK4Container) inventorySlots);
		this.xSize = 218;
		this.ySize = 165;
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
		this.fontRenderer.drawString(Long.toString(container.emc), 91, 32, 4210752);
		
		long kleinCharge = container.kleinEmc;
		if (kleinCharge > 0)
			this.fontRenderer.drawString(Constants.EMC_FORMATTER.format(kleinCharge), 91, 44, 4210752);
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
		
		this.drawTexturedModalRect(x + 160, y + 49 - progress, 220, 13 - progress, 12, progress);
				
		//EMC storage. Max is 48
		this.drawTexturedModalRect(x + 98, y + 18, 0, 166, (int) (container.emc / tile.getMaximumEmc() * 48), 10);
				
		//Klein Star Charge Progress. Max is 48
		progress = (int) (container.kleinChargeProgress * 48);
		this.drawTexturedModalRect(x + 98, y + 58, 0, 166, progress, 10);
		
		//Fuel Progress. Max is 24.
		progress = (int) (container.fuelProgress * 24);
		this.drawTexturedModalRect(x + 172, y + 55 - progress, 219, 38 - progress, 10, progress + 1);
	}
}
