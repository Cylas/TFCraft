package com.bioxx.tfc.Core.Player;

import com.bioxx.tfc.Core.TFC_Core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryPlayerTFC extends InventoryPlayer {

	public ItemStack[] extraEquipInventory = new ItemStack[TFC_Core.getExtraEquipInventorySize()];
	
	public InventoryPlayerTFC(EntityPlayer par1EntityPlayer) {
		super(par1EntityPlayer);
		
	}
	
	@Override
	public void damageArmor(float par1)
	{
		par1 /= 4.0F;
		if (par1 < 1.0F)
			par1 = 1.0F;

		for (int i = 0; i < this.armorInventory.length; ++i)
		{
			if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor)
			{
				this.armorInventory[i].damageItem((int) par1, this.player);
				if (this.armorInventory[i].stackSize == 0)
					this.armorInventory[i] = null;
			}
		}
	}

	@Override
	public int getSizeInventory()
	{
		return this.mainInventory.length + armorInventory.length + this.extraEquipInventory.length;
	}

	@Override
	public void readFromNBT(NBTTagList par1NBTTagList)
	{
		this.mainInventory = new ItemStack[36];
		this.armorInventory = new ItemStack[4];
		this.extraEquipInventory = new ItemStack[TFC_Core.getExtraEquipInventorySize()];

		for (int i = 0; i < par1NBTTagList.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = par1NBTTagList.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
			if (itemstack != null)
			{
				if (j >= 0 && j < this.mainInventory.length)
					this.mainInventory[j] = itemstack;
				if (j >= 100 && j < this.armorInventory.length + 100)
					this.armorInventory[j - 100] = itemstack;
				if (j >= 150 && j < this.extraEquipInventory.length + 150)
					this.extraEquipInventory[j - 150] = itemstack;
			}
		}
	}
	
	@Override
	/**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        ItemStack[] aitemstack = this.mainInventory;
        if (par1 >= this.mainInventory.length + this.extraEquipInventory.length)
        {
            par1 -= this.mainInventory.length + this.extraEquipInventory.length;
            aitemstack = this.armorInventory;
        }
        else if(par1 >= this.mainInventory.length){
        	par1-= aitemstack.length;
        	aitemstack = this.extraEquipInventory;
        }
        return aitemstack[par1];
    }
	
	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
    {
        ItemStack[] aitemstack = this.mainInventory;
        
        if (par1 >= this.mainInventory.length + this.extraEquipInventory.length)
        {
            aitemstack = this.armorInventory;
            par1 -= this.mainInventory.length + this.extraEquipInventory.length;
        }
        else if(par1 >= this.mainInventory.length){
        	par1-= aitemstack.length;
        	aitemstack = this.extraEquipInventory;
        }
        if (aitemstack[par1] != null)
        {
            ItemStack itemstack = aitemstack[par1];
            aitemstack[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

	@Override
	public int clearInventory(Item p_146027_1_, int p_146027_2_)
    {
		for(int i = 0; i < this.extraEquipInventory.length; i++){
			this.extraEquipInventory[i] = null;
		}
		return super.clearInventory(p_146027_1_, p_146027_2_);
    }
	
	@Override
	public void decrementAnimations()
    {
        for (int i = 0; i < this.extraEquipInventory.length; ++i)
        {
            if (this.extraEquipInventory[i] != null)
            {
                this.extraEquipInventory[i].updateAnimation(this.player.worldObj, this.player, i, this.currentItem == i);
            }
        }
        super.decrementAnimations();
    }
	
	@Override
	public ItemStack decrStackSize(int par1, int par2)
    {
		ItemStack[] aitemstack = this.mainInventory;

        if (par1 >= this.mainInventory.length + this.extraEquipInventory.length)
        {
            aitemstack = this.armorInventory;
            par1 -= this.mainInventory.length + this.extraEquipInventory.length;
        }
        else if(par1 >= this.mainInventory.length){
        	par1-= aitemstack.length;
        	aitemstack = this.extraEquipInventory;
        }
       

        if (aitemstack[par1] != null)
        {
            ItemStack itemstack;

            if (aitemstack[par1].stackSize <= par2)
            {
                itemstack = aitemstack[par1];
                aitemstack[par1] = null;
                return itemstack;
            }
            else
            {
                itemstack = aitemstack[par1].splitStack(par2);

                if (aitemstack[par1].stackSize == 0)
                {
                    aitemstack[par1] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }
	
	@Override
	public void dropAllItems()
    {
        int i;

        for (i = 0; i < this.extraEquipInventory.length; ++i)
        {
            if (this.extraEquipInventory[i] != null)
            {
                this.player.func_146097_a(this.extraEquipInventory[i], true, false);
                this.extraEquipInventory[i] = null;
            }
        }
        super.dropAllItems();
    }
	
	@Override
	public boolean hasItemStack(ItemStack par1ItemStack)
    {
        int i;

        for (i = 0; i < this.extraEquipInventory.length; ++i)
        {
            if (this.extraEquipInventory[i] != null && this.extraEquipInventory[i].isItemEqual(par1ItemStack))
            {
                return true;
            }
        }
        return super.hasItemStack(par1ItemStack);
    }
	
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        
        ItemStack[] aitemstack = this.mainInventory;

        if (par1 >= this.mainInventory.length + this.extraEquipInventory.length)
        {
            par1 -= this.mainInventory.length + this.extraEquipInventory.length;
            aitemstack = this.armorInventory;
        }
        else if(par1 >= this.mainInventory.length){
        	par1-= aitemstack.length;
        	aitemstack = this.extraEquipInventory;
        }

        aitemstack[par1] = par2ItemStack;
    }
	
	@Override
	public void copyInventory(InventoryPlayer par1InventoryPlayer)
    {
        if(par1InventoryPlayer instanceof InventoryPlayerTFC){
        	this.copyInventoryTFC((InventoryPlayerTFC)par1InventoryPlayer);
        }
        else{
        	super.copyInventory(par1InventoryPlayer);
        }
    }
	
	public void copyInventoryTFC(InventoryPlayerTFC par1InventoryPlayer)
    {
        int i;

        for (i = 0; i < this.extraEquipInventory.length; ++i)
        {
            this.extraEquipInventory[i] = ItemStack.copyItemStack(par1InventoryPlayer.extraEquipInventory[i]);
        }
        super.copyInventory(par1InventoryPlayer);
    }
	
	@Override
	public NBTTagList writeToNBT(NBTTagList par1NBTTagList)
	{
		int i;
		NBTTagCompound nbttagcompound;

		for (i = 0; i < this.mainInventory.length; ++i)
		{
			if (this.mainInventory[i] != null)
			{
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				this.mainInventory[i].writeToNBT(nbttagcompound);
				par1NBTTagList.appendTag(nbttagcompound);
			}
		}
		for (i = 0; i <  this.armorInventory.length; ++i)
		{
			if (this.armorInventory[i] != null)
			{
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) (i + 100));
				this.armorInventory[i].writeToNBT(nbttagcompound);
				par1NBTTagList.appendTag(nbttagcompound);
			}
		}
		for (i = 0; i < this.extraEquipInventory.length; i++){
			if (this.extraEquipInventory[i] != null)
			{
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) (i + 50));
				this.extraEquipInventory[i].writeToNBT(nbttagcompound);
				par1NBTTagList.appendTag(nbttagcompound);
			}
		}
		return par1NBTTagList;
	}
}
