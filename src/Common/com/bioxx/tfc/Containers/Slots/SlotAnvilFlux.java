package com.bioxx.tfc.Containers.Slots;

import com.bioxx.tfc.TFCItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAnvilFlux extends Slot
{
	public SlotAnvilFlux(IInventory iinventory, int i, int j, int k)
	{
		super(iinventory, i, j, k);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		if(itemstack.getItem() == TFCItems.Powder && itemstack.getItemDamage() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
