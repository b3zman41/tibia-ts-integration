package tibia;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jsoup.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TibiaPlayer {

    private String html;
    private Document document;

    private String name, sex, vocation, world, residence;

    private Integer level, achievementPoints;

    private ArrayList<Death> deaths = new ArrayList<>();

    private Date lastLogin;

    public TibiaPlayer(String username) throws UnirestException {
        String html = Unirest.get("http://www.tibia.com/community/")
                .queryString("subtopic", "characters")
                .queryString("name", username)
                .asString()
                .getBody();

        this.setupFromHTML(html);
    }

    public void setupFromHTML(String html) {
        this.html = html;
        this.document = Jsoup.parse(html);

        this.parse();
    }

    private void parse() {
        this.parseName();
        this.parseSex();
        this.parseVocation();
        this.parseLevel();
        this.parseAchievementPoints();
        this.parseWorld();
        this.parseResidence();
        this.parseDeaths();
    }

    private void parseName() {
        this.name = TibiaParserUtil.getElementForAssociatedData(document, "Name:").text();
    }

    private void parseSex() {
        this.sex = TibiaParserUtil.getElementForAssociatedData(document, "Sex:").text();
    }

    private void parseVocation() {
        this.vocation = TibiaParserUtil.getElementForAssociatedData(document, "Vocation:").text();
    }

    private void parseLevel() {
        this.level = Integer.valueOf(TibiaParserUtil.getElementForAssociatedData(document, "Level:").text());
    }

    private void parseAchievementPoints() {
        this.achievementPoints = Integer.valueOf(TibiaParserUtil.getElementForAssociatedData(document, "Achievement Points:").text());
    }

    private void parseWorld() {
        this.world = TibiaParserUtil.getElementForAssociatedData(document, "World:").text();
    }

    private void parseResidence() {
        this.residence = TibiaParserUtil.getElementForAssociatedData(document, "Residence:").text();
    }

    public void parseDeaths() {
        Element element = this.document.select("b:contains(Character Deaths)").first();

        if (element != null) {
            Elements elements = element.parent().parent().parent().children();

            elements.remove(elements.first());

            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss z");

            elements.forEach(td -> {
                Death death = new Death();

                try {
                    death.time = formatter.parse(td.children().first().text().replace("\u00a0", " "));
                    death.description = td.child(1).text();

                    this.deaths.add(death);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getVocation() {
        return vocation;
    }

    public String getWorld() {
        return world;
    }

    public String getResidence() {
        return residence;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getAcheivementPoints() {
        return achievementPoints;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public ArrayList<Death> getDeaths() {
        return this.deaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TibiaPlayer that = (TibiaPlayer) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
