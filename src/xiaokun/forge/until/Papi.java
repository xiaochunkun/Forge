package xiaokun.forge.until;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Papi extends PlaceholderExpansion {
    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "XiaoKun";
    }

    @Override
    public String getIdentifier() {
        return "dz";
    }

    @Override
    public String getVersion() {
        return "1.0.3";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        if (identifier.equals("level")) {
            return String.valueOf(PlayerData.getLevel(player));
        }
        if (identifier.equals("exp")) {
            return String.valueOf(PlayerData.getExp(player));
        }
        if (identifier.equals("needexp")) {
            if (Config.getExp(String.valueOf(PlayerData.getLevel(player) + 1)) > 0) {
                return String.valueOf(Config.getExp(String.valueOf(PlayerData.getLevel(player) + 1)) - PlayerData.getExp(player));
            } else {
                return "0";
            }
        }
        if (identifier.equals("nextlevel")) {
            return String.valueOf(Config.getExp(String.valueOf(PlayerData.getLevel(player) + 1)));
        }
        return null;
    }
}
