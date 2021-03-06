package com.bioxx.tfc.GUI;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Containers.ContainerFirepit;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Core.Player.PlayerInventory;
import com.bioxx.tfc.TileEntities.TEFirepit;


public class GuiFirepit extends GuiContainer
{
	private TEFirepit FirepitEntity;


	public GuiFirepit(InventoryPlayer inventoryplayer, TEFirepit tileentityfirepit, World world, int x, int y, int z)
	{
		super(new ContainerFirepit(inventoryplayer,tileentityfirepit, world, x, y, z) );
		FirepitEntity = tileentityfirepit;
		xSize = 176;
		ySize = 85+PlayerInventory.invYSize;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		TFC_Core.bindTexture(new ResourceLocation(Reference.ModID, Reference.AssetPathGui + "gui_firepit.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		int w = (width - xSize) / 2;
		int h = (height - ySize) / 2;
		drawTexturedModalRect(w, h, 0, 0, xSize, ySize);
		int i1 = FirepitEntity.getTemperatureScaled(49);
		drawTexturedModalRect(w + 30, h + 65 - i1, 185, 31, 15, 6);
		
		PlayerInventory.drawInventory(this, width, height, ySize-PlayerInventory.invYSize);
	}

	protected void drawGuiContainerForegroundLayer()
	{
		
	}

	@Override
	public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k)
	{
		fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
	}


}
