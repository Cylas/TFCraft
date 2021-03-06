package com.bioxx.tfc.Containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.Containers.Slots.SlotArmorTFC;
import com.bioxx.tfc.Containers.Slots.SlotExtraEquipable;
import com.bioxx.tfc.Core.Player.PlayerInventory;
import com.bioxx.tfc.Handlers.CraftingHandler;
import com.bioxx.tfc.Handlers.FoodCraftingHandler;
import com.bioxx.tfc.Items.ItemTFCArmor;
import com.bioxx.tfc.api.Interfaces.IFood;

public class ContainerPlayerTFC extends ContainerPlayer
{
	private final EntityPlayer thePlayer;
	
	public ContainerPlayerTFC(InventoryPlayer playerInv, boolean par2, EntityPlayer player)
	{
		super(playerInv, par2, player);
		this.craftMatrix = new InventoryCrafting(this, 3, 3);
		this.inventorySlots.clear();
		this.inventoryItemStacks.clear();
		this.thePlayer = player;
		this.addSlotToContainer(new SlotCrafting(player, craftMatrix, craftResult, 0, 152, 36));
		int x;
		int y;

		for (x = 0; x < 2; ++x)
		{
			for (y = 0; y < 2; ++y)
				this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18, 18 + x * 18));
		}
		
		for (x = 0; x < playerInv.armorInventory.length; ++x)
		{
			int index = playerInv.getSizeInventory() - 1 - x;
			this.addSlotToContainer(new SlotArmorTFC(this, playerInv, index, 8, 8 + x * 18, x));
		}
		PlayerInventory.buildInventoryLayout(this, playerInv, 8, 90, false, true);
		PlayerInventory.addExtraEquipables(this, playerInv, 8, 90, false);
		
		//Manually built the remaining crafting slots because of an order issue. These have to be created after the default slots
		if(player.getEntityData().hasKey("craftingTable") || !player.worldObj.isRemote)
		{
			x = 2; y = 0; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18, 18 + x * 18));
			x = 2; y = 1; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18, 18 + x * 18));
			x = 0; y = 2; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18, 18 + x * 18));
			x = 1; y = 2; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18, 18 + x * 18));
			x = 2; y = 2; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18, 18 + x * 18));
		}
		else
		{
			//Have to create some dummy slots
			x = 2; y = 0; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18-50000, 18 + x * 18));
			x = 2; y = 1; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18-50000, 18 + x * 18));
			x = 0; y = 2; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18-50000, 18 + x * 18));
			x = 1; y = 2; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18-50000, 18 + x * 18));
			x = 2; y = 2; this.addSlotToContainer(new Slot(craftMatrix, y + x * 3, 82 + y * 18-50000, 18 + x * 18));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		for (int i = 0; i < 9; ++i)
		{
			ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
			if (itemstack != null)
				par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
		}

		this.craftResult.setInventorySlotContents(0, (ItemStack)null);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2)
	{
		ItemStack origStack = null;
		Slot slot = (Slot)this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack(); 
			origStack = slotStack.copy();

			if (par2 == 0)
			{
				FoodCraftingHandler.preCraft(player, slotStack, craftMatrix);
				CraftingHandler.preCraft(player, slotStack, craftMatrix);

				if (!this.mergeItemStack(slotStack, 9, 45, true))
					return null;

				slot.onSlotChange(slotStack, origStack);
			}
			else if ((par2 >= 1 && par2 < 5) || (player.getEntityData().hasKey("craftingTable") && (par2 >= 45 && par2 < 50)))
			{
				if (!this.mergeItemStack(slotStack, 9, 45, false))
					return null;
			}
			else if (par2 >= 5 && par2 < 9 || par2 == 50)
			{
				if (!this.mergeItemStack(slotStack, 9, 45, false))
					return null;
			}
			else if (origStack.getItem() instanceof ItemArmor)
			{
				if(origStack.getItem() instanceof ItemTFCArmor &&
						((!((Slot)this.inventorySlots.get(5 + ((ItemTFCArmor)origStack.getItem()).getUnadjustedArmorType())).getHasStack() && ((ItemTFCArmor)origStack.getItem()).getUnadjustedArmorType() != 4)||
						(!((Slot)this.inventorySlots.get(50)).getHasStack())))
				{
					int j = ((ItemTFCArmor)origStack.getItem()).getUnadjustedArmorType() != 4 ? 5 + ((ItemTFCArmor)origStack.getItem()).getUnadjustedArmorType() : 50;
					if (!this.mergeItemStack(slotStack, j, j + 1, false))
						return null;
				}
				else if(((!((Slot)this.inventorySlots.get(5 + ((ItemArmor)origStack.getItem()).armorType)).getHasStack() && ((ItemArmor)origStack.getItem()).armorType != 4)||
						(!((Slot)this.inventorySlots.get(50)).getHasStack())))
				{
					int j = ((ItemArmor)origStack.getItem()).armorType != 4 ? 5 + ((ItemArmor)origStack.getItem()).armorType : 50;
					if (!this.mergeItemStack(slotStack, j, j + 1, false))
						return null;
				}
			}
			else if (par2 >= 9 && par2 < 45 && origStack.getItem() instanceof IFood && !isCraftingGridFull())
			{
				if (!this.mergeItemStack(slotStack, 1, 5, false) && slotStack.stackSize == 0)
					return null;
				else if (slotStack.stackSize > 0 && player.getEntityData().hasKey("craftingTable") && !this.mergeItemStack(slotStack, 45, 50, false))
					return null;
				else if (slotStack.stackSize > 0 && par2 >= 9 && par2 < 36)
				{
					if (!this.mergeItemStack(slotStack, 36, 45, false))
						return null;
				}
				else if (slotStack.stackSize > 0 && par2 >= 36 && par2 < 45)
				{
					if (!this.mergeItemStack(slotStack, 9, 36, false))
						return null;
				}
			}
			else if (par2 >= 9 && par2 < 36)
			{
				if (!this.mergeItemStack(slotStack, 36, 45, false))
					return null;
			}
			else if (par2 >= 36 && par2 < 45)
			{
				if (!this.mergeItemStack(slotStack, 9, 36, false))
					return null;
			}
			else if (!this.mergeItemStack(slotStack, 9, 45, false))
				return null;

			if (slotStack.stackSize == 0)
				slot.putStack((ItemStack)null);
			else
				slot.onSlotChanged();

			if (slotStack.stackSize == origStack.stackSize)
				return null;

			slot.onPickupFromSlot(player, slotStack);
		}

		return origStack;
	}

	protected boolean isCraftingGridFull()
	{
		for(int i = 0; i < this.craftMatrix.getSizeInventory(); i++)
		{
			if(this.craftMatrix.getStackInSlot(i) == null)
				return false;
		}
		return true;
	}

	public EntityPlayer getPlayer()
	{
		return this.thePlayer;
	}
}
