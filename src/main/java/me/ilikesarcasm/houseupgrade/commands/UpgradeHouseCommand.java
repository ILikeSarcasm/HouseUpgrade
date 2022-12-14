package me.ilikesarcasm.houseupgrade.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import me.ilikesarcasm.houseupgrade.houses.House;

public class UpgradeHouseCommand extends HouseUpgradeCommand {
    
    @Override
    public String getCommandStart() {
        return "upgrade";
    }

    @Override
    public String getPermission() {
        return "houseupgrade.upgrade";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            HouseUpgrade.message(sender, "cmd.player-only");
            return;
        }

        if (!this.plugin.getHouseManager().houseExists(args[0])) {
            HouseUpgrade.message(sender, "house.does-not-exist", args[0]);
            return;
        }

        House house = this.plugin.getHouseManager().getHouse(args[0]);

        if (house.isMaxedOut()) {
            HouseUpgrade.message(sender, "house.upgrade.already-maxed-out", args[0]);
            return;
        }

        // TODO: Rebuild structure with Air block checking to remove user placed blocks

        house.upgrade();

        StructureBlockLibApi.INSTANCE
            .loadStructure(this.plugin)
            .at(house.getLocation())
            .loadFromPath(house.computeStructureFile().toPath())
            .onException(e -> {
                HouseUpgrade.message(sender, "house.upgrade.failed");
                HouseUpgrade.error("Couldn't upgrade the house.\n" + e);
            })
            .onResult(e -> {
                HouseUpgrade.message(sender, "house.upgrade.succeed");
                HouseUpgrade.info("House upgraded.");
            });
    }
    
}
