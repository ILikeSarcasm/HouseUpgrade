package me.ilikesarcasm.houseupgrade.listener;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import org.bukkit.Bukkit;
import org.ipvp.canvas.MenuFunctionListener;

public class ListenerManager {

    private static ListenerManager instance;

    public static ListenerManager getInstance() {
        if (ListenerManager.instance == null) {
            ListenerManager.instance = new ListenerManager();
        }

        return ListenerManager.instance;
    }

    public void loadListeners(HouseUpgrade plugin) {
        Bukkit.getPluginManager().registerEvents(new MenuFunctionListener(), HouseUpgrade.getInstance());
        Bukkit.getPluginManager().registerEvents(new BoughtRegionListener(plugin), HouseUpgrade.getInstance());
    }

}
