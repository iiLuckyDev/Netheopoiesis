package dev.sefiraat.netheopoiesis.implementation;

import com.google.common.base.Preconditions;
import dev.sefiraat.netheopoiesis.api.plant.netheos.NetheoBalls;
import dev.sefiraat.netheopoiesis.utils.Keys;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Registers a progression-focused research ladder for Netheopoiesis.
 * Each item gets its own research while still inheriting the tier's cost.
 */
public final class Researches {

    private static final int STARTING_ID = 51000;

    private Researches() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup() {
        int nextId = STARTING_ID;

        nextId = registerTier(
            nextId,
            "nether_survival",
            8,
            true,
            Stacks.ADDON_BERRY,
            Stacks.ADDON_JAM,
            Stacks.CRUDE_HARVESTING_TOOL,
            Stacks.HARVESTING_TOOL,
            Stacks.PURIFICATION_BAROMETER,
            Stacks.BASIC_PURIFIED_NETHERRACK,
            Stacks.PURIFICATION_SEED
        );

        nextId = registerTier(
            nextId,
            "purification",
            16,
            true,
            Stacks.PURIFIED_NETHERRACK,
            Stacks.VORACIOUS_DIRT,
            Stacks.SOUL_SEED,
            Stacks.SPIRIT_SEED
        );

        nextId = registerTier(
            nextId,
            "sanctification",
            28,
            true,
            Stacks.NETHER_DIRT,
            Stacks.NETHER_GRASS,
            Stacks.SAINTLY_SEED,
            Stacks.EDEN_SEED
        );

        nextId = registerTier(
            nextId,
            "cultivation",
            42,
            true,
            Stacks.SEED_ANALYSER,
            Stacks.SPINDLE_SEED,
            Stacks.GRAINY_SEED,
            Stacks.STRINGY_SEED,
            Stacks.GRASS_SEED,
            Stacks.COBBLED_SEED,
            Stacks.VOLCANIC_SEED,
            Stacks.IGNEOUS_SEED,
            Stacks.FELDSPAR_SEED,
            Stacks.DEEPSLATE_SEED,
            Stacks.DUSTY_SEED,
            Stacks.SEASIDE_SEED,
            Stacks.NORI_SEED,
            Stacks.MOLDABLE_SEED,
            Stacks.WET_SEED,
            Stacks.SPLINTERED_SEED,
            Stacks.ROTTEN_SEED,
            Stacks.METALLIC_SEED,
            Stacks.TARNISHED_SEED,
            Stacks.SHINY_SEED,
            Stacks.SMOOTH_SEED,
            Stacks.SEEDY_SEED,
            Stacks.SWEET_SEED,
            Stacks.ENCHANTED_SEED,
            Stacks.COMBUSTIBLE_SEED,
            Stacks.PROTECTIVE_SEED,
            Stacks.PORKY_SEED,
            Stacks.VALUABLE_SEED,
            Stacks.PERFECTION_SEED
        );

        nextId = registerTier(
            nextId,
            "advanced_cultivation",
            60,
            true,
            Stacks.RAINBOW_SEED,
            Stacks.GLOWING_SEED,
            Stacks.GLISTENING_SEED,
            Stacks.ETHEREAL_SEED,
            Stacks.IGNITED_SEED,
            Stacks.BARTERED_SEED,
            Stacks.PRISMATIC_SEED,
            Stacks.POROUS_SEED,
            Stacks.LEARNED_SEED,
            Stacks.BUSY_SEED,
            Stacks.OAKENDRAN_SEED
        );

        nextId = registerTier(
            nextId,
            "biome_genesis",
            82,
            true,
            Stacks.JUNGLE_CRUX,
            Stacks.BEACH_CRUX,
            Stacks.DESERT_CRUX,
            Stacks.SNOW_CRUX,
            Stacks.STONEY_CRUX,
            Stacks.SWAMP_CRUX,
            Stacks.JUNGLE_SEED,
            Stacks.BEACH_SEED,
            Stacks.DESERT_SEED,
            Stacks.SNOW_SEED,
            Stacks.STONEY_SEED,
            Stacks.SWAMP_SEED,
            Stacks.BLACK_AND_WHITE_SEED,
            Stacks.PARROT_SEED,
            Stacks.WILD_SEED,
            Stacks.SHELLED_SEED,
            Stacks.TREASURED_SEED,
            Stacks.SPINEY_SEED,
            Stacks.HUSKY_SEED,
            Stacks.STRAY_SEED,
            Stacks.POLAR_SEED,
            Stacks.CHILLY_SEED,
            Stacks.HEXED_SEED,
            Stacks.SLIMY_SEED,
            Stacks.BLOB_SEED
        );

        final List<SlimefunItemStack> piglinTradecraft = new ArrayList<>();
        piglinTradecraft.add(Stacks.MIXING_QUARTZ);
        piglinTradecraft.add(Stacks.QUARTZ_BLANK);
        piglinTradecraft.add(Stacks.HARMONISED_CRYSTAL_FIRE);
        piglinTradecraft.add(Stacks.HARMONISED_CRYSTAL_WATER);
        piglinTradecraft.add(Stacks.HARMONISED_CRYSTAL_EARTH);
        piglinTradecraft.add(Stacks.HARMONISED_CRYSTAL_AIR);
        piglinTradecraft.add(Stacks.DISCORDANT_CRYSTAL_FIRE);
        piglinTradecraft.add(Stacks.DISCORDANT_CRYSTAL_WATER);
        piglinTradecraft.add(Stacks.DISCORDANT_CRYSTAL_EARTH);
        piglinTradecraft.add(Stacks.DISCORDANT_CRYSTAL_AIR);
        piglinTradecraft.add(Stacks.DISSONANT_CRYSTAL_FIRE);
        piglinTradecraft.add(Stacks.DISSONANT_CRYSTAL_WATER);
        piglinTradecraft.add(Stacks.DISSONANT_CRYSTAL_EARTH);
        piglinTradecraft.add(Stacks.DISSONANT_CRYSTAL_AIR);
        piglinTradecraft.add(Stacks.ELEMENTAL_CRYSTAL);
        piglinTradecraft.add(Stacks.VACUUM_CRYSTAL);
        piglinTradecraft.add(Stacks.IRREGULAR_CRYSTAL);
        NetheoBalls.getCachedValues()
                  .stream()
                  .sorted(Comparator.comparing(NetheoBalls::getName))
                  .map(NetheoBalls::getSlimefunItemStack)
                  .forEach(piglinTradecraft::add);
        nextId = registerTier(
            nextId,
            "piglin_tradecraft",
            108,
            false,
            piglinTradecraft.toArray(SlimefunItemStack[]::new)
        );

        nextId = registerTier(
            nextId,
            "nether_engineering",
            136,
            false,
            Stacks.PURIFICATION_SCANNER,
            Stacks.CRUX_GATHERER,
            Stacks.NETHER_BEACON,
            Stacks.NETHER_BEACON_BASE_1,
            Stacks.NETHER_BEACON_BASE_2,
            Stacks.NETHER_BEACON_BASE_3,
            Stacks.SIPHON_ANALYSER
        );

        registerTier(
            nextId,
            "transcendence",
            168,
            true,
            Stacks.ENDER_CAKE,
            Stacks.CRYSTALLINE_CRUX,
            Stacks.ADDON_BERRY_SEED,
            Stacks.CUTE_SEED,
            Stacks.BEST_FRIEND_SEED,
            Stacks.MATH_SEED,
            Stacks.BUZZING_SEED,
            Stacks.TERRIFYING_SEED,
            Stacks.HATE_FILLED_SEED,
            Stacks.PULSING_SEED,
            Stacks.GATEWAY_SEED,
            Stacks.CRYSTALLINE_SEED
        );
    }

