import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tibia.TibiaPlayer;

public class TestTibiaPlayer {

    private TibiaPlayer parser;

    private String username = "Havoc Second";

    @BeforeClass
    public void setupParser() throws UnirestException {
        this.parser = new TibiaPlayer(username);
    }

    @Test
    public void testCorrectName() {
        Assert.assertEquals(this.parser.getName(), username);
    }

    @Test
    public void testCorrectSex() {
        Assert.assertEquals("male", this.parser.getSex());
    }

    @Test
    public void testCorrectVocation() {
        Assert.assertEquals("Elite Knight", this.parser.getVocation());
    }

    @Test
    public void testCorrectLevel() {
        Assert.assertEquals(85, (int) this.parser.getLevel());
    }

    @Test
    public void testCorrectAchievementPoints() {
        Assert.assertEquals(58, (int) this.parser.getAcheivementPoints());
    }

    @Test
    public void testCorrectWorld() {
        Assert.assertEquals("Amera", this.parser.getWorld());
    }

    @Test
    public void testCorrectResidence() {
        Assert.assertEquals("Carlin", this.parser.getResidence());
    }

}
