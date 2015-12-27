import com.fasterxml.jackson.databind.ObjectMapper;
import config.TeamspeakConfig;
import reference.Reference;
import teamspeak.TibiaTeamspeak;
import tibia.TibiaTask;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TibiaMain {

    private static TeamspeakConfig config;

    public static void main(String[] args) {
        Reference.objectMapper = new ObjectMapper();

        TibiaTeamspeak tibiaTeamspeak = new TibiaTeamspeak();
        config = tibiaTeamspeak.teamspeakConfig;

        setupPolling(tibiaTeamspeak);
    }

    public static void setupPolling(TibiaTeamspeak tibiaTeamspeak) {
        long pollingTime = tibiaTeamspeak.teamspeakConfig.pollingTime;
        Date last = new Date();
        TibiaTask tibiaTask = new TibiaTask(tibiaTeamspeak);

        tibiaTask.poll();

        scheduleNextPoll(tibiaTask, last);
    }

    private static void scheduleNextPoll(TibiaTask tibiaTask, Date last) {
        Date now = new Date();
        long nextTime = last.getTime() + config.pollingTime;

        long difference = nextTime - now.getTime();

        if (difference < 0) {
            tibiaTask.poll();
            scheduleNextPoll(tibiaTask, now);
        } else {
            try {
                Thread.sleep(difference);
                tibiaTask.poll();
                scheduleNextPoll(tibiaTask, new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
