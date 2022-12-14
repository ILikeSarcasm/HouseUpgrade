package me.ilikesarcasm.houseupgrade.commands;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private static final String BASE_COMMAND = "houseupgrade";
    private static final String DEFAULT_COMMAND_NAME = "menu";

    private static CommandManager instance;

    private final HashMap<String, HouseUpgradeCommand> commands = new HashMap<>();

    public static CommandManager getInstance() {
        if (CommandManager.instance == null) {
            CommandManager.instance = new CommandManager();
        }

        return CommandManager.instance;
    }

    public static String getBaseCommand() {
        return CommandManager.BASE_COMMAND;
    }

    public void loadCommands(HouseUpgrade plugin) {
        List<HouseUpgradeCommand> commandsToLoad = new ArrayList<>();
        commandsToLoad.add(new MenuCommand());
        commandsToLoad.add(new SaveHouseStructureCommand());
        commandsToLoad.add(new RegisterHouseCommand());
        commandsToLoad.add(new UpgradeHouseCommand());

        for (HouseUpgradeCommand commandToLoad: commandsToLoad) {
            this.commands.put(commandToLoad.getCommandStart(), commandToLoad);
        }

        PluginCommand command = HouseUpgrade.getInstance().getServer().getPluginCommand(CommandManager.BASE_COMMAND);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = args.length > 0 ? args[0] : CommandManager.DEFAULT_COMMAND_NAME;
        String[] commandArgs = args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : null;
        
        if (!this.commands.containsKey(commandName)) {
            HouseUpgrade.message(sender, "cmd.does-not-exist");
            return false;
        }

        HouseUpgradeCommand c = this.commands.get(commandName);

        if (!sender.hasPermission(c.getPermission())) {
            HouseUpgrade.message(sender, "cmd.missing-permission");
            return false;
        }

        if (!c.canExecute(commandName, commandArgs)) {
            String messageKey = c.getHelp(sender);
            HouseUpgrade.message(sender, messageKey);
            return false;
        }

        c.execute(sender, commandArgs);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(!sender.hasPermission("houseupgrade.tabComplete")) {
            return new ArrayList<>();
        }
        
        String commandName = args.length > 0 ? args[0].toLowerCase() : "";
        String[] commandArgs = args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : null;

        return args.length <= 1 ?
            this.getCommandNameCompleters(sender, commandName) :
            this.getCommandParamsCompleters(sender, commandName, commandArgs);
    }

    private List<String> getCommandNameCompleters(CommandSender sender, String partialCommandName) {
        List<String> completers = new ArrayList<>();

        for (HouseUpgradeCommand c: this.commands.values()) {
            if (sender.hasPermission(c.getPermission())) {
                if (c.getCommandStart().startsWith(partialCommandName)) {
                    completers.add(c.getCommandStart());
                }
            }
        }

        completers.sort(String::compareToIgnoreCase);

        return completers;
    }

    private List<String> getCommandParamsCompleters(CommandSender sender, String commandName, String[] commandArgs) {
        List<String> completers = new ArrayList<>();

        for (HouseUpgradeCommand c: this.commands.values()) {
            if(sender.hasPermission(c.getPermission()) && c.canExecute(commandName, commandArgs)) {
                completers = c.getTabCompleteList(sender, commandArgs);
            }
        }
        
        completers.sort(String::compareToIgnoreCase);

        return completers;
    }

}
