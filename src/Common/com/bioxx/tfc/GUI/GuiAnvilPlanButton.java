package com.bioxx.tfc.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.bioxx.tfc.TFCItems;
import com.bioxx.tfc.Core.TFC_Core;

public class GuiAnvilPlanButton extends GuiButton 
{
	GuiAnvil screen;
	protected static final RenderItem itemRenderer = new RenderItem();

	public GuiAnvilPlanButton(int index, int xPos, int yPos, int width, int height, GuiAnvil gui, String s)
	{
		super(index, xPos, yPos, width, height, s);
		screen = gui;
	}

	@Override
	public void drawButton(Minecraft mc, int x, int y)
	{
		if (this.visible)
		{
			int k = this.getHoverState(this.field_146123_n)-1;

			TFC_Core.bindTexture(GuiAnvil.texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.zLevel = 300f;
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 205 + k*18, 18, 18);
			this.field_146123_n = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix(); //start
			TFC_Core.bindTexture(TextureMap.locationItemsTexture);

			if(!screen.AnvilEntity.craftingPlan.equals("") && screen.AnvilEntity.workRecipe != null) 
				renderInventorySlot(screen.AnvilEntity.workRecipe.getCraftingResult(),this.xPosition+1, this.yPosition+1);
			//this.drawTexturedModelRectFromIcon(this.xPosition+1, this.yPosition+1, screen.AnvilEntity.workRecipe.getCraftingResult().getIconIndex(), this.width-2, this.height-2);
			else
				this.drawTexturedModelRectFromIcon(this.xPosition+1, this.yPosition+1, TFCItems.Blueprint.getIconIndex(new ItemStack(TFCItems.Blueprint)), this.width-2, this.height-2);

			this.zLevel = 0;
			this.mouseDragged(mc, x, y);
			GL11.glPopMatrix(); //end

			if(field_146123_n)
			{
				FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
				screen.drawTooltip(x, y, this.displayString);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	protected void renderInventorySlot(ItemStack par1, int par2, int par3)
	{
		if (par1 != null)
		{
			itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), par1, par2, par3);
		}
	}

	private boolean isPointInRegion(int mouseX, int mouseY)
	{
		int k1 = 0;//screen.getGuiLeft();
		int l1 = 0;//screen.getGuiTop();
		mouseX -= k1;
		mouseY -= l1;
		return mouseX >= xPosition - 1 && mouseX < xPosition + width + 1 && mouseY >= yPosition - 1 && mouseY < yPosition + height + 1;
	}
}
