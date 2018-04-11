package com.aeza;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LastResort {

    int av;
    int sumav = 0;
    String rawUrl;

    LastResort(String rawUrl) {

        this.rawUrl = rawUrl;
    }

    public String fetch() {

        String img = null;
        Document doc = null;
        try {
           
            doc = Jsoup
                    .connect(rawUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                    .timeout(10000)
                    .maxBodySize(0)
                    .get();

            Elements media = doc.select("[src]");

            for (Element src : media) {
                try {

                    String wi = src.attr("width");
                    String he = src.attr("height");

                    wi = wi.replace("px", "");
                    he = he.replace("px", "");

                    int width = Integer.valueOf(wi);
                    int height = Integer.valueOf(he);

                    av = (width*height) / 2;

                    if (src.tagName().equals("img") & av > sumav) {

                        sumav = av;
                        img = src.attr("abs:src");
                    }
                } catch (Exception e) {

                    System.out.println("DEVMSG: could not find width or height, so valueOf returned NumberFormatException");
                    Logger.getLogger(LastResort.class.getName()).log(Level.SEVERE, null, e);

                    try {
                        Element e1 = doc.head().select("link[href~=.*\\.(ico|png)]").first();
                        img = e1.attr("href");


                        if (img == null || img.equals("")) {
                            Element e2 = doc.head().select("meta[itemprop=image]").first();
                            img = e2.attr("itemprop");
                        }

                    } catch (Exception eh) {

                        System.out.println("DEVMSG: LastResort Exception where heigh and width were not available and none other methods worked so far to fetch a suitable image so we sufficed to give google's favicon method... DEV:OUT");
                    }//nested try inside number format exception catch, we catch it to let other modules work
                    
                }//number format exception catch

                //we repeat this module if the last module gives exception or not
                if (img == null || img.equals("")) {
                    try {

                        Element e1 = doc.head().select("link[href~=.*\\.(ico|png)]").first();
                        img = e1.attr("href");

                        if (img == null || img.equals("")) {
                            Element e2 = doc.head().select("meta[itemprop=image]").first();
                            img = e2.attr("itemprop");
                        }

                    } catch (Exception eh) {

                        System.out.println("DEVMSG: LastResort Exception where heigh and width were not available and none other methods worked so far to fetch a suitable image so we suffced to give google's favicon method... DEVOUT");
                    }
                }
            }
        
        } catch (Exception ex) {

            System.out.println("LastRast could not connect too.");
            Logger.getLogger(LastResort.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img;
    }
}
