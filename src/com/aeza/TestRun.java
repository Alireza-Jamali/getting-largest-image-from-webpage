package com.aeza;

public class TestRun {
    
    String img,url;
    String domain, dName;
   
    public Info getServletInfo(String rawUrl) {
        
        return new InfoFetcher(rawUrl).fetch();
    }
    
    public static void main(String[] args) {
        
        
        
        TestRun im = new TestRun();
        
        //example:
        im.url = "http://www.digikala.com/Product/DKP-93276/Huawei-Honor-4X-Dual-SIM-Mobile-Phone/%DA%AF%D9%88%D8%B4%D9%8A-%D9%85%D9%88%D8%A8%D8%A7%D9%8A%D9%84-%D9%87%D9%88%D8%A2%D9%88%D9%8A-%D8%A2%D9%86%D8%B1-%D9%85%D8%AF%D9%84-4X-%D8%AF%D9%88-%D8%B3%D9%8A%D9%85-%DA%A9%D8%A7%D8%B1%D8%AA";
        
        Info info = im.getServletInfo(im.url);
        System.out.println(info.img);
        System.out.println(info.txt);
        
    }
}
