package me.ilikesarcasm.houseupgrade.handlers;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.regions.GeneralRegion;
import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AreaShopHandler {

    private static AreaShopHandler instance;

    private AreaShop areaShopPlugin;

    public static AreaShopHandler getInstance() {
        if (AreaShopHandler.instance == null) {
            AreaShopHandler.instance = new AreaShopHandler();
        }

        return AreaShopHandler.instance;
    }

    private AreaShopHandler() {
        this.areaShopPlugin = (AreaShop) HouseUpgrade.getInstance().getServer().getPluginManager().getPlugin("AreaShop");
    }

    public List<GeneralRegion> getRegions() {
        return this.areaShopPlugin.getFileManager().getRegions();
    }

    public GeneralRegion getRegion(String name) {
        return this.areaShopPlugin.getFileManager().getRegion(name);
    }

    public Set<GeneralRegion> getPlayerRegions(OfflinePlayer player) {
        Set<GeneralRegion> regions = new HashSet<>();

        // Get the regions owned by the player
        for(GeneralRegion region : this.areaShopPlugin.getFileManager().getRents()) {
            if(region.isOwner(player)) {
                regions.add(region);
            }
        }

        for(GeneralRegion region : this.areaShopPlugin.getFileManager().getBuys()) {
            if(region.isOwner(player)) {
                regions.add(region);
            }
        }

        // Get the regions the player is added as friend
        for(GeneralRegion region : this.areaShopPlugin.getFileManager().getRegions()) {
            if(region.getFriendsFeature().getFriends().contains(player.getUniqueId())) {
                regions.add(region);
            }
        }

        return regions;
    }

}
