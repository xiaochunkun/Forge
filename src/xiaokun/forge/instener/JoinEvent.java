package xiaokun.forge.instener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xiaokun.forge.until.PlayerData;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!PlayerData.getPlayerFile(player).exists()) {
            PlayerData.loadPlayerData(player);
        }
    }
}