    private static int registerTier(int startingId,
                                    @Nonnull String keyPrefix,
                                    int cost,
                                    boolean includePastes,
                                    @Nonnull SlimefunItemStack... items
    ) {
        final Set<String> uniqueItemIds = new LinkedHashSet<>();
        for (SlimefunItemStack stack : items) {
            uniqueItemIds.add(stack.getItemId());
        }

        int nextId = startingId;
        for (String itemId : uniqueItemIds) {
            registerResearch(nextId++, keyPrefix, cost, includePastes, itemId);
        }

        return nextId;
    }

    @Nonnull
    private static Research registerResearch(int id,
                                             @Nonnull String keyPrefix,
                                             int cost,
                                             boolean includePastes,
                                             @Nonnull String itemId
    ) {
        final SlimefunItem item = requireItem(itemId);
        final Research research = new Research(
            Keys.newKey(keyPrefix + "_" + sanitize(itemId)),
            id,
            getResearchName(item),
            cost
        );

        bindItem(research, itemId, includePastes);
        research.register();
        return research;
    }

    private static void bindItem(@Nonnull Research research, @Nonnull String itemId, boolean includePaste) {
        final SlimefunItem item = requireItem(itemId);
        item.setResearch(research);
        research.addItems(item);

        if (includePaste) {
            final Optional<SlimefunItem> paste = SlimefunItem.getOptionalById(toPasteId(itemId));
            paste.ifPresent(slimefunItem -> {
                slimefunItem.setResearch(research);
                research.addItems(slimefunItem);
            });
        }
    }

    @Nonnull
    private static SlimefunItem requireItem(@Nonnull String itemId) {
        final SlimefunItem item = SlimefunItem.getById(itemId);
        Preconditions.checkNotNull(item, "Missing research item: %s", itemId);
        return item;
    }

    @Nonnull
    private static String toPasteId(@Nonnull String itemId) {
        return "NPS_PASTE_" + itemId.replace("NPS_", "");
    }

    @Nonnull
    private static String getResearchName(@Nonnull SlimefunItem item) {
        final String strippedName = ChatColor.stripColor(item.getItemName());

        if (strippedName == null || strippedName.isBlank() || "Error".equalsIgnoreCase(strippedName)) {
            return prettify(item.getId());
        }

        return strippedName.trim();
    }

    @Nonnull
    private static String sanitize(@Nonnull String itemId) {
        String key = itemId.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9._-]", "_");

        if (key.length() > 80) {
            key = key.substring(0, 56) + "_" + Integer.toHexString(itemId.hashCode());
        }

        return key;
    }

    @Nonnull
    private static String prettify(@Nonnull String itemId) {
        final String[] parts = itemId.toLowerCase(Locale.ROOT).split("_");
        final StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }

            if (!builder.isEmpty()) {
                builder.append(' ');
            }

            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }

        return builder.toString();
    }
}
