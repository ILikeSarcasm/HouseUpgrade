package me.ilikesarcasm.houseupgrade.houses;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import me.wiefferink.areashop.regions.GeneralRegion;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class House {

    public class Dimensions {
        private final int x;
        private final int y;
        private final int z;

        public Dimensions(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Dimensions(String stringifiedDimensions) {
            Integer[] dimensions = (Integer[])(Arrays.asList(stringifiedDimensions.split("x")).stream().map(dimension -> 
                Integer.parseInt(dimension)
            ).toArray());

            this.x = dimensions[0];
            this.y = dimensions[1];
            this.z = dimensions[2];
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getZ() {
            return this.z;
        }

        public String toString() {
            return this.x + "x" + this.y + "x" + this.z;
        }
    }

    private static final HashMap<String, String> CONFIG_PARAMS = new HashMap<String, String>() {{
        put("type", "general.type");
        put("region", "general.region");
        put("level", "level");
        put("structureFileName", "structure.file.name");
    }};

    private static Map<String, Integer> allMaxLevels;
    private static Map<String, List<Integer>> allUpgradeCosts;

    private HouseUpgrade plugin;

    private String name;
    private String type;
    private File structureFile;
    private int level;
    private GeneralRegion region;
    private UUID owner;
    private Dimensions dimensions;
    private Location location;

    public static void setAllMaxLevels(Map<String, Integer> allMaxLevels) {
        House.allMaxLevels = allMaxLevels;
    }

    public static void setAllUpgradeCosts(Map<String, List<Integer>> allUpgradeCosts) {
        House.allUpgradeCosts = allUpgradeCosts;
    }

    public House(String type, GeneralRegion region) {
        this.plugin = HouseUpgrade.getInstance();

        this.name = region.getName();
        this.type = type;
        this.structureFile = this.computeStructureFile();
        this.level = 0;
        this.region = region;
        this.dimensions = new Dimensions(region.getWidth(), region.getHeight(), region.getDepth());
        this.location = new Location(region.getWorld(), region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ());
    }

    public House(YamlConfiguration houseConfig) {
        this.plugin = HouseUpgrade.getInstance();
        
        this.region = plugin.getAreaShopHandler().getRegion(houseConfig.getString(House.CONFIG_PARAMS.get("region")));
        this.name = this.region.getName();
        this.type = houseConfig.getString(House.CONFIG_PARAMS.get("type"));
        this.structureFile = new File(houseConfig.getString(House.CONFIG_PARAMS.get("structureFileName")));
        this.level = houseConfig.getInt(House.CONFIG_PARAMS.get("level"));
        this.owner = this.region.getOwner();
        this.dimensions = new Dimensions(this.region.getWidth(), this.region.getHeight(), this.region.getDepth());
        this.location = new Location(this.region.getWorld(), this.region.getMinimumPoint().getBlockX(), this.region.getMinimumPoint().getBlockY(), this.region.getMinimumPoint().getBlockZ());
    }

    public YamlConfiguration getConfig() {
        YamlConfiguration houseConfig = new YamlConfiguration();

        houseConfig.set(House.CONFIG_PARAMS.get("type"), this.getType());
        houseConfig.set(House.CONFIG_PARAMS.get("region"), this.getRegion().getName());
        houseConfig.set(House.CONFIG_PARAMS.get("level"), this.getLevel());
       
        return houseConfig;
    }

    public String getFullName() {
        return this.type + "." + this.dimensions.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public GeneralRegion getRegion() {
        return this.region;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public Dimensions getDimensions() {
        return this.dimensions;
    }

    public Location getLocation() {
        return this.location;
    }

    public File getFile() {
        return new File(plugin.getFileManager().get("houseFolder").getAbsolutePath() + File.separator + this.name + ".yml");
    }

    public File getStructureFile() {
        return this.structureFile;
    }

    public File getStructureFolder() {
        return this.structureFile.getParentFile();
        // return new File(plugin.getFileManager().get("schematicFolder") + File.separator + this.dimensions.toString() + File.separator + this.level + File.separator + this.type);
    }

    public boolean isMaxedOut() {
        return this.level >= House.allMaxLevels.get(this.getFullName());
    }

    public File computeStructureFile() {
        if (this.isMaxedOut()) {
            return null;
       }

       File[] availableStructureFiles = this.getStructureFolder().listFiles();
       int randomIndex = new Random().nextInt(availableStructureFiles.length);

       return availableStructureFiles[randomIndex];
    }

    public int getUpgradeCost(int level) {
        return House.allUpgradeCosts.get(this.getFullName()).get(level);
    }

    public String getConfigPath() {
        return "houses." + this.type + "." + this.dimensions.toString() +  "." + this.name;
    }

    public String getConfigPath(int level) {
        return this.getConfigPath() + "." + level;
    }

    public House upgrade() {
        this.level++;
        this.structureFile = this.computeStructureFile();

        return this;
    }

    public House save() {
        try {
            this.getConfig().save(this.getFile());
        } catch (IOException e) {
            HouseUpgrade.error("Couldn't save house '" + this.name + "' to " + this.getFile().getAbsolutePath() + "\n" + e);
        }

        return this;
    }

}
