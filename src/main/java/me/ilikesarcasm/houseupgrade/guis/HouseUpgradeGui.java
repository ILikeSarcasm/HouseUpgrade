package me.ilikesarcasm.houseupgrade.guis;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.slot.Slot;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;

public abstract class HouseUpgradeGui {

    static class GuiSlot {
        private final int slot;
        private final Material material;
        private final String name;
        private final Slot.ClickHandler clickHandler;

        public GuiSlot(int slot, Material material, String name, Slot.ClickHandler clickHandler) {
            this.slot = slot;
            this.material = material;
            this.name = name;
            this.clickHandler = clickHandler;
        }

        public int getSlot() {
            return this.slot;
        }

        public Material getMaterial() {
            return this.material;
        }

        public String getName() {
            return this.name;
        }

        public Slot.ClickHandler getClickHandler() {
            return this.clickHandler;
        }
    }

    protected HouseUpgrade plugin;

    public HouseUpgradeGui() {
        this.plugin = HouseUpgrade.getInstance();
    }
    
    public void displayMenu(Player player, Map<String, Object> args) {
        Menu menu = createMenu(player, args);
        menu.open(player);
    }

    protected abstract Menu createMenu(Player player, Map<String, Object> args);

}
