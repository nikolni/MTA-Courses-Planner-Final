package com.meidanet.htmlscraper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Entity
public class HtmlData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String htmlContent;
    //private String htmlContent;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHtmlContent() { return htmlContent; }
    public void setHtmlContent(String htmlContent) {

        this.htmlContent = convertHtmlToJson(htmlContent);
    }


    public static String convertHtmlToJson(String htmlFile) {
        // Parse HTML file using Jsoup
        Document doc = Jsoup.parse(htmlFile);

        // Extract data from HTML and build JSON object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject json = new JsonObject();
        json.addProperty("title", doc.title());
        json.add("headings", extractHeadings(doc));
        json.add("paragraphs", extractParagraphs(doc));

        return gson.toJson(json);
    }

    public static JsonArray extractHeadings(Document doc) {
        Elements headings = doc.select("h1, h2, h3, h4, h5, h6");
        JsonArray jsonArray = new JsonArray();
        for (Element heading : headings) {
            jsonArray.add(heading.text());
        }
        return jsonArray;
    }

    public static JsonArray extractParagraphs(Document doc) {
        Elements paragraphs = doc.select("p");
        JsonArray jsonArray = new JsonArray();
        for (Element paragraph : paragraphs) {
            jsonArray.add(paragraph.text());
        }
        return jsonArray;
    }
}
