package com.bioxx.tfc.Blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Core.CollisionRayTraceStandard;
import com.bioxx.tfc.TileEntities.TileEntityWoodConstruct;
import com.bioxx.tfc.api.Interfaces.ICustomCollision;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWoodConstruct extends BlockTerraContainer implements ICustomCollision
{
	public BlockWoodConstruct()
	{
		super(Material.wood);
		setBlockBounds(0.0F, 0.0F, 0.0f, 0.0f, 0.0F, 0.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new TileEntityWoodConstruct();
	}

	@Override
	public int getRenderType()
	{
		return TFCBlocks.woodConstructRenderId;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int i, int j, int k)
	{
		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegisterer)
	{
		//Empty On Purpose
	}

	@Override
	public IIcon getIcon(int i, int j)
	{
		return TFCBlocks.Planks.getIcon(i, j);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public void Eject(World world, int x, int y, int z)
	{
		if(!world.isRemote && (TileEntityWoodConstruct)world.getTileEntity(x, y, z)!=null)
		{
			TileEntityWoodConstruct te = (TileEntityWoodConstruct)world.getTileEntity(x, y, z);
			te.ejectContents();
			world.setBlockToAir(x, y, z);
		}
	}
	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
	{
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
	{
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5)
	{
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		Eject(world,x,y,z);
		super.breakBlock(world, x, y, z, block, metadata);
	}

	@Override
	public boolean canDropFromExplosion(Explosion ex)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
	}

	@Override
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB aabb, List list, Entity entity)
	{
		ArrayList<Object[]> alist = new ArrayList<Object[]>();
		addCollisionBoxesToList(world, i, j, k, alist);
		for(Object[] obj : alist)
		{
			AxisAlignedBB plankAABB = (AxisAlignedBB)obj[0];
			plankAABB.minX += i; plankAABB.maxX += i;
			plankAABB.minY += j; plankAABB.maxY += j;
			plankAABB.minZ += k; plankAABB.maxZ += k;
			if (plankAABB != null && aabb.intersectsWith(plankAABB))
			{
				list.add(plankAABB);
			}
		}
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 player, Vec3 view)
	{
		return CollisionRayTraceStandard.collisionRayTrace(this, world, x, y, z, player, view);
	}

	@Override
	public void addCollisionBoxesToList(World world, int i, int j, int k, List list) 
	{
		TileEntityWoodConstruct te = (TileEntityWoodConstruct) world.getTileEntity(i, j, k);

		int d = TileEntityWoodConstruct.PlankDetailLevel;
		int dd = TileEntityWoodConstruct.PlankDetailLevel * TileEntityWoodConstruct.PlankDetailLevel;

		float div = 1f / d;

		for(int x = 0; x < dd; x++)
		{
			if(te.data.get(x))
			{
				float minX = 0;
				float maxX = 1;
				float minY = div * (x & 7);
				float maxY = minY + div;
				float minZ = div * (x >> 3);
				float maxZ = minZ + div;
				list.add(new Object[]{AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)});
			}
		}

		for(int y = 0; y < dd; y++)
		{
			if(te.data.get(y+dd))
			{
				float minX = div * (y & 7);
				float maxX = minX + div;
				float minY = 0;
				float maxY = 1;
				float minZ = div * (y >> 3);
				float maxZ = minZ + div;
				list.add(new Object[]{AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)});
			}
		}

		for(int z = 0; z < dd; z++)
		{
			if(te.data.get(z+(dd*2)))
			{
				float minX = div * (z & 7);
				float maxX = minX + div;
				float minY = div * (z >> 3);
				float maxY = minY + div;
				float minZ = 0;
				float maxZ = 1;
				list.add(new Object[]{AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)});
			}
		}
	}

}
