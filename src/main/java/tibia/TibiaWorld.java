package tibia;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TibiaWorld {

    private String html;
    private Document document;

    public ArrayList<String> players = new ArrayList<>();

    public TibiaWorld(String world) throws UnirestException, IOException {
        this.document = Jsoup.connect("http://www.tibia.com/community/?subtopic=worlds&world=" + world).get();

        this.parse();
    }

    public boolean isPlayerOnline(String player) {
        return this.players.contains(player);
    }

    public void parse() {
        this.parsePlayers();
    }

    public void parsePlayers() {
        Elements elements = this.document.select(".Table2 table>tbody>tr.Odd,.Even");

        elements.stream().forEach(element -> {
            this.players.add(element.child(0).text().replace("\u00a0", " "));
        });
    }



}
