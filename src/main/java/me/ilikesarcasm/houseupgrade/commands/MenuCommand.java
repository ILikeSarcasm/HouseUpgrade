package me.ilikesarcasm.houseupgrade.commands;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import me.ilikesarcasm.houseupgrade.guis.MenuGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends HouseUpgradeCommand {

    @Override
    public String getCommandStart() {
        return "menu";
    }

    @Override
    public String getPermission() {
        return "houseupgrade.menu";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            HouseUpgrade.message(sender, "cmd.player-only");
            return;
        }

        Player player = (Player)sender;
        new MenuGui().displayMenu(player, null);
    }

}
