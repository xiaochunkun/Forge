package xiaokun.forge.command;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xiaokun.forge.Forge;
import xiaokun.forge.until.*;

import java.util.ArrayList;
import java.util.List;


public class OnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if ((command.getName().equalsIgnoreCase("dz")) && (args.length == 0)) {
                if ((player.hasPermission("dz.duanzao"))) {
                    Turn.init();
                    player.closeInventory();
                    player.openInventory(CreateInventory.getInventory(0, player));
                } else {
                    player.sendMessage("§a 你没有§e[§4 dz.duanzao §e]§a权限");
                }
                return true;
            } else if ((args[0].equalsIgnoreCase("map")) && (args.length >= 1)) {
                if ((player.hasPermission("dz.map"))) {
                    if (args.length == 1) {
                        List<String> list = ItemConfig.getList();
                        if (list.size() > 0) {
                            player.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b 图纸列表 §4 -----§c ----§7 ---§8 --§0 -");
                            for (int i = 0; i < list.size(); i++) {
                                String o = list.get(i);
                                final YamlConfiguration yml = ItemConfig.getMapYml(o);
                                final ItemMeta meta = yml.getItemStack(o + ".item").getItemMeta();
                                String name = null;
                                if (meta != null) {
                                    name = meta.getDisplayName();
                                } else {
                                    name = o;
                                }
                                TextComponent tc = Message.getTextComponent("§e" + String.valueOf(i + 1) + ". §a" + name, "/dz map give " + o, meta.getLore());
                                Message.sendTextComponent(sender, tc);
                            }
                        } else {
                            player.sendMessage("§4暂无图纸信息");
                        }
                        player.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b 点击领取 §4 -----§c ----§7 ---§8 --§0 -");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("give") && args.length >= 2) {
                        if (!(Bukkit.getPlayer(args[2]) instanceof Player) && (ItemConfig.getList().contains(args[2]))) {
                            final YamlConfiguration yml = ItemConfig.getMapYml(args[2]);
                            final ItemStack item = yml.getItemStack(args[2] + ".item");
                            if (args.length == 3) {
                                player.getInventory().addItem(item);
                            } else {
                                if (Integer.valueOf(args[3]) instanceof Integer) {
                                    item.setAmount(Integer.valueOf(args[3]));
                                }
                                player.getInventory().addItem(item);
                            }
                            player.sendMessage("§a 物品已经发送至背包，请查收");
                            return true;
                        } else if ((Bukkit.getPlayer(args[2]) instanceof Player) && (ItemConfig.getList().contains(args[3]))) {
                            final YamlConfiguration yml = ItemConfig.getMapYml(args[3]);
                            final ItemStack item = yml.getItemStack(args[3] + ".item");
                            Player p = Bukkit.getPlayer(args[2]);
                            if (args.length == 4) {
                                p.getInventory().addItem(item);
                            } else {
                                if (Integer.valueOf(args[4]) instanceof Integer) {
                                    item.setAmount(Integer.valueOf(args[4]));
                                }
                                p.getInventory().addItem(item);
                            }
                            player.sendMessage("§a 物品已经发送至背包，请查收");
                        }
                    }
                } else {
                    player.sendMessage("§a 你没有§e[§4 dz.map §e]§a权限");
                }
                return true;
            } else if ((args[0].equalsIgnoreCase("material")) && (args.length >= 1)) {
                if (player.hasPermission("dz.material")) {
                    if (args.length == 1) {
                        List<String> list = ItemConfig.getList();
                        if (list.size() > 0) {
                            player.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b 材料列表 §4 -----§c ----§7 ---§8 --§0 -");
                            for (int i = 0; i < list.size(); i++) {
                                String o = list.get(i);
                                final YamlConfiguration yml = ItemConfig.getMapYml(o);
                                final ItemMeta meta = yml.getItemStack(o + ".item").getItemMeta();
                                String name = null;
                                if (meta != null) {
                                    name = meta.getDisplayName() + " §e的材料";
                                } else {
                                    name = o + " §e的材料";
                                }
                                TextComponent tc = Message.getTextComponent("§e" + String.valueOf(i + 1) + ". §a" + name, "/dz material give " + o, meta.getLore());
                                Message.sendTextComponent(sender, tc);
                            }
                        } else {
                            player.sendMessage("§4暂无物品信息");
                        }
                        player.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b 点击领取 §4 -----§c ----§7 ---§8 --§0 -");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("give") && args.length >= 2) {
                        if (!(Bukkit.getPlayer(args[2]) instanceof Player) && (ItemConfig.getList().contains(args[2]))) {
                            final YamlConfiguration yml = ItemConfig.getMaterialYml(args[2]);
                            final ConfigurationSection section = yml.getConfigurationSection(args[2]);
                            if (args.length == 3) {
                                for (String key : section.getKeys(false)) {
                                    player.getInventory().addItem(section.getItemStack(key));
                                }
                            } else {
                                if (Integer.valueOf(args[3]) instanceof Integer) {
                                    for (String key : section.getKeys(false)) {
                                        final ItemStack item = section.getItemStack(key);
                                        item.setAmount(Integer.valueOf(args[3]));
                                        player.getInventory().addItem(item);
                                    }
                                }
                            }
                            player.sendMessage("§a 物品已经发送至背包，请查收");
                            return true;
                        } else if ((Bukkit.getPlayer(args[2]) instanceof Player) && (ItemConfig.getList().contains(args[3]))) {
                            final YamlConfiguration yml = ItemConfig.getMaterialYml(args[3]);
                            final ConfigurationSection section = yml.getConfigurationSection(args[3]);
                            if (args.length == 4) {
                                for (String key : section.getKeys(false)) {
                                    player.getInventory().addItem(section.getItemStack(key));
                                }
                            } else {
                                if (Integer.valueOf(args[4]) instanceof Integer) {
                                    for (String key : section.getKeys(false)) {
                                        final ItemStack item = section.getItemStack(key);
                                        item.setAmount(Integer.valueOf(args[4]));
                                        player.getInventory().addItem(item);
                                    }
                                }
                            }
                            player.sendMessage("§a 物品已经发送至背包，请查收");
                        }
                    }
                } else {
                    player.sendMessage("§a 你没有§e[§4 dz.material §e]§a权限");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload") && (args.length == 1)) {
                if (player.hasPermission("dz.reload")){
                    Config.loadConfig();
                    Message.loadMessage();
                    ItemConfig.loadMapConfig();
                    ItemConfig.loadItemConfig();
                    ItemConfig.loadMaterialConfig();
                    player.sendMessage(Message.getMessage("Reload"));
                } else {
                    player.sendMessage("§a 你没有§e[§4 dz.reload §e]§a权限");
                }
                return true;
            } else {
                player.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b " + Forge.getPluginName() + "§4 -----§c ----§7 ---§8 --§0 -");
                List<String> l = new ArrayList<String>();
                l.add("点击即可自动输入指令");
                TextComponent tc = Message.getTextComponent("§a/dz 打开锻造列表", "/dz", l);
                Message.sendTextComponent(sender, tc);

                tc = Message.getTextComponent("§a/dz map 打开图纸列表", "/dz map", l);
                Message.sendTextComponent(sender, tc);

                tc = Message.getTextComponent("§a/dz material 打开材料列表", "/dz material", l);
                Message.sendTextComponent(sender, tc);

                tc = Message.getTextComponent("§a/dz reload 重载配置文件", "/dz reload", l);
                Message.sendTextComponent(sender, tc);

                player.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b " + Forge.getPluginName() + "§4 -----§c ----§7 ---§8 --§0 -");
                return true;
            }
        }
        return false;
    }

    private SenderType getType(CommandSender sender) {
        if (sender instanceof Player) {
            return SenderType.PLAYER;
        }
        return SenderType.CONSOLE;
    }


}
