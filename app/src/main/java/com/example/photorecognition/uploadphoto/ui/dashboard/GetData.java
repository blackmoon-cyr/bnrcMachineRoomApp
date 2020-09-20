package com.example.photorecognition.uploadphoto.ui.dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public  class GetData {

    public static String getResponse(String path) throws IOException {
        BufferedReader reader=null;
        System.out.println(path);
        URL url = new URL(path);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setReadTimeout(500);

        conn.setRequestMethod("GET");

        InputStream in=conn.getInputStream();
        reader = new BufferedReader( new InputStreamReader(in));
        StringBuilder response =new StringBuilder();
        String line;
        while ((line=reader.readLine())!=null){
            response.append(line);
        }
        return response.toString();
    }
}
