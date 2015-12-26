import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tibia.TibiaParserUtil;
import tibia.TibiaPlayer;
import tibia.TibiaWorld;

import java.io.IOException;

public class TestPlayerWorldIntegration {

    String player = "Alitilis";

    TibiaPlayer tibiaPlayer;

    @BeforeClass
    public void setupWorldAndPlayer() throws UnirestException {
        this.tibiaPlayer = new TibiaPlayer(player);
    }

    @Test
    public void testPlayerBelongsToWorld() throws IOException, UnirestException {
        TibiaWorld tibiaWorld = new TibiaWorld(this.tibiaPlayer.getWorld());

        Assert.assertTrue(tibiaWorld.isPlayerOnline(player));
    }

}
