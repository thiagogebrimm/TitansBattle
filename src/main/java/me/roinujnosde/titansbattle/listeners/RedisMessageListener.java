package me.roinujnosde.titansbattle.listeners;

import io.lettuce.core.pubsub.RedisPubSubListener;
import me.roinujnosde.titansbattle.TitansBattle;
import org.bukkit.Bukkit;

public class RedisMessageListener implements RedisPubSubListener<String, String> {

    private final TitansBattle plugin;

    public RedisMessageListener(TitansBattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public void message(String channel, String message) {
        if ("titansbattle-broadcasts".equals(channel)) {
            switch (message.toUpperCase()) {
                case "EVENT_ACTIVE":
                    plugin.getGameManager().setEventActive(true);
                    if (!plugin.getListenerManager().isBattleListenersRegistered()) {
                        plugin.getListenerManager().registerBattleListeners();
                        plugin.getLogger().info("Battle listeners registrados neste servidor devido ao evento ativo via Redis.");
                    }
                    break;

                case "EVENT_INACTIVE":
                    plugin.getGameManager().setEventActive(false);
                    if (plugin.getListenerManager().isBattleListenersRegistered()) {
                        plugin.getListenerManager().unregisterBattleListeners();
                        plugin.getLogger().info("Battle listeners desregistrados neste servidor devido ao evento inativo via Redis.");
                    }
                    break;

                default:
                    Bukkit.getServer().broadcastMessage(message);
                    break;
            }
        }
    }

    @Override
    public void message(String pattern, String channel, String message) {
        // Não necessário para este caso de uso
    }

    @Override
    public void subscribed(String channel, long count) {
        // Quando o canal é subscrito
    }

    @Override
    public void psubscribed(String pattern, long count) {
        // Quando o padrão é subscrito
    }

    @Override
    public void unsubscribed(String channel, long count) {
        // Quando o canal é desinscrito
    }

    @Override
    public void punsubscribed(String pattern, long count) {
        // Quando o padrão é desinscrito
    }
}
