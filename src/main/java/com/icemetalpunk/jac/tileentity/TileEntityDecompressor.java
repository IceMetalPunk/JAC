package com.icemetalpunk.jac.tileentity;

import java.util.Arrays;
import java.util.List;

import com.icemetalpunk.jac.blocks.BlockDecompressor;
import com.icemetalpunk.jac.container.ContainerDecompressor;
import com.icemetalpunk.jac.util.CompressionLookup;
import com.icemetalpunk.jac.util.CompressionLookup.MultiItem;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDecompressor extends TileEntityLockable implements ITickable, ISidedInventory {
	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1 };

	private NonNullList<ItemStack> itemStacks = NonNullList.<ItemStack> withSize(3, ItemStack.EMPTY);
	private int operationTimeLeft;
	private int currentOperationTime;
	private String customName;
	public static List<Item> compressionFuels = Arrays.asList(new Item[] { Items.GUNPOWDER });

	public int getSizeInventory() {
		return this.itemStacks.size();
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : this.itemStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public ItemStack getStackInSlot(int index) {
		return this.itemStacks.get(index);
	}

	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.itemStacks, index, count);
	}

	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.itemStacks, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.itemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.itemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.currentOperationTime = this.getOperationTime(stack);
			if (!this.itemStacks.get(1).isEmpty()) {
				this.operationTimeLeft = this.currentOperationTime;
			}
			this.markDirty();
		} else if (index == 1 && !flag) {
			this.operationTimeLeft = this.currentOperationTime;
			this.markDirty();
		}
	}

	public String getName() {
		return this.hasCustomName() ? this.customName : "container.jac.decompressor";
	}

	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.itemStacks = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.itemStacks);
		this.operationTimeLeft = compound.getInteger("TimeLeft");
		this.currentOperationTime = getOperationTime(this.itemStacks.get(0));

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("TimeLeft", (short) this.operationTimeLeft);
		ItemStackHelper.saveAllItems(compound, this.itemStacks);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		}

		return compound;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isActive() {
		return this.operationTimeLeft > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isActive(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	public void update() {
		ItemStack processingItem = this.itemStacks.get(0);
		if (processingItem.isEmpty()) {
			this.currentOperationTime = 0;
			this.operationTimeLeft = 0;
		} else if (this.itemStacks.get(1).isEmpty()) {
			this.operationTimeLeft = 0;
		}

		if (this.operationTimeLeft > 0) {
			if (--this.operationTimeLeft == 0) {
				decompressItem();
				this.itemStacks.get(1).shrink(1);
				this.operationTimeLeft = this.currentOperationTime;
			}
		}

		IBlockState currentState = this.world.getBlockState(this.pos);
		boolean active = currentState.getValue(BlockDecompressor.ACTIVE);
		if (active && this.operationTimeLeft <= 0) {
			BlockDecompressor.setState(false, this.world, this.pos);
		} else if (!active && this.operationTimeLeft > 0) {
			BlockDecompressor.setState(true, this.world, this.pos);
		}

	}

	public int getOperationTime(ItemStack stack) {
		// TODO: Calculate operation times.
		return 100;
	}

	private boolean canDecompress() {
		if (this.itemStacks.get(0).isEmpty()) {
			return false;
		} else {
			MultiItem multi = new MultiItem(this.itemStacks.get(0));
			ItemStack itemstack = CompressionLookup.decompressionRecipes.getOrDefault(multi, ItemStack.EMPTY);

			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = this.itemStacks.get(2);

				if (itemstack1.isEmpty()) {
					return true;
				} else if (!itemstack1.isItemEqual(itemstack)) {
					return false;
				} else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit()
						&& itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
					return true;
				} else {
					return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
				}
			}
		}
	}

	public void decompressItem() {
		if (this.canDecompress()) {
			ItemStack itemstack = this.itemStacks.get(0);
			MultiItem multi = new MultiItem(itemstack);
			ItemStack itemstack1 = CompressionLookup.decompressionRecipes.getOrDefault(multi, ItemStack.EMPTY);
			ItemStack itemstack2 = this.itemStacks.get(2);

			this.world.playSound((EntityPlayer) null, this.pos.getX(), this.pos.getY(), this.pos.getZ(),
					SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.05F, 1.0f);

			if (itemstack2.isEmpty()) {
				this.itemStacks.set(2, itemstack1.copy());
			} else if (itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}

			itemstack.shrink(1);
		}
	}

	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
			if (burnTime >= 0)
				return burnTime;
			Item item = stack.getItem();

			if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) {
				return 150;
			} else if (item == Item.getItemFromBlock(Blocks.WOOL)) {
				return 100;
			} else if (item == Item.getItemFromBlock(Blocks.CARPET)) {
				return 67;
			} else if (item == Item.getItemFromBlock(Blocks.LADDER)) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) {
				return 100;
			} else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
				return 16000;
			} else if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) {
				return 200;
			} else if (item == Items.STICK) {
				return 100;
			} else if (item != Items.BOW && item != Items.FISHING_ROD) {
				if (item == Items.SIGN) {
					return 200;
				} else if (item == Items.COAL) {
					return 1600;
				} else if (item == Items.LAVA_BUCKET) {
					return 20000;
				} else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL) {
					if (item == Items.BLAZE_ROD) {
						return 2400;
					} else if (item instanceof ItemDoor && item != Items.IRON_DOOR) {
						return 200;
					} else {
						return item instanceof ItemBoat ? 400 : 0;
					}
				} else {
					return 100;
				}
			} else {
				return 300;
			}
		}
	}

	public static boolean isItemFuel(ItemStack stack) {
		return compressionFuels.contains(stack.getItem());
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with
	 * Container
	 */
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	public void openInventory(EntityPlayer player) {
	}

	public void closeInventory(EntityPlayer player) {
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 1) {
			return true;
		} else {
			ItemStack itemstack = this.itemStacks.get(1);
			return isItemFuel(stack);
		}
	}

	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN) {
			return SLOTS_BOTTOM;
		} else {
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}

	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot
	 * from the given side.
	 */
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index != 1;
	}

	public String getGuiID() {
		return "jacmod:decompressor";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerDecompressor(playerInventory, this);
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return this.operationTimeLeft;
		case 1:
			return this.currentOperationTime;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.operationTimeLeft = value;
			break;
		case 1:
			this.currentOperationTime = value;
			break;
		}
	}

	public int getFieldCount() {
		return 2;
	}

	public void clear() {
		this.itemStacks.clear();
	}

	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this,
			net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this,
			net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this,
			net.minecraft.util.EnumFacing.WEST);

	@SuppressWarnings("unchecked")
	@Override
	@javax.annotation.Nullable
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability,
			@javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			if (facing == EnumFacing.DOWN)
				return (T) handlerBottom;
			else if (facing == EnumFacing.UP)
				return (T) handlerTop;
			else
				return (T) handlerSide;
		return super.getCapability(capability, facing);
	}
}