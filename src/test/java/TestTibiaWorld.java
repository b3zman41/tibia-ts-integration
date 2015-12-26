import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tibia.TibiaWorld;

import java.io.IOException;

public class TestTibiaWorld {

    String world = "Amera";
    TibiaWorld tibiaWorld;

    @BeforeClass
    public void setupWorld() throws UnirestException, IOException {
        this.tibiaWorld = new TibiaWorld(world);
    }

    @Test
    public void testTibiaWorldSize() {
        Assert.assertTrue(this.tibiaWorld.players.size() > 10);
    }

}
