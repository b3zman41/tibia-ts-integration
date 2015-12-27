package teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import config.TeamspeakConfig;
import reference.Reference;
import tibia.TibiaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class TibiaTeamspeak {

    public static final String offlineImage = "[img]https://cdn4.iconfinder.com/data/icons/fugue/icon/status-busy.png[/img]";
    public static final String onlineImage = "[img]http://www.blinktoolbar.com/images/user_icons/online.png[/img]";

    final TS3Config tsConfig;
    public TeamspeakConfig teamspeakConfig;

    public TS3Api tsApi;
    public TS3Query ts3Query;

    public TibiaTeamspeak() {
        tsConfig = new TS3Config();
        this.setInfoFromJSON("config.json");

        tsConfig.setHost(teamspeakConfig.host);
        tsConfig.setDebugLevel(Level.ALL);

        setInfoFromJSON("config.json");

        final TS3Query tsQuery = new TS3Query(tsConfig);
        tsQuery.connect();

        final TS3Api tsApi = tsQuery.getApi();
        tsApi.login(teamspeakConfig.username, teamspeakConfig.password);

        tsApi.setNickname("Some Bot");
        tsApi.selectVirtualServerById(1);

        this.tsApi = tsApi;
    }

    public void updateOnlineMap(HashMap<TibiaPlayer, Boolean> onlineMap) {
    }

    public void setInfoFromJSON(String fileName) {
        try {
            this.teamspeakConfig = Reference.objectMapper.readValue(new File(fileName), TeamspeakConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
