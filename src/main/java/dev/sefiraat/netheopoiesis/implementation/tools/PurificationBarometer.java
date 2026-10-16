package dev.sefiraat.netheopoiesis.implementation.tools;

import dev.sefiraat.netheopoiesis.Purification;
import dev.sefiraat.netheopoiesis.api.interfaces.PurifyingObject;
import dev.sefiraat.netheopoiesis.utils.ItemStackUtils;
import dev.sefiraat.netheopoiesis.utils.Theme;
import dev.sefiraat.netheopoiesis.utils.WorldUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * The PurificationBarometer will display the player's currently located chunks purification level
 *
 * @see PurifyingObject
 * Coolsdown after use.
 */
public class PurificationBarometer extends SimpleSlimefunItem<ItemUseHandler> {

    public PurificationBarometer(ItemGroup itemGroup,
                                 SlimefunItemStack item,
                                 RecipeType recipeType,
                                 ItemStack[] recipe
    ) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return event -> {
            final Player player = event.getPlayer();
            final Chunk chunk = player.getLocation().getChunk();
            final ItemStack barometer = event.getItem();
            if (!WorldUtils.inNether(player.getWorld())) {
                player.sendMessage(Theme.WARNING + "Purification readings are available in the Nether.");
                return;
            }

            if (ItemStackUtils.isOnCooldown(barometer)) {
                player.sendMessage(Theme.WARNING + "Ready in " + ItemStackUtils.getCooldownSeconds(barometer) + "s.");
                return;
            }

            final String message = Theme.CLICK_INFO.asTitle(
                "Chunk Purification Amount",
                Purification.getValue(chunk)
            );
            player.sendMessage(message);
            showNextMilestone(player, Purification.getValue(chunk));
            ItemStackUtils.addCooldown(barometer, 5);
        };
    }

    private void showNextMilestone(Player player, int value) {
        final int[] thresholds = {
            Purification.SLEEP_IN_BED, Purification.PLACE_WATER, Purification.FRIENDLY_PIGLINS,
            Purification.FRIENDLY_HOGLINS, Purification.ENDER_CAKE, Purification.REGEN_3
        };
        final String[] names = {
            "Sleeping in beds", "Water placement", "Friendly piglins",
            "Friendly hoglins", "Ender Cake travel", "Regeneration III"
        };
        for (int i = 0; i < thresholds.length; i++) {
            if (value < thresholds[i]) {
                player.sendMessage(Theme.CLICK_INFO.asTitle("Next utility milestone",
                    names[i] + " at " + thresholds[i] + " (" + (thresholds[i] - value) + " more needed)"));
                return;
            }
        }
        player.sendMessage(Theme.CLICK_INFO + "All utility milestones reached in this chunk.");
    }
}
