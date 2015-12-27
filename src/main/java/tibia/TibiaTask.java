package tibia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.mashape.unirest.http.exceptions.UnirestException;
import config.TeamspeakConfig;
import reference.Reference;
import teamspeak.TibiaTeamspeak;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class TibiaTask{

    public static Map<String, List<Death>> playerDeaths = new HashMap<>();

    private static Date lastPoll = new Date();

    private TeamspeakConfig config;
    private TibiaTeamspeak tibiaTeamspeak;

    private HashMap<String, TibiaWorld> worldCache = new HashMap<>();

    private ArrayList<TibiaPlayer> allPlayers = new ArrayList<>();
    private ArrayList<TibiaPlayer> deathPlayers = new ArrayList<>();
    private ArrayList<TibiaPlayer> statusPlayers = new ArrayList<>();

    private HashMap<TibiaPlayer, Boolean> onlineMap = new HashMap<>();

    public TibiaTask(TibiaTeamspeak tibiaTeamspeak) {
        this.tibiaTeamspeak = tibiaTeamspeak;
        this.config = tibiaTeamspeak.teamspeakConfig;
    }

    public void poll() {
        System.out.println("POLLING " + ((new Date().getTime() - lastPoll.getTime()) / 1000) + " seconds later");
        lastPoll = new Date();
        config.death.stream().forEach(this::addPlayerToList);
        config.status.stream().forEach(this::addPlayerToList);

        allPlayers.stream().forEach(player -> {
            this.addWorldToCache(player.getWorld());
        });

        statusPlayers.stream().forEach(player -> {
            onlineMap.put(player, worldCache.get(player.getWorld()).isPlayerOnline(player.getName()));
        });

        if (Reference.hasRunOnce) {
            pokeFromNewDeaths();
        } else Reference.hasRunOnce = true;

        setLastDeathMap();
        updateDescriptionFromOnlineMap();
    }

    public void setLastDeathMap() {
        allPlayers.stream().forEach(player -> {
            TibiaTask.playerDeaths.put(player.getName(), player.getDeaths());
        });
    }

    public void pokeFromNewDeaths() {
        deathPlayers.stream().forEach(player -> {
            List<Death> previousList = TibiaTask.playerDeaths.get(player.getName());

            int size = 0;
            if (previousList != null) {
                size = previousList.size();
            }

            if (player.getDeaths().size() > size) {
                Death latest = Death.getLatestDeathFromDeathList(player.getDeaths());

                tibiaTeamspeak.tsApi.getClients().forEach(client -> {
                    tibiaTeamspeak.tsApi.pokeClient(client.getId(), player.getName() + " Died : " + latest.description);
                });
            }
        });
    }

    public void updateDescriptionFromOnlineMap() {
        String serverMessage = "";
        for (TibiaPlayer tibiaPlayer : onlineMap.keySet()) {
            serverMessage += "[left]" + (onlineMap.get(tibiaPlayer) ? TibiaTeamspeak.onlineImage : TibiaTeamspeak.offlineImage) + tibiaPlayer.getName() + "[/left]";
        }
        Channel channel = tibiaTeamspeak.tsApi.getChannels().get(0);

        Map<ChannelProperty, String> properties = new HashMap<>();
        properties.put(ChannelProperty.CHANNEL_DESCRIPTION, serverMessage);

        tibiaTeamspeak.tsApi.editChannel(channel.getId(), properties);
    }

    public void addPlayerToList(String player) {
        if (!allPlayers.contains(player)) {
            try {
                System.out.println("Adding player : " + player);
                TibiaPlayer tibiaPlayer = new TibiaPlayer(player);
                allPlayers.add(tibiaPlayer);

                if (config.status.contains(player)) {
                    statusPlayers.add(tibiaPlayer);
                }

                if (config.death.contains(player)) {
                    deathPlayers.add(tibiaPlayer);
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addWorldToCache(String worldName) {

        boolean hasWorld = false;
        for (TibiaWorld world : this.worldCache.values()) {
            if (world.worldName.equals(worldName)) {
                hasWorld = true;
            }
        }

        if (!hasWorld) {
            try {
                Thread.sleep(1000);
                this.worldCache.put(worldName, new TibiaWorld(worldName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
