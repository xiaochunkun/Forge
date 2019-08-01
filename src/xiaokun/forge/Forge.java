package xiaokun.forge;


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xiaokun.forge.command.OnCommand;
import xiaokun.forge.instener.ClickInventoryEvent;
import xiaokun.forge.instener.InteractEvent;
import xiaokun.forge.instener.JoinEvent;
import xiaokun.forge.until.*;

import java.util.Collection;
import java.util.Random;


public class Forge extends JavaPlugin {


    @Getter
    private static final Random random = new Random();
    @Getter
    private static String pluginName;
    @Getter
    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        pluginName = this.getName();
        plugin = this;

        Message.sendNormalMessage("§a==== §6" + pluginName + " > 正在载入§a ====");
        Message.sendNormalMessage("§a> 当前服务器版本：§f" + getSubString(Bukkit.getVersion(), "MC: ", ")"));
        Message.sendNormalMessage("§a> 作者：§fXiaokun");
        Bukkit.getPluginCommand("dz").setExecutor(new OnCommand());
        Bukkit.getPluginManager().registerEvents(new ClickInventoryEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(),this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Papi().register();
        } else {
            getLogger().info("§a未找到PlaceholderAPI.");
        }
        Config.loadConfig();
        Message.loadMessage();
        ItemConfig.loadMapConfig();
        ItemConfig.loadItemConfig();
        ItemConfig.loadMaterialConfig();
        Collection<? extends Player> p = Bukkit.getOnlinePlayers();
        for (Player player : p) {
            PlayerData.loadPlayerData(player);
        }
        Message.sendNormalMessage("§a==== §6" + pluginName + " > 载入完成§a ====");
    }

    public static String getSubString(String text, String left, String right) {
        String result = null;
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

}
