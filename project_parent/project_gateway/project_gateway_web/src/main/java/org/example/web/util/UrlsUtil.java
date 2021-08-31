package org.example.web.util;

public class UrlsUtil {
    private static String URIS="/api/oauth/toLogin,/api/oauth/login,/oauth/token";
    public static boolean hasAuth(String url){
        String [] split=URIS.split(",");
        for(String uri:split){
            if(url.equals(uri)){
                return true;
            }
        }
        return false;
    }
}
