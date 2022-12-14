package me.ilikesarcasm.houseupgrade.guis;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ipvp.canvas.ClickInformation;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;

import net.kyori.adventure.text.Component;

public class MenuGui extends HouseUpgradeGui {

    @Override
    protected Menu createMenu(Player player, Map<String, Object> args) {
        Menu menu = ChestMenu.builder(5)
                .title("HouseUpgrade")
                .redraw(true)
                .build();

        ArrayList<GuiSlot> slots = new ArrayList<GuiSlot>() {{
            add(new GuiSlot(13, Material.BRICKS, "MY HOUSES", (Player p, ClickInformation info) -> {
                new HouseListGui().displayMenu(p, null);
            }));
        }};

        slots.forEach(s -> {
            Slot slot = menu.getSlot(s.getSlot());

            ItemStack item = new ItemStack(s.getMaterial());
            ItemMeta metadata = item.getItemMeta();
            Component component = Component.text(s.getName());
            metadata.displayName(component);
            item.setItemMeta(metadata);
            slot.setItem(item);

            slot.setClickHandler(s.getClickHandler());
        });

        return menu;
    }

}
