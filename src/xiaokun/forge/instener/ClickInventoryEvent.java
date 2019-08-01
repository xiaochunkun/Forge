package xiaokun.forge.instener;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xiaokun.forge.Forge;
import xiaokun.forge.event.ExpChangeEvent;
import xiaokun.forge.until.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class ClickInventoryEvent implements Listener {
    private Inventory pInv;
    private String key = null;

    @EventHandler
    public void clickInventory(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inv = event.getInventory();
        final ItemStack eItem = event.getCurrentItem();
        final List<String> list =new ArrayList<String>();
        list.add("锻造台");
        list.add("可锻造列表");
        list.add("开始锻造");
        list.add("历史锻造");
        list.add("锻造中");
        switch (inv.getName()) {
            case "锻造台":
                event.setCancelled(true);
                break;
            case "可锻造列表":
                event.setCancelled(true);
                key = ItemConfig.getKey(ItemConfig.getMap(), eItem);
                if (key != null) {
                    pInv = CreateInventory.getInventory(2, player);
                    final YamlConfiguration yml = ItemConfig.getMaterialYml(key);
                    final ConfigurationSection section = yml.getConfigurationSection(key);
                    int num = 0;
                    pInv.setItem(19, eItem);
                    for (String keys : section.getKeys(false)) {
                        pInv.setItem((((num / 5) + 1) * 9) + 3 + (num % 5), section.getItemStack(keys));
                        num++;
                    }
                    player.closeInventory();
                    player.openInventory(pInv);
                }
                break;
            case "开始锻造":
                event.setCancelled(true);
                if ((eItem != null) && (!event.getCurrentItem().getType().equals(Material.AIR)) && (eItem.getItemMeta().getDisplayName().equals("开始锻造"))) {
                    ItemStack book = inv.getItem(19);
                    key = ItemConfig.getKey(ItemConfig.getMap(), book);
                    if (key != null) {
                        pInv = player.getInventory();
                        boolean confirm = false;
                        ItemStack[] map = pInv.getContents();
                        final YamlConfiguration yml = ItemConfig.getMaterialYml(key);
                        final ConfigurationSection section = yml.getConfigurationSection(key);
                        for (String keys : section.getKeys(false)) {
                            ItemStack item = section.getItemStack(keys);
                            final int needNum = item.getAmount();
                            final int hasNum = ItemConfig.getInvItemNum(pInv, item);
                            if (hasNum < needNum) {
                                confirm = true;
                                break;
                            } else {
                                ItemConfig.removeItem(pInv, item, needNum);
                            }
                        }
                        if (confirm) {
                            for (int i = 0; i < pInv.getSize(); i++) {
                                if (map[i] != null) {
                                    pInv.setItem(i, map[i]);
                                }
                            }
                            player.sendMessage(Message.getMessage("NoMaterial"));
                        } else {
                            ItemStack item2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
                            ItemMeta meta2 = item2.getItemMeta();
                            meta2.setDisplayName(" ");
                            item2.setItemMeta(meta2);
                            Inventory inv2 = Bukkit.createInventory(player, 27, "锻造中");

                            player.closeInventory();
                            player.openInventory(inv2);

                            AtomicInteger count = new AtomicInteger(0);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    inv2.setItem(count.get(), item2);
                                    count.getAndIncrement();
                                    if (count.get() >= 27) {
                                        cancel();
                                        player.closeInventory();
                                        final YamlConfiguration yml2 = ItemConfig.getItemYml(key);
                                        final ConfigurationSection section = yml2.getConfigurationSection(key);
                                        int a = 0;
                                        for (String k : section.getKeys(false)) {
                                            a++;
                                        }
                                        int sjs = (int) (Math.random() * a);
                                        ItemStack items = yml2.getItemStack(key + "." + String.valueOf(sjs));
                                        if ((items != null) && (items.getItemMeta() != null)) {
                                            ItemMeta meta = items.getItemMeta();
                                            List<String> l = new ArrayList<String>();
                                            List<String> l2 = meta.getLore();
                                            String level = Randoms.getLevel();
                                            double ratio = Randoms.getLevelAttribute(level);
                                            String name = Randoms.getLevelName(level);
                                            for (String s : l2) {
                                                if (s.indexOf("$forge-") != -1) {
                                                    double math = Double.valueOf(Forge.getSubString(s, "$forge-", "$"));
                                                    final int att = (int) Math.rint(math * ratio);
                                                    s = s.replace("$forge-" + String.valueOf((int) math) + "$", String.valueOf(att));
                                                    if (s.indexOf("$forge-") != -1) {
                                                        double math2 = Double.valueOf(Forge.getSubString(s, "$forge-", "$"));
                                                        final int att2 = (int) Math.rint(math2 * ratio);
                                                        s = s.replace("$forge-" + String.valueOf((int) math2) + "$", String.valueOf(att2));
                                                    }
                                                }
                                                if (s.indexOf("%quality%") != 1) {
                                                    s = s.replace("%quality%", name);
                                                }

                                                if (s.indexOf("%dz_author%") != 1) {
                                                    s = s.replace("%dz_author%", player.getName());
                                                }
                                                l.add(s);
                                            }
                                            meta.setLore(l);
                                            items.setItemMeta(meta);

                                            String notice = Message.getMessage("Announce");
                                            if (notice.length() != 0) {
                                                if (eItem.getItemMeta() != null) {
                                                    TextComponent tc = Message.getTextComponent(notice.replaceAll("%item%", items.getItemMeta().getDisplayName()).replaceAll("%player%", player.getName()), null, items.getItemMeta().getLore());
                                                    Bukkit.spigot().broadcast(tc);
                                                }
                                            }

                                            /*
                                            Inventory newInv = Bukkit.createInventory(null, 9, "请拿走你锻造的装备");
                                            newInv.setItem(4, items);
                                            player.openInventory(newInv);

                                             */

                                            pInv.addItem(items);
                                            player.sendMessage(Message.getMessage("Successful"));

                                            PlayerData.addExp(player, ItemConfig.getExp(key));

                                            ExpChangeEvent e = new ExpChangeEvent(player, ItemConfig.getExp(key));
                                            Bukkit.getServer().getPluginManager().callEvent(e);

                                            File file = PlayerData.getPlayerFile(player);
                                            YamlConfiguration yml3 = YamlConfiguration.loadConfiguration(file);
                                            int num = PlayerData.getItemNum(player);
                                            yml3.set("item." + String.valueOf(num), items);
                                            yml3.set("item.num", ++num);
                                            try {
                                                yml3.save(file);
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }, 0, 100);
                        }
                    }
                }
                break;
            case "历史锻造":
                event.setCancelled(true);
                break;
           // case "请拿走你锻造的装备":
            case "锻造中":
                event.setCancelled(true);
                break;
        }
        if ((eItem != null) && (!eItem.getType().equals(Material.AIR)) && (list.contains(inv.getName()))) {
            ItemMeta eMeta = eItem.getItemMeta();
            switch (eMeta.getDisplayName()) {
                case "查看已学习的图纸":
                    Turn.setMapNum(Turn.getMapNum() + 1);
                    pInv = CreateInventory.getInventory(1, player);
                    player.closeInventory();
                    player.openInventory(pInv);
                    break;
                case "查看历史锻造的装备":
                    Turn.setItemNum(Turn.getItemNum() + 1);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            pInv = CreateInventory.getInventory(3, player);
                            player.closeInventory();
                            player.openInventory(pInv);

                        }
                    }.runTask(Forge.getPlugin());
                    break;
                case "返回主菜单":
                    pInv = CreateInventory.getInventory(0, player);
                    player.closeInventory();
                    player.openInventory(pInv);
                    break;
                case "上一页":
                    if (inv.getName().equals("可锻造列表")) {
                        Turn.setMapNum(Turn.getMapNum() - 1);
                        pInv = CreateInventory.getInventory(1, player);
                        player.closeInventory();
                        player.openInventory(pInv);
                    }
                    if (inv.getName().equals("历史锻造")) {
                        Turn.setItemNum(Turn.getItemNum() - 1);
                        pInv = CreateInventory.getInventory(3, player);
                        player.closeInventory();
                        player.openInventory(pInv);
                    }
                    break;
                case "下一页":
                    if (inv.getName().equals("可锻造列表")) {
                        Turn.setMapNum(Turn.getMapNum() + 1);
                        pInv = CreateInventory.getInventory(1, player);
                        player.closeInventory();
                        player.openInventory(pInv);
                    }
                    if (inv.getName().equals("历史锻造")) {
                        Turn.setItemNum(Turn.getItemNum() + 1);
                        pInv = CreateInventory.getInventory(3, player);
                        player.closeInventory();
                        player.openInventory(pInv);
                    }
                    break;
            }
        }
    }
}
