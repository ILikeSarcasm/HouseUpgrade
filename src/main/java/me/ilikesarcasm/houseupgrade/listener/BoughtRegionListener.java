package me.ilikesarcasm.houseupgrade.listener;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import me.ilikesarcasm.houseupgrade.houses.House;
import me.wiefferink.areashop.events.notify.BoughtRegionEvent;
import me.wiefferink.areashop.regions.GeneralRegion;

public class BoughtRegionListener implements Listener {

    private HouseUpgrade plugin;

    public BoughtRegionListener(HouseUpgrade plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBoughtRegion(BoughtRegionEvent event) {
        GeneralRegion region = event.getRegion();
        House house = this.plugin.getHouseManager().getHouse(region.getName());
        if (house != null) {
            this.plugin.getHouseManager().addHouseToPlayer(house, region.getOwner());

            OfflinePlayer offleinPlayer = this.plugin.getServer().getOfflinePlayer(region.getOwner());
            HouseUpgrade.info(offleinPlayer.getName() + " is now owner of house " + house.getName());

            Player player = offleinPlayer.getPlayer();
            if (player != null) {
                HouseUpgrade.message(player.getPlayer(), "house.bought", house.getName());
            }
        }
    }
    
}
