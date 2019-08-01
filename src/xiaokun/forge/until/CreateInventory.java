package xiaokun.forge.until;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateInventory {

    private static Inventory inv;
    private static ItemStack item;
    private static ItemMeta meta;

    public static Inventory getInventory(int type, Player player) {
        if (type == 0) {
            inv = Bukkit.createInventory(null, 27, "锻造台");
            item = new ItemStack(Material.ENCHANTED_BOOK);
            meta = item.getItemMeta();
            meta.setDisplayName("查看已学习的图纸");
            item.setItemMeta(meta);
            inv.setItem(11, item);

            meta.setDisplayName("查看历史锻造的装备");
            item.setItemMeta(meta);
            inv.setItem(15, item);

            Turn.init();
            return inv;
        }
        if (type == 1) {
            inv = Bukkit.createInventory(null, 54, "可锻造列表");
            int num = PlayerData.getMapNum(player);
            for (int i = 0; i < num; i++) {
                if (i < ((Turn.getMapNum()) * 45)) {
                    if (PlayerData.getMap(player, ((Turn.getMapNum() - 1) * 45) + i) != null) {
                        inv.addItem(PlayerData.getMap(player, ((Turn.getMapNum() - 1) * 45) + i));
                    }
                } else {
                    break;
                }
            }
            setInventory(inv);
            return inv;
        }
        if (type == 2) {
            inv = Bukkit.createInventory(null, 54, "开始锻造");
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            inv.setItem(0, item);
            inv.setItem(1, item);
            inv.setItem(2, item);
            inv.setItem(3, item);
            inv.setItem(4, item);
            inv.setItem(5, item);
            inv.setItem(6, item);
            inv.setItem(7, item);
            inv.setItem(8, item);
            inv.setItem(9, item);
            inv.setItem(11, item);
            inv.setItem(17, item);
            inv.setItem(18, item);
            inv.setItem(20, item);
            inv.setItem(26, item);
            inv.setItem(27, item);
            inv.setItem(29, item);
            inv.setItem(35, item);
            inv.setItem(36, item);
            inv.setItem(38, item);
            inv.setItem(44, item);
            inv.setItem(45, item);
            inv.setItem(46, item);
            inv.setItem(47, item);
            inv.setItem(48, item);
            inv.setItem(50, item);
            inv.setItem(51, item);
            inv.setItem(52, item);
            inv.setItem(53, item);

            item = new ItemStack(Material.WORKBENCH);
            meta = item.getItemMeta();
            meta.setDisplayName("开始锻造");
            item.setItemMeta(meta);
            inv.setItem(49, item);
            return inv;
        }
        if (type == 3) {
            inv = Bukkit.createInventory(null, 54, "历史锻造");
            int num = PlayerData.getItemNum(player);
            for (int i = 0; i < num; i++) {
                if (i < ((Turn.getItemNum()) * 45)) {
                    if (PlayerData.getItem(player, ((Turn.getItemNum() - 1) * 45) + i) != null) {
                        inv.addItem(PlayerData.getItem(player, ((Turn.getItemNum() - 1) * 45) + i));
                    }
                } else {
                    break;
                }
            }
            setInventory(inv);
            return inv;
        }
        return null;
    }

    static void setInventory(Inventory inv) {
        item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        inv.setItem(46, item);
        inv.setItem(47, item);
        inv.setItem(48, item);
        inv.setItem(50, item);
        inv.setItem(51, item);
        inv.setItem(52, item);

        item = new ItemStack(Material.ARROW);
        meta = item.getItemMeta();
        meta.setDisplayName("上一页");
        item.setItemMeta(meta);
        inv.setItem(45, item);

        meta.setDisplayName("下一页");
        item.setItemMeta(meta);
        inv.setItem(53, item);

        item = new ItemStack(Material.SKULL_ITEM);
        meta = item.getItemMeta();
        meta.setDisplayName("返回主菜单");
        item.setItemMeta(meta);
        inv.setItem(49, item);
    }
}
