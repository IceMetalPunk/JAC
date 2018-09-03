package com.icemetalpunk.jac.util;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

public class CompressionLookup {
	public static HashMap<MultiItem, ItemStack> decompressionRecipes = new HashMap<>();
	public static HashMap<ItemStack, ItemStack> compressionRecipes = new HashMap<>();

	public static void buildLookup() {
		CraftingManager.REGISTRY.forEach(new Consumer<IRecipe>() {
			@Override
			public void accept(IRecipe recipe) {
				List<Ingredient> ingredients = recipe.getIngredients();
				if (ingredients.size() == 1) {
					for (ItemStack stack : ingredients.get(0).getMatchingStacks()) {
						ItemStack result = recipe.getRecipeOutput();
						if (stack.getCount() == 1 && result.getCount() > 1) {
							decompressionRecipes.put(new MultiItem(stack), result);
						}
					}
				}
			}
		});
	}

	public static class MultiItem {
		private Item item;
		private int meta;

		public MultiItem(Item item, int meta) {
			this.item = item;
			this.meta = meta;
		}

		public MultiItem(ItemStack stack) {
			this(stack.getItem(), stack.getItemDamage());
		}

		@Override
		public boolean equals(Object compare) {
			if (compare == this) {
				return true;
			} else if (compare instanceof MultiItem) {
				MultiItem compareItem = (MultiItem) compare;
				return (compareItem.item == this.item && compareItem.meta == this.meta);
			} else if (compare instanceof ItemStack) {
				ItemStack stack = (ItemStack) compare;
				return (this.item == stack.getItem() && this.meta == stack.getItemDamage());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.item, this.meta);
		}
	}

}
