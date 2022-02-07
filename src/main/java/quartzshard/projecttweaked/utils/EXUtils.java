package main.java.quartzshard.projecttweaked.utils;

import main.java.quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import main.java.quartzshard.projecttweaked.api.event.PlayerAttemptLearnEvent;
import main.java.quartzshard.projecttweaked.utils.NBTWhitelist;
import main.java.quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import main.java.quartzshard.projecttweaked.PECore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraft.nbt.CompressedStreamTools;


/**
 * @author LatvianModder
 */
public class EXUtils
{
	public static ItemStack fixOutput(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(stack, 1);

		if (!stack1.getHasSubtypes() && stack1.isItemStackDamageable())
		{
			stack1.setItemDamage(0);
		}

		if (!NBTWhitelist.shouldDupeWithNBT(stack1))
		{
			stack1.setTagCompound(null);

			try
			{
				stack1.getItem().readNBTShareTag(stack1, null);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				//Catch exception if readNBTShareTag fails
			}
		}

		return stack1;
	}

	public static int addKnowledge(EntityPlayer player, IKnowledgeProvider knowledgeProvider, ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		if (!knowledgeProvider.hasKnowledge(stack))
		{
			if (MinecraftForge.EVENT_BUS.post(new PlayerAttemptLearnEvent(player, stack)))
			{
				return 0;
			}

			knowledgeProvider.addKnowledge(stack);
			return 2;
		}

		return 1;
	}

    public static void addEMC(TransmutationInventory transmutationInventory, long add)
	{
		long l = Math.min(add, Long.MAX_VALUE - transmutationInventory.getAvailableEMC());

		if (l > 0L)
		{
			transmutationInventory.addEmc(l);
		}
	}

	public static void removeEMC(TransmutationInventory transmutationInventory, long remove)
	{
		long l = Math.min(transmutationInventory.getAvailableEMC(), remove);

		if (l > 0L)
		{
			transmutationInventory.removeEmc(l);
		}
	}
	@Nullable
	public static IKnowledgeProvider getEMC(@Nullable World world, @Nullable UUID id)
	{
		if (world == null || id == null || id.getLeastSignificantBits() == 0L && id.getMostSignificantBits() == 0L)
		{
			return null;
		}

		if (world.isRemote)
		{
			EntityPlayer player = PECore.proxy.getClientPlayer();
			return player == null ? null : get(player);
		}

		EntityPlayer player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(id);

		if (player != null)
		{
			return get(player);
		}

		OfflineKnowledgeProvider provider = OFFLINE_MAP.get(id);

		if (provider == null)
		{
			provider = new OfflineKnowledgeProvider(id);
			OFFLINE_MAP.put(provider.playerId, provider);

			File playerDataFolder = new File(world.getSaveHandler().getWorldDirectory(), "playerdata");

			if (playerDataFolder.exists())
			{
				File playerFile = new File(playerDataFolder, provider.playerId + ".dat");

				if (playerFile.exists() && playerFile.isFile())
				{
					try (FileInputStream in = new FileInputStream(playerFile))
					{
						NBTTagCompound nbt = CompressedStreamTools.readCompressed(in).getCompoundTag("ForgeCaps").getCompoundTag(KnowledgeImpl.Provider.NAME.toString());

						if (!nbt.isEmpty())
						{
							provider.deserializeNBT(nbt);
						}
					}
					catch (Throwable ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}

		return provider;
	}

	public static IKnowledgeProvider getEMC(EntityPlayer player)
	{
		return Objects.requireNonNull(player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null));
	}
}