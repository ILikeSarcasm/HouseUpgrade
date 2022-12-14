package me.ilikesarcasm.houseupgrade.houses;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.ipvp.canvas.Menu.Dimension;

public class HouseManager {

    private static HouseManager instance;

    private List<String> houseTypes;
    private Map<String, House> houses;
    private Map<UUID, ArrayList<House>> playersHouses;

    public static HouseManager getInstance() {
        if (HouseManager.instance == null) {
            HouseManager.instance = new HouseManager();
        }

        return HouseManager.instance;
    }

    public void loadHouses(MemorySection housesConfigSection, File housesFolder) {
        this.houseTypes = housesConfigSection.getKeys(false).stream().map(
            houseTypeKey -> houseTypeKey
        ).toList();

        this.houses = new HashMap<>();
        this.playersHouses = new HashMap<>();

        for (File houseFile: housesFolder.listFiles()) {
            YamlConfiguration houseConfig = YamlConfiguration.loadConfiguration(houseFile);

            House house = new House(houseConfig);
            this.addHouse(house);
            this.addHouseToPlayer(house, house.getOwner());
        }
    }

    public void initHouseClassStaticVariables(MemorySection housesConfigSection) {
        List<String> allHouseTypes = new ArrayList<>();
        Map<String, Integer> allHousesMaxLevel = new HashMap<>();
        Map<String, List<Integer>> allHousesUpgradeCosts = new HashMap<>();

        housesConfigSection.getKeys(false).forEach(houseTypeKey -> {
            MemorySection houseTypeSection = (MemorySection)housesConfigSection.get(houseTypeKey);

            allHouseTypes.add(houseTypeKey);

            houseTypeSection.getKeys(false).forEach(houseDimensionKey -> {
                MemorySection houseDimensionSection = (MemorySection)houseTypeSection.get(houseDimensionKey);

                int houseTotalLevels = houseDimensionSection.getKeys(false).size();
                allHousesMaxLevel.put(houseTypeKey, houseTotalLevels);
                allHousesUpgradeCosts.put(houseTypeKey, (List<Integer>)houseDimensionSection.getKeys(false).stream().map(houseLevelKey ->
                    ((MemorySection)houseDimensionSection.get(houseLevelKey)).getInt("upgrade-cost")
                ).toList());
            });
        });

        House.setAllMaxLevels(allHousesMaxLevel);
        House.setAllUpgradeCosts(allHousesUpgradeCosts);
    }

    public List<String> getHouseTypes() {
        return this.houseTypes;
    }

    public Map<String, House> getHouses() {
        return this.houses;
    }

    public House getHouse(String name) {
        return this.houses.get(name);
    }

    public List<House> getPlayerHouses(Player player) {
        return this.playersHouses.containsKey(player.getUniqueId()) ?
            this.playersHouses.get(player.getUniqueId()) :
            new ArrayList<>();
    }

    public List<House> getTypeHouses(String type) {
        return this.houses.entrySet().stream().map(entry -> 
            entry.getKey().contains(type) ? entry.getValue() : null
        ).filter(house -> house != null).toList();
    }

    public List<House> getDimensionHouses(Dimension dimension) {
        return this.houses.entrySet().stream().map(entry -> 
            entry.getKey().contains(dimension.toString()) ? entry.getValue() : null
        ).filter(house -> house != null).toList();
    }

    public void addHouse(House house) {
        this.houses.put(house.getFullName(), house);
        house.save();
    }

    public void addHouseToPlayer(House house, UUID owner) {
        if (!this.playersHouses.containsKey(owner)) {
            this.playersHouses.put(owner, new ArrayList<>());
        }

        this.playersHouses.get(owner).add(house);
    }

    public void removeHouseFromOwner(House house) {
        UUID owner = house.getOwner();
        this.playersHouses.get(owner).remove(house);

        if (this.playersHouses.get(owner).isEmpty()) {
            this.playersHouses.remove(owner);
        }
    }

    public void transferFromOwnerToPlayer(House house, UUID to) {
        this.removeHouseFromOwner(house);
        this.addHouseToPlayer(house, to);
    }

    public boolean houseExists(String name) {
        return this.houses.containsKey(name);
    }

    public static void setInstance(HouseManager instance) {
        HouseManager.instance = instance;
    }

    public void setHouses(Map<String, House> houses) {
        this.houses = houses;
    }

}
