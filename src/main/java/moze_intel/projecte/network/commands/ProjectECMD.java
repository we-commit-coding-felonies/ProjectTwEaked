package moze_intel.projecte.network.commands;

import javax.annotation.Nonnull;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class ProjectECMD extends CommandTreeBase
{
	public ProjectECMD()
	{
		addSubcommand(new ClearKnowledgeCMD());
		addSubcommand(new ReloadEmcCMD());
		addSubcommand(new RemoveEmcCMD());
		addSubcommand(new ResetEmcCMD());
		addSubcommand(new SetEmcCMD());
		addSubcommand(new ShowBagCMD());
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "projecte";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "pe.command.main.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() 
	{
		return 0;
	}
}
