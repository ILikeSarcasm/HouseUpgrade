package me.ilikesarcasm.houseupgrade.guis;

import me.ilikesarcasm.houseupgrade.houses.House;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ipvp.canvas.ClickInformation;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

public class HouseListGui extends HouseUpgradeGui {

    @Override
    protected Menu createMenu(Player player, Map<String, Object> args) {
        // TODO: Paginated menu
        Menu menu = ChestMenu.builder(5)
                .title("HouseUpgrade")
                .redraw(true)
                .build();

        List<House> houses = this.plugin.getHouseManager().getPlayerHouses(player);
        for (int i = 0; i < houses.size(); i++) {
            House house = houses.get(i);
            Slot slot = menu.getSlot(i);

            slot.setItemTemplate((Player p) -> {
                ItemStack item = new ItemStack(Material.OAK_DOOR);
                ItemMeta itemMeta = item.getItemMeta();
                
                itemMeta.displayName(Component.text("House: " + house.getName()));
                item.setItemMeta(itemMeta);

                return item;
            });
            
            slot.setClickHandler((Player p, ClickInformation info) -> {
                HashMap<String, Object> params = new HashMap<String, Object>() {{
                    put("house", house);
                }};

                new HouseGui().displayMenu(p, params);
            });
        }

        return menu;
    }

}
