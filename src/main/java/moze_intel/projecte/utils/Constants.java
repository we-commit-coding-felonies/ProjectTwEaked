package moze_intel.projecte.utils;

import java.text.DecimalFormat;

import moze_intel.projecte.config.ProjectEConfig;

public final class Constants 
{
	public static final DecimalFormat EMC_FORMATTER = new DecimalFormat("#,###.##");
	public static final DecimalFormat SINGLE_DP_EMC_FORMATTER = new DecimalFormat("#,###.#");

	public static final long[] MAX_KLEIN_EMC = new long[] {50000, 200000, 800000, 3200000, 12800000, 51200000};
	public static final float[] COLLECTOR_LIGHT_VALS = new float[] {0.0F, 0.25F, 0.5F, 0.75F, 1.0F};
	
	public static final float[] EXPLOSIVE_LENS_RADIUS = new float[] {4.0F, 8.0F, 12.0F, 16.0F, 16.0F, 16.0F, 16.0F, 16.0F};
	public static final long[] EXPLOSIVE_LENS_COST = new long[] {384, 768, 1536, 2304, 2304, 2304, 2304, 2304};
	
	public static final long TILE_MAX_EMC = Long.MAX_VALUE;
	
	public static final long COLLECTOR_MK0_MAX = ProjectEConfig.powerFlowerValues.cMk0Max;
	public static final long COLLECTOR_MK1_MAX = ProjectEConfig.powerFlowerValues.cMk1Max;
	public static final long COLLECTOR_MK2_MAX = ProjectEConfig.powerFlowerValues.cMk2Max;
	public static final long COLLECTOR_MK3_MAX = ProjectEConfig.powerFlowerValues.cMk3Max;
	public static final long COLLECTOR_MK4_MAX = ProjectEConfig.powerFlowerValues.cMk4Max;
	
	public static final long COLLECTOR_MK0_GEN = ProjectEConfig.powerFlowerValues.cMk0Gen;
	public static final long COLLECTOR_MK1_GEN = ProjectEConfig.powerFlowerValues.cMk1Gen;
	public static final long COLLECTOR_MK2_GEN = ProjectEConfig.powerFlowerValues.cMk2Gen;
	public static final long COLLECTOR_MK3_GEN = ProjectEConfig.powerFlowerValues.cMk3Gen;
	public static final long COLLECTOR_MK4_GEN = ProjectEConfig.powerFlowerValues.cMk4Gen;

	public static final long RELAY_MK0_MAX = ProjectEConfig.powerFlowerValues.rMk0Max;
	public static final long RELAY_MK1_MAX = ProjectEConfig.powerFlowerValues.rMk1Max;
	public static final long RELAY_MK2_MAX = ProjectEConfig.powerFlowerValues.rMk2Max;
	public static final long RELAY_MK3_MAX = ProjectEConfig.powerFlowerValues.rMk3Max;
	public static final long RELAY_MK4_MAX = ProjectEConfig.powerFlowerValues.rMk4Max;
	
	public static final long RELAY_MK0_OUTPUT = ProjectEConfig.powerFlowerValues.rMk0Out;
	public static final long RELAY_MK1_OUTPUT = ProjectEConfig.powerFlowerValues.rMk1Out;
	public static final long RELAY_MK2_OUTPUT = ProjectEConfig.powerFlowerValues.rMk2Out;
	public static final long RELAY_MK3_OUTPUT = ProjectEConfig.powerFlowerValues.rMk3Out;
	public static final long RELAY_MK4_OUTPUT = ProjectEConfig.powerFlowerValues.rMk4Out;
	
	public static final int COAL_BURN_TIME = 1600;
	public static final int ALCH_BURN_TIME = COAL_BURN_TIME * 4;
	public static final int MOBIUS_BURN_TIME = ALCH_BURN_TIME * 4;
	public static final int AETERNALIS_BURN_TIME = MOBIUS_BURN_TIME * 4;
	
	public static final int ALCH_CHEST_GUI = 0;
	public static final int ALCH_BAG_GUI = 1;
	public static final int TRANSMUTE_STONE_GUI = 2;
	public static final int CONDENSER_GUI = 3;
	public static final int RM_FURNACE_GUI = 4;
	public static final int DM_FURNACE_GUI = 5;
	public static final int COLLECTOR1_GUI = 6;
	public static final int COLLECTOR2_GUI = 7;
	public static final int COLLECTOR3_GUI = 8;
	public static final int RELAY1_GUI = 9;
	public static final int RELAY2_GUI = 10;
	public static final int RELAY3_GUI = 11;
	public static final int MERCURIAL_GUI = 12;
	public static final int PHILOS_STONE_GUI = 13;
	public static final int TRANSMUTATION_GUI = 14;
	public static final int ETERNAL_DENSITY_GUI = 15;
	public static final int CONDENSER_MK2_GUI = 16;
	public static final int COLLECTOR0_GUI = 17;
	public static final int COLLECTOR4_GUI = 18;
	public static final int RELAY0_GUI = 19;
	public static final int RELAY4_GUI = 20;

	public static final int MAX_CONDENSER_PROGRESS = 102;

	public static final int MAX_VEIN_SIZE = 250;
	
	public static final long ENCH_EMC_BONUS = 5000;
}
