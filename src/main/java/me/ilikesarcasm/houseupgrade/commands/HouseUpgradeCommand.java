package me.ilikesarcasm.houseupgrade.commands;

import org.bukkit.command.CommandSender;

import me.ilikesarcasm.houseupgrade.HouseUpgrade;

import java.util.ArrayList;
import java.util.List;

public abstract class HouseUpgradeCommand {

    // The HouseUpgrade plugin instance
    protected HouseUpgrade plugin;

    /**
     * Constructor.
     * @param plugin The HouseUpgrade plugin instance
     */
    public HouseUpgradeCommand() {
        this.plugin = HouseUpgrade.getInstance();
    }

    /**
     * Get the argument that comes after the base command that this command reacts to.
     * @return The string that should be in front of the command for this class to act
     */
    public abstract String getCommandStart();

    /**
     * Returns the default permission needed for executing this command.
     * @return the permission as a string or null if no permission are required
     */
    public String getPermission() {
        return null;
    }

    /**
     * Check the given command and arguments can be executed by this instance.
     * @param commandName The name of the command to check for execution
     * @param args        The arguments to check
     * @return true if it can execute the command, false otherwise
     */
    public boolean canExecute(String commandName, String[] args) {
        return commandName.toLowerCase().startsWith(this.getCommandStart().toLowerCase());
    }

    /**
     * Returns the correct help string key to be used on the help page.
     * @param target The command caller
     * @return The help message key according to the permissions of the receiver
     */
    public String getHelp(CommandSender target) {
        return "help.no-help";
    }

    /**
     * Execute a (sub)command if the conditions are met.
     * @param sender The commandSender that executed the command
     * @param args   The arguments that are given
     */
    public abstract void execute(CommandSender sender, String[] args);

    /**
     * Get a list of string to complete a command with (raw list, not matching ones not filtered out).
     * @param sender     The CommandSender that wants to tab complete
     * @param start      The already given start of the command
     * @return A collection with all the possibilities for argument to complete
     */
    public List<String> getTabCompleteList(CommandSender sender, String[] start) {
        return new ArrayList<>();
    }

}
