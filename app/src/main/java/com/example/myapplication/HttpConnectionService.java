package com.example.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnectionService {
    StringBuilder response = new StringBuilder();
    URL url;
    HttpURLConnection conn = null;
    int responseCode = 0;

    public String sendRequest(String path, HashMap<String, String> params) {
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("connection", "close");//Jellybean is having an issue on "Keep-Alive" connections
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (IOException ioe) {
            Log.d("HttpConnectionService", "Problem in getting connection.");
            ioe.printStackTrace();
        } catch (Exception e) {
            Log.d("HttpConnectionService", "Problem in getting connection. Safegaurd catch.");
            e.printStackTrace();
        }

        OutputStream os;
        try {
            if (null != conn) {
                os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();
                responseCode = conn.getResponseCode();
            }
        } catch (IOException e) {
            Log.d("HttpConnectionService", "Problem in getting outputstream and passing parameter.");
            e.printStackTrace();
        }

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            Log.d("HttpConnectionService", "Connection success to path: " + path);
            StringBuilder line = new StringBuilder();
            BufferedReader br = null;

            //getting the reader instance from connection
            try {
                if (null != conn) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                }
            } catch (IOException e) {
                Log.d("HttpConnectionService", "Problem with opening reader.");
                e.printStackTrace();
            }

            //reading the response from stream
            try {
                if (null != br) {
                    while ((response.append(br.readLine())) != null) {
                        Log.d("HttpConnectionService", "output: " + line);
                    }
                }
            } catch (IOException e) {
                response.append("");
                Log.d("HttpConnectionService", "Problem in extracting the result.");
                e.printStackTrace();
            }
        } else {
            response.append("");
        }
        Log.d("HttpConnectionService", "Response was: " + response);
        return response.toString();
    }

    private String getPostDataString(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return result.toString();
    }
}
