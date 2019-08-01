package xiaokun.forge.until;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xiaokun.forge.Forge;
import xiaokun.forge.event.LevelChangeEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    public static void loadPlayerData(final Player player) {
        File FILE = getPlayerFile(player);
        if (!FILE.exists()) {
            createDefaultPlayerData(player);
        }
    }

    public static void createDefaultPlayerData(final Player player) {
        Message.sendMessage("创建新玩家 §e[§4" +player.getName() + "§e] §a配置");
        File FILE = getPlayerFile(player);
        YamlConfiguration playerData = new YamlConfiguration();
        playerData.set("forge.level", 1);
        playerData.set("forge.exp", 0);
        playerData.set("map.num", 0);
        playerData.set("map.0", "");
        playerData.set("item.num", 0);
        playerData.set("item.0", "");
        try {
            playerData.save(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getPlayerFile(final Player player) {
        return new File("plugins" + File.separator + Forge.getPluginName() + File.separator + "PlayerData" + File.separator + player.getName() + ".yml");
    }

    public static int getExp(final Player player) {
        return getPlayerData(player).getInt("forge.exp");
    }

    public static int getMapNum(final Player player) {
        return getPlayerData(player).getInt("map.num");
    }

    public static int getItemNum(final Player player) {
        return getPlayerData(player).getInt("item.num");
    }

    public static ItemStack getItem(final Player player, int num) {
        return getPlayerData(player).getItemStack("item." + String.valueOf(num));
    }

    public static ItemStack getMap(final Player player, int num) {
        return getPlayerData(player).getItemStack("map." + String.valueOf(num));
    }

    public static int getLevel(final Player player) {
        return getPlayerData(player).getInt("forge.level");
    }

    public static void addExp(final Player player, int num) {
        YamlConfiguration yml = getPlayerData(player);
        int level = getLevel(player);
        int newLevel = level;
        int exp = getExp(player);
        final List<String> levelList = new ArrayList<String>(Forge.getPlugin().getConfig().getConfigurationSection("ForgeLevel").getKeys(false));
        for (String levels : levelList) {
            if(levels.equalsIgnoreCase(String.valueOf(level))) {
                int needExp = Config.getExp(String.valueOf(level + 1));
                if(exp + num < needExp) {
                    exp += num;
                    break;
                }else {
                    exp += num;
                    exp -= needExp;
                    newLevel = level + 1;
                    LevelChangeEvent levelChange = new LevelChangeEvent(player,level,newLevel);
                    Bukkit.getPluginManager().callEvent(levelChange);
                    break;
                }
            }
        }
        yml.set("forge.exp", exp);
        yml.set("forge.level", newLevel);
        try
        {
            yml.save(getPlayerFile(player));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getPlayerData(final Player player) {
        final File f = getPlayerFile(player);
        return YamlConfiguration.loadConfiguration(f);
    }
}
