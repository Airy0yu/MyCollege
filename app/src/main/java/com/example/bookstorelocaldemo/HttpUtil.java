package com.example.bookstorelocaldemo;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static void sendOkhttpRequest(int num,String query,okhttp3.Callback callback){

        String url;
        switch (num){
            case 0:
                url="https://api.douban.com/v2/book/search?q="
                        +query+"&fields=id,title,author,image";
                break;
            case 1:
                url="https://api.douban.com/v2/book/"
                        +query+"?fields=author,pubdate,summary,price,title,image";
                break;
            default:url="https://api.douban.com/v2/book/search?q="
                    +query+"&fields=id,title,author,image";
        }
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);

    }
}
