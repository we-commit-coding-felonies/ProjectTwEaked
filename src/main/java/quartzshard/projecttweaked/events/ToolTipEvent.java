package main.java.quartzshard.projecttweaked.events;

import com.google.common.math.LongMath;
import main.java.quartzshard.projecttweaked.PECore;
import main.java.quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import main.java.quartzshard.projecttweaked.api.item.IItemEmc;
import main.java.quartzshard.projecttweaked.api.item.IPedestalItem;
import main.java.quartzshard.projecttweaked.gameObjs.ObjHandler;
import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;
import main.java.quartzshard.projecttweaked.utils.Constants;
import main.java.quartzshard.projecttweaked.utils.EMCHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.math.BigInteger;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PECore.MODID)
public class ToolTipEvent 
{
	@SubscribeEvent
	public static void tTipEvent(ItemTooltipEvent event)
	{
		ItemStack current = event.getItemStack();
		if (current.isEmpty())
		{
			return;
		}
		Item currentItem = current.getItem();
		Block currentBlock = Block.getBlockFromItem(currentItem);
		EntityPlayer clientPlayer = Minecraft.getMinecraft().player;

		if (ProjectTwEakedConfig.misc.pedestalToolTips
			&& currentItem instanceof IPedestalItem)
		{
			event.getToolTip().add(TextFormatting.DARK_PURPLE + I18n.format("pe.pedestal.on_pedestal") + " ");
			List<String> description = ((IPedestalItem) currentItem).getPedestalDescription();
			if (description.isEmpty())
			{
				event.getToolTip().add(IPedestalItem.TOOLTIPDISABLED);
			}
			else
			{
				event.getToolTip().addAll(((IPedestalItem) currentItem).getPedestalDescription());
			}
		}

		if (ProjectTwEakedConfig.misc.odToolTips)
		{
			for (int id : OreDictionary.getOreIDs(current))
			{
				event.getToolTip().add("OD: " + OreDictionary.getOreName(id));
			}
			if (currentBlock instanceof BlockFluidBase) {
				event.getToolTip().add("Fluid: " + ((BlockFluidBase) currentBlock).getFluid().getName());
			}
		}

		if (ProjectTwEakedConfig.misc.emcToolTips)
		{
			if (EMCHelper.doesItemHaveEmc(current))
			{
				long value = EMCHelper.getEmcValue(current);

				event.getToolTip().add(TextFormatting.YELLOW +
						I18n.format("pe.emc.emc_tooltip_prefix") + " " + TextFormatting.WHITE + Constants.EMC_FORMATTER.format(value) + TextFormatting.BLUE + EMCHelper.getEmcSellString(current, 1));

				if (current.getCount() > 1)
				{
					event.getToolTip().add(TextFormatting.YELLOW + I18n.format("pe.emc.stackemc_tooltip_prefix") + " " +
							TextFormatting.WHITE + Constants.EMC_FORMATTER.format(BigInteger.valueOf(value).multiply(BigInteger.valueOf(current.getCount()))) +
							TextFormatting.BLUE + EMCHelper.getEmcSellString(current, current.getCount()));
				}

				if (GuiScreen.isShiftKeyDown()
						&& clientPlayer != null
						&& clientPlayer.getCapability(ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY, null).hasKnowledge(current))
				{
					event.getToolTip().add(TextFormatting.YELLOW + I18n.format("pe.emc.has_knowledge"));
				}
			}
		}

		if (ProjectTwEakedConfig.misc.statToolTips)
		{
			/*
			 * Collector ToolTips
			 */
			String unit = I18n.format("pe.emc.name");
			String rate = I18n.format("pe.emc.rate");

			if (currentBlock == ObjHandler.collectorMK0)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxgenrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.cMk0Gen));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.cMk0Max));
			}

			if (currentBlock == ObjHandler.collectorMK1)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxgenrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.cMk1Gen));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.cMk1Max));
			}

			if (currentBlock == ObjHandler.collectorMK2)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxgenrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.cMk2Gen));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.cMk2Max));
			}

			if (currentBlock == ObjHandler.collectorMK3)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxgenrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.cMk3Gen));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.cMk3Max));
			}

			if (currentBlock == ObjHandler.collectorMK4)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxgenrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.cMk4Gen));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.cMk4Max));
			}

			/*
			 * Relay ToolTips
			 */
			if (currentBlock == ObjHandler.relayMK0)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxoutrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.rMk0Out));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.rMk0Max));
			}

			if (currentBlock == ObjHandler.relay)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxoutrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.rMk1Out));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.rMk1Max));
			}

			if (currentBlock == ObjHandler.relayMK2)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxoutrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.rMk2Out));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.rMk2Max));
			}

			if (currentBlock == ObjHandler.relayMK3)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxoutrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.rMk3Out));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.rMk3Max));
			}

			if (currentBlock == ObjHandler.relayMK4)
			{
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxoutrate_tooltip")
						+ TextFormatting.BLUE + " %d " + rate, ProjectTwEakedConfig.powerFlowerValues.rMk4Out));
				event.getToolTip().add(TextFormatting.DARK_PURPLE
						+ String.format(I18n.format("pe.emc.maxstorage_tooltip")
						+ TextFormatting.BLUE + " %d " + unit, ProjectTwEakedConfig.powerFlowerValues.rMk4Max));
			}
		}

		if (current.hasTagCompound())
		{
			if (current.getItem() instanceof IItemEmc || current.getTagCompound().hasKey("StoredEMC"))
			{
				long value;
				if (current.getTagCompound().hasKey("StoredEMC"))
				{
					value = current.getTagCompound().getLong("StoredEMC");
				} else
				{
					value = ((IItemEmc) current.getItem()).getStoredEmc(current);
				}

				event.getToolTip().add(TextFormatting.YELLOW + I18n.format("pe.emc.storedemc_tooltip") + " " + TextFormatting.RESET + Constants.EMC_FORMATTER.format(value));
			}
		}
	}
}
