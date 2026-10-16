package dev.sefiraat.netheopoiesis.managers;

import dev.sefiraat.netheopoiesis.Netheopoiesis;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used to create and manage/save custom configuration files
 */
public class ConfigManager {

    // Player breeding discoveries
    private final FileConfiguration discoveries;

    public ConfigManager() {
        setupDefaultConfig();
        this.discoveries = getConfig("discoveries.yml");
    }

    private void setupDefaultConfig() {
        final Netheopoiesis plugin = Netheopoiesis.getInstance();
        final InputStream inputStream = plugin.getResource("config.yml");
        final File existingFile = new File(plugin.getDataFolder(), "config.yml");

        if (inputStream == null) {
            // Not sure how? Regardless cannot copy over new keys
            return;
        }

        final Reader reader = new InputStreamReader(inputStream);
        final FileConfiguration resourceConfig = YamlConfiguration.loadConfiguration(reader);

        if (!existingFile.exists()) {
            plugin.saveResource("config.yml", false);
            return;
        }

        final FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(existingFile);

        int resourceVersion = resourceConfig.getInt("config-version", 1);
        int existingVersion = existingConfig.getInt("config-version", 1);

        boolean changed = existingVersion != resourceVersion;
        for (String key : resourceConfig.getKeys(false)) {
            changed |= checkKey(existingConfig, resourceConfig, key);
        }
        if (!changed) {
            plugin.reloadConfig();
            return;
        }
        try {
            backupConfig(plugin, existingFile, existingVersion, resourceVersion);
            existingConfig.set("config-version", resourceVersion);
            existingConfig.options().header("Netheopoiesis config-version: " + resourceVersion);
            existingConfig.save(existingFile);
            plugin.reloadConfig();
        } catch (IOException e) {
            throw new IllegalStateException("Could not back up and update config.yml", e);
        }
    }

    @ParametersAreNonnullByDefault
    private boolean checkKey(FileConfiguration existingConfig, FileConfiguration resourceConfig, String key) {
        final Object currentValue = existingConfig.get(key);
        final Object newValue = resourceConfig.get(key);
        if (newValue instanceof ConfigurationSection section) {
            if (currentValue != null && !(currentValue instanceof ConfigurationSection)) {
                return false;
            }
            boolean changed = false;
            for (String sectionKey : section.getKeys(false)) {
                changed |= checkKey(existingConfig, resourceConfig, key + "." + sectionKey);
            }
            return changed;
        } else if (currentValue == null) {
            existingConfig.set(key, newValue);
            return true;
        }
        return false;
    }

    @Nonnull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private FileConfiguration getConfig(@Nonnull String fileName) {
        final Netheopoiesis plugin = Netheopoiesis.getInstance();
        final File file = new File(plugin.getDataFolder(), fileName);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveAll() {
        Netheopoiesis.getInstance().getLogger().info("Netheopoiesis saving data.");
        saveDiscoveries();
    }

    private void saveDiscoveries() {
        File file = new File(Netheopoiesis.getInstance().getDataFolder(), "discoveries.yml");
        try {
            discoveries.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public FileConfiguration getDiscoveries() {
        return discoveries;
    }

    public boolean isAutoUpdate() {
        return Netheopoiesis.getInstance().getConfig().getBoolean("auto-update");
    }

    public boolean isDebugMessages() {
        return Netheopoiesis.getInstance().getConfig().getBoolean("debug-messages");
    }

    public boolean areResearchesEnabled() {
        return Netheopoiesis.getInstance().getConfig().getBoolean("options.addon-researches", false);
    }

    public int getPlayerMobCapWaterAmbient() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-water-ambient");
    }

    public int getPlayerMobCapWaterAnimal() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-water-animal");
    }

    public int getPlayerMobCapWaterHostile() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-water-hostile");
    }

    public int getPlayerMobCapLandAmbient() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-land-ambient");
    }

    public int getPlayerMobCapLandAnimal() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-land-animal");
    }

    public int getPlayerMobCapLandHostile() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-land-hostile");
    }

    public int getPlayerMobCapVillager() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-villager");
    }

    public int getPlayerMobCapPiglinTrader() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-piglin-trader");
    }

    public int getPlayerMobCapWanderingTrader() {
        return Netheopoiesis.getInstance().getConfig().getInt("spawning.player-cap-wandering-trader");
    }

    private void backupConfig(Netheopoiesis plugin,
                                          File existingFile,
                                          int existingVersion,
                                          int resourceVersion
    ) throws IOException {
            File backupDir = new File(plugin.getDataFolder(), "config-backups");
            if (!backupDir.exists()) {
                Files.createDirectories(backupDir.toPath());
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS"));
            String backupName = "config-v" + existingVersion + "-to-v" + resourceVersion + "-" + timestamp + ".yml";
            File backupFile = new File(backupDir, backupName);
            Files.copy(existingFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
