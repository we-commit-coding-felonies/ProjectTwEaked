package quartzshard.projecttweaked.gameObjs.items.itemBlocks;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMatterBlock extends ItemBlock
{
	public ItemMatterBlock(Block block) 
	{
		super(block);
		this.setMaxDamage(0);
		this.hasSubtypes = true;
	}
	
	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack)
	{
		if (stack.getItemDamage() == 0)
		{
			return "tile.pe_dm_block";
		}
		else
		{
			return "tile.pe_rm_block";
		}
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
}
