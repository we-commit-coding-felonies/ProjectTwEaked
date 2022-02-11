package quartzshard.projecttweaked.api;

import quartzshard.projecttweaked.api.capabilities.IAlchBagProvider;
import quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import quartzshard.projecttweaked.api.proxy.IBlacklistProxy;
import quartzshard.projecttweaked.api.proxy.IConversionProxy;
import quartzshard.projecttweaked.api.proxy.IEMCProxy;
import quartzshard.projecttweaked.api.proxy.ITransmutationProxy;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.FMLLog;

public final class ProjectTwEakedAPI
{
	private static IEMCProxy emcProxy;
	private static ITransmutationProxy transProxy;
	private static IBlacklistProxy blacklistProxy;
	private static IConversionProxy recipeProxy;

	private ProjectTwEakedAPI() {}

	/**
	 * The capability object for IAlchBagProvider
	 */
	@CapabilityInject(IAlchBagProvider.class)
	public static final Capability<IAlchBagProvider> ALCH_BAG_CAPABILITY = null;

	/**
	 * The capability object for IKnowledgeProvider
	 */
	@CapabilityInject(IKnowledgeProvider.class)
	public static final Capability<IKnowledgeProvider> KNOWLEDGE_CAPABILITY = null;

	/**
	 * Retrieves the proxy for EMC-based API queries.
	 * @return The proxy for EMC-based API queries
	 */
	public static IEMCProxy getEMCProxy()
	{
		if (emcProxy == null)
		{
			try
			{
				Class<?> clazz = Class.forName("quartzshard.projecttweaked.impl.EMCProxyImpl");
				emcProxy = (IEMCProxy) clazz.getField("instance").get(null);
			} catch (ReflectiveOperationException ex)
			{
				FMLLog.warning("[ProjectTwEakedAPI] Error retrieving EMCProxyImpl, ProjectTwEaked may be absent, damaged, or outdated.");
			}
		}
		return emcProxy;
	}

	/**
	 * Retrieves the proxy for EMC-Recipe-Calculation-based API queries.
	 * @return The proxy for EMC-Recipe-Calculation-based API queries
	 */
	public static IConversionProxy getConversionProxy()
	{
		if (recipeProxy == null)
		{
			try
			{
				Class<?> clazz = Class.forName("quartzshard.projecttweaked.impl.ConversionProxyImpl");
				recipeProxy = (IConversionProxy) clazz.getField("instance").get(null);
			} catch (ReflectiveOperationException ex)
			{
				FMLLog.warning("[ProjectTwEakedAPI] Error retrieving ConversionProxyImpl, ProjectTwEaked may be absent, damaged, or outdated.");
			}
		}
		return recipeProxy;
	}

	/**
	 * Retrieves the proxy for Transmutation-based API queries.
	 * @return The proxy for Transmutation-based API queries
	 */
	public static ITransmutationProxy getTransmutationProxy()
	{
		if (transProxy == null)
		{
			try
			{
				Class<?> clazz = Class.forName("quartzshard.projecttweaked.impl.TransmutationProxyImpl");
				transProxy = (ITransmutationProxy) clazz.getField("instance").get(null);
			} catch (ReflectiveOperationException ex)
			{
				FMLLog.warning("[ProjectTwEakedAPI] Error retrieving TransmutationProxyImpl, ProjectTwEaked may be absent, damaged, or outdated.");
			}
		}
		return transProxy;
	}

	/**
	 * Retrieves the proxy for black/whitelist-based API queries.
	 * @return The proxy for black/whitelist-based API queries
	 */
	public static IBlacklistProxy getBlacklistProxy()
	{
		if (blacklistProxy == null)
		{
			try
			{
				Class<?> clazz = Class.forName("quartzshard.projecttweaked.impl.BlacklistProxyImpl");
				blacklistProxy = (IBlacklistProxy) clazz.getField("instance").get(null);
			} catch (ReflectiveOperationException ex)
			{
				FMLLog.warning("[ProjectTwEakedAPI] Error retrieving BlacklistProxyImpl, ProjectTwEaked may be absent, damaged, or outdated.");
			}
		}
		return blacklistProxy;
	}
}