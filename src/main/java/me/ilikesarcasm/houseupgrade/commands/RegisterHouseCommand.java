package me.ilikesarcasm.houseupgrade.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import me.ilikesarcasm.houseupgrade.events.HouseRegistered;
import me.ilikesarcasm.houseupgrade.houses.House;
import me.wiefferink.areashop.regions.GeneralRegion;

public class RegisterHouseCommand extends HouseUpgradeCommand {

    @Override
    public String getCommandStart() {
        return "register";
    }

    @Override
    public String getPermission() {
        return "houseupgrade.register";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String houseType = args[0];
        GeneralRegion region = this.plugin.getAreaShopHandler().getRegion(args[1]);

        if (!this.plugin.getHouseManager().getHouseTypes().contains(houseType)) {
            HouseUpgrade.message(sender, "house.type.does-not-exist", args[1]);
            return;
        }

        if (region == null) {
            HouseUpgrade.message(sender, "region.does-not-exist", args[1]);
            return;
        }

        if (!this.plugin.getHouseManager().houseExists(region.getName())) {
            House house = new House(houseType, region);
            this.plugin.getHouseManager().addHouse(house);

            HouseRegistered e = new HouseRegistered(house);
            Bukkit.getPluginManager().callEvent(e);

            HouseUpgrade.message(sender, "house.register.success", region.getName());
        } else {
            HouseUpgrade.message(sender, "house.register.already-registered", region.getName());
        }
    }

    @Override
    public List<String> getTabCompleteList(CommandSender sender, String[] args) {
        List<String> completers;
        String argToComplete = args[args.length - 1];

        switch (args.length) {
            case 1:
                completers = this.plugin.getHouseManager().getHouseTypes().stream().filter(type ->
                    type.startsWith(argToComplete)
                ).toList();
                break;

            case 2:
                completers = this.plugin.getAreaShopHandler().getRegions().stream().map(GeneralRegion::getName).filter(
                    regionName -> regionName.startsWith(argToComplete)
                ).toList();
                break;

            default: completers = new ArrayList<>(); break;
        }
        
        return completers;
    }
    
}
