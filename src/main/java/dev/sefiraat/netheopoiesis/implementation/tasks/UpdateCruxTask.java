package dev.sefiraat.netheopoiesis.implementation.tasks;

import dev.sefiraat.netheopoiesis.utils.Keys;
import dev.sefiraat.netheopoiesis.Netheopoiesis;
import dev.sefiraat.netheopoiesis.Purification;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * When trying to swap out a Slimefun Block in BlockStorage, the deletion will happen after a delay removing
 * the newly stored data. Not removing the old data will cause a duplicate block in storage reverting
 * to its previous state after a restart. This task will ensure the new block data is added only after the
 * removed block has left the deletion queue.
 */
public class UpdateCruxTask extends BukkitRunnable {

    private static final Set<Location> PENDING = new HashSet<>();

    @Nonnull
    private final Block block;
    @Nonnull
    private final SlimefunItemStack crux;
    private final int steps;
    private final BlockData originalData;

    public UpdateCruxTask(@Nonnull Block block, @Nonnull SlimefunItemStack crux) {
        this(block, crux, -1);
    }

    public UpdateCruxTask(@Nonnull Block block, @Nonnull SlimefunItemStack crux, int spreadStepsRemaining) {
        this.block = block;
        this.crux = crux;
        this.steps = spreadStepsRemaining;
        this.originalData = block.getBlockData().clone();
    }

    public static boolean schedule(@Nonnull Block block, @Nonnull SlimefunItemStack crux, int steps) {
        if (!PENDING.add(block.getLocation())) {
            return false;
        }
        try {
            UpdateCruxTask task = new UpdateCruxTask(block, crux, steps);
            BlockStorage.clearBlockInfo(block);
            Purification.removeValue(block);
            task.runTaskTimer(Netheopoiesis.getInstance(), 1, 20);
            return true;
        } catch (RuntimeException e) {
            PENDING.remove(block.getLocation());
            throw e;
        }
    }

    @Override
    public void run() {
        if (!block.getWorld().isChunkLoaded(block.getX() >> 4, block.getZ() >> 4)) {
            finish();
            return;
        }
        if (!originalData.equals(block.getBlockData())) {
            finish();
            return;
        }
        if (Slimefun.getTickerTask().isDeletedSoon(block.getLocation())) {
            // BlockStorage still has not been cleared
            return;
        }
        if (BlockStorage.check(block) != null) {
            finish();
            return;
        }
        // Store the new data then cancel task
        block.setType(crux.getType());
        BlockStorage.store(block, crux.getItemId());
        if (steps > -1) {
            BlockStorage.addBlockInfo(block, Keys.CRYSTALLINE_STEPS_REMAINING, String.valueOf(this.steps));
        }
        finish();
    }

    private void finish() {
        PENDING.remove(block.getLocation());
        cancel();
    }
}
