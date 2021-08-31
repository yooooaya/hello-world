package org.example.util;

public class UrlsUtil {
    private static String URIS="/oauth/toLogin,/oauth/login,/oauth/token";
    public static boolean hasAuth(String url){
        String [] split=URIS.split(",");
        for(String uri:split){
            if(url.contains(uri)){
                return true;
            }
        }
        return false;
    }
}
