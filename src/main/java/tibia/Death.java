package tibia;

import java.util.Date;
import java.util.List;

public class Death {

    public Date time;

    public String description;

    public Death() {}

    public Death(Date time, String description) {
        this.time = time;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.time + " : " + this.description;
    }


    public static Death getLatestDeathFromDeathList(List<Death> deaths) {
        Death latest = null;

        for (Death death : deaths) {
            if (latest == null) latest = death;

            if (death.time.getTime() > latest.time.getTime()) latest = death;
        }

        return latest;
    }
}
