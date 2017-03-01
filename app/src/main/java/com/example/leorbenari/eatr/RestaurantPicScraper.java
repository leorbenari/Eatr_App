package com.example.leorbenari.eatr;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class RestaurantPicScraper {

    private static String getOriginal(String url) {
        int i;
        for (i = url.length()-1; i >= 0; i--){
            if (url.charAt(i) == '/') {
                break;

            }
        }
        return url.substring(0, i+1) + "o.jpg";
    }

    public static List<String > getPics(String html){
        Document doc = Jsoup.parse(html);
        Elements images = doc.select("div.photobox > img");

        List<String> urls = new ArrayList<>();

        String src;

        for (Element image : images) {
            src = image.attr("src");
            if (src != null && src.equals("")) {
                urls.add(getOriginal(src));
            }
        }
        return urls;
    }




}
