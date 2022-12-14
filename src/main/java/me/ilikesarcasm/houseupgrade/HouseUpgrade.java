package me.ilikesarcasm.houseupgrade;

import io.github.ilikesarcasm.messages.Message;
import me.ilikesarcasm.houseupgrade.commands.CommandManager;
import me.ilikesarcasm.houseupgrade.handlers.AreaShopHandler;
import me.ilikesarcasm.houseupgrade.houses.HouseManager;
import me.ilikesarcasm.houseupgrade.listener.ListenerManager;
import io.github.ilikesarcasm.messages.LanguageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class HouseUpgrade extends JavaPlugin {

    private static final String CONFIG_FILE = "config.yml";
    private static final HashMap<String, String> CONFIG_PARAMS = new HashMap<String, String>() {{
        put("languageFileName", "default.language.file");
        put("houses", "houses");
    }};

    private static HouseUpgrade instance;
    private static File pluginFolder;

    private YamlConfiguration config;
    private AreaShopHandler areaShopHandler;
    private LanguageManager languageManager;
    private FileManager fileManager;
    private ListenerManager listenerManager;
    private CommandManager commandManager;
    private HouseManager houseManager;

    public static HouseUpgrade getInstance() {
        return HouseUpgrade.instance;
    }

    public static File getPluginFolder() {
        return HouseUpgrade.pluginFolder;
    }

    @Override
    public void onEnable() {
        HouseUpgrade.instance = this;
        HouseUpgrade.pluginFolder = this.getDataFolder();
        
        this.fileManager = FileManager.getInstance();
        this.fileManager.createFileStructure();
        
        File configFile = new File(HouseUpgrade.pluginFolder + File.separator + HouseUpgrade.CONFIG_FILE);
        this.config = YamlConfiguration.loadConfiguration(configFile);

        this.languageManager = LanguageManager.getInstance();
        File languageFile = new File(this.fileManager.get("langFolder").getAbsolutePath() + File.separator + this.config.get(HouseUpgrade.CONFIG_PARAMS.get("languageFileName")));
        this.languageManager.loadLanguage(languageFile);
        Message.setLanguageManager(this.languageManager);

        this.areaShopHandler = AreaShopHandler.getInstance();

        this.houseManager = HouseManager.getInstance();
        MemorySection housesConfigSection = (MemorySection)config.get(HouseUpgrade.CONFIG_PARAMS.get("houses"));
        this.houseManager.initHouseClassStaticVariables(housesConfigSection);
        this.houseManager.loadHouses(housesConfigSection, this.fileManager.get("houseFolder"));

        this.listenerManager = ListenerManager.getInstance();
        this.listenerManager.loadListeners(this);

        this.commandManager = CommandManager.getInstance();
        this.commandManager.loadCommands(this);

        HouseUpgrade.info("Initialized");
    }

    @Override
    public void onDisable() {
        HouseUpgrade.info("Stopped");
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public AreaShopHandler getAreaShopHandler() {
        return this.areaShopHandler;
    }

    public HouseManager getHouseManager() {
        return this.houseManager;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * Sends the message associated to a key to a Player instance
     * @param target Player to send the message to
     * @param key    Key to get the message from
     * @param params The optional parameters to edit the message
     */
    public static void message(Player target, String key, Object... params) {
        Message.fromKey(key, params).sendTo(target);
    }

    /**
     * Sends the message associated to a key event to a CommandSender instance
     * @param target CommandSender to send the message to
     * @param key    Key to get the message from
     * @param params The optional parameters to edit the message
     */
    public static void message(CommandSender target, String key, Object... params) {
        Message.fromKey(key, params).sendTo(target);
    }

    /**
     * Print an information to the console
     * @param message The message to print
     */
    public static void info(String message) {
        HouseUpgrade.getInstance().getLogger().info(message);
    }

    /**
     * Print a warning to the console
     * @param message The message to print
     */
    public static void warn(String message) {
        HouseUpgrade.getInstance().getLogger().warning(message);
    }

    /**
     * Print an error to the console
     * @param message The message to print
     */
    public static void error(String message) {
        HouseUpgrade.getInstance().getLogger().severe(message);
    }

}
