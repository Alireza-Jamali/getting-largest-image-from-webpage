package com.aeza;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


class InfoFetcher{
    
    String rawUrl;
    String txt = "";
    String img = null;
    
    InfoFetcher(String rawUrl) {
    
        this.rawUrl = rawUrl;
    }
    
    public Info fetch() {
        
        /*Layer: 1*/
        parsePageHeaderInfo(rawUrl);        
        
            /*Layer: 2*/
            if(img == null || img.equals("")) {            
                System.out.println("DEVMSG: InfoFetcher faced a faultCased therefore lastResort initiated");
                Logger.getLogger(InfoFetcher.class.getName()).log(Level.SEVERE, "InfoFetcher fail log");
                img = new LastResort(rawUrl).fetch();

                /*Layer: 3*/
                if (img == null || img.equals("")) {
                    System.out.println("DEVMSG: LastResort faced a faultCase therefore google-fetched favicon returned");
                    img = "http://www.google.com/s2/favicons?domain_url=" + rawUrl;
                }
            }
        
                    /*layer: 4*/
                    if (  ! (img.contains("www") || img.contains("http"))   ){
                        
                        img = rawUrl.replaceAll("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?", "$1$3") + img;
                    }
            
        return new Info(img , txt);
    }

    public void parsePageHeaderInfo(String urlStr) {
        
        
        Document doc;
        try{
            
            doc = Jsoup
                    .connect(urlStr)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                    .timeout(10000)
                    .maxBodySize(0)
                    .get();
            
            String text;
            Elements metaOgTitle = doc.select("meta[property=og:title]");            
            if(metaOgTitle != null) {                
                text = metaOgTitle.attr("content");
            } else {
                text = doc.title();
            }
            
            if (text != null && !"".equals(text)) {
                this.txt = text;
            }

            String imageUrl;
            Elements metaOgImage = doc.select("meta[property=og:image]");            
            if(metaOgImage != null) {                
                imageUrl = metaOgImage.attr("content");
                
                if (imageUrl != null && !"".equals(imageUrl)) {
                    
                    img = imageUrl;
                }
            }
            
        } catch (Exception e) {

            System.out.println("DEVMSG: parsePageHeaderInfo inside InfoFetcher could not connect (Jsoup.connect) to given URL");
            Logger.getLogger(InfoFetcher.class.getName()).log(Level.SEVERE, null, e);
            
        }
       
    }
}