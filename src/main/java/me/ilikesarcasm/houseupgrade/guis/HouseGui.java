package me.ilikesarcasm.houseupgrade.guis;

import me.ilikesarcasm.houseupgrade.commands.UpgradeHouseCommand;
import me.ilikesarcasm.houseupgrade.houses.House;
import net.kyori.adventure.text.Component;

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

public class HouseGui extends HouseUpgradeGui {

    @Override
    protected Menu createMenu(Player player, Map<String, Object> args) {
        Menu menu = ChestMenu.builder(5)
                .title("HouseUpgrade")
                .redraw(true)
                .build();
                
        ArrayList<GuiSlot> slots = new ArrayList<GuiSlot>() {{
            add(new GuiSlot(12, Material.EXPERIENCE_BOTTLE, "UPGRADE", (Player p, ClickInformation info) -> {
                String[] params = { ((House)args.get("house")).getRegion().getName() };
                new UpgradeHouseCommand().execute(p, params);
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
