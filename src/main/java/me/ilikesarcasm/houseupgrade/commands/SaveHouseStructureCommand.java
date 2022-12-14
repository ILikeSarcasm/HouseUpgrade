package me.ilikesarcasm.houseupgrade.commands;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import me.ilikesarcasm.houseupgrade.houses.House;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveHouseStructureCommand extends HouseUpgradeCommand {

    @Override
    public String getCommandStart() {
        return "save";
    }

    @Override
    public String getPermission() {
        return "houseupgrade.save";
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

        StructureBlockLibApi.INSTANCE
            .saveStructure(this.plugin)
            .at(house.getLocation())
            .sizeX(house.getDimensions().getX())
            .sizeY(house.getDimensions().getY())
            .sizeZ(house.getDimensions().getZ())
            .saveToPath(house.getStructureFile().toPath())
            .onException(e -> {
                HouseUpgrade.message(sender, "house.structure.save.failed");
                HouseUpgrade.error("Couldn't save the structure.\n" + e);
            })
            .onResult(e -> {
                HouseUpgrade.message(sender, "house.structure.save.succeed");
                HouseUpgrade.info("Structure saved.");
            });
    }

}
