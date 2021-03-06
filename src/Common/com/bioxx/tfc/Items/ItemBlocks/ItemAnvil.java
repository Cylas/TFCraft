package com.bioxx.tfc.Items.ItemBlocks;

import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ISmeltable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public abstract class ItemAnvil extends ItemTerraBlock implements ISmeltable
{
	public ItemAnvil(Block par1)
	{
		super(par1);
	}

	@Override
	public EnumSize getSize(ItemStack is) {
		return EnumSize.HUGE;
	}

	@Override
	public EnumWeight getWeight(ItemStack is) {
		return EnumWeight.HEAVY;
	}

	@Override
	public short GetMetalReturnAmount(ItemStack is) {

		return 1400;
	}

	@Override
	public boolean isSmeltable(ItemStack is) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public EnumTier GetSmeltTier(ItemStack is) {
		// TODO Auto-generated method stub
		return EnumTier.TierI;
	}
}
