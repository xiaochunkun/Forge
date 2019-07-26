package xiaokun.forge.until;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xiaokun.forge.Forge;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;


public class Message {
    @Getter
    static final File file = new File("plugins" + File.separator + Forge.getPluginName() + File.separator + "message.yml");
    @Getter
    static final YamlConfiguration yml = new YamlConfiguration();


    public static void sendNormalMessage(String msg) {
        if (msg != null) {
            Bukkit.getConsoleSender().sendMessage("§a" + msg);
        }

    }

    public static void sendMessage(String msg) {
        if (msg != null) {
            Bukkit.getConsoleSender().sendMessage("[" + Forge.getPluginName() + "] §a" + msg);
        }
    }

    public static TextComponent getTextComponent(String message, String command, List<String> stringList) {
        TextComponent tcMessage = new TextComponent(message);
        if (stringList != null && stringList.size() > 0) {
            ComponentBuilder bc = new ComponentBuilder("§7" + stringList.get(0).replace("&", "§"));
            IntStream.range(1, stringList.size()).mapToObj(i -> "\n§7" + stringList.get(i).replace("&", "§")).forEach(bc::append);
            tcMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bc.create()));
        }
        if (command != null) {
            tcMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        return tcMessage;
    }

    public static void sendTextComponent(CommandSender sender, TextComponent tc) {
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(tc);
        } else {
            sender.sendMessage(tc.getText());
        }
    }



    public static void loadMessage() {
        if (!file.exists()) {
            createMessage();
        }
        Message.sendMessage("§a找到 message.yml");
        try {
            yml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Message.sendMessage("§a读取 message.yml 时发生错误");
        }
    }

    public static String getMessage(String name) {
        return yml.getString(name);
    }

    static void createMessage() {
        yml.set("NoStudyMap", "§c你已经学过该图纸，请勿重复学习");
        yml.set("NoForgeGrade", "§4你的锻造等级不够！！！");
        yml.set("NoMaterial", "§c材料不全，已经退还全部材料");
        yml.set("Announce", "§a恭喜 §e[§4%player%§e] §a锻造出 %item%");
        yml.set("Reload", "§a配置重载已完成!!");
        yml.set("Successful","§a 锻造完成！已发送物品到你的背包!!");
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
