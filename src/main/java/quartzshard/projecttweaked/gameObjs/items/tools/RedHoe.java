package main.java.quartzshard.projecttweaked.gameObjs.items.tools;

public class RedHoe extends DarkHoe
{
	public RedHoe() 
	{
		super("rm_hoe", (byte)3, new String[]{});
		this.setNoRepair();
		this.peToolMaterial = "rm_tools";
		this.toolClasses.add("hoe");
	}
}
