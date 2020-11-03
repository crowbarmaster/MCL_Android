package com.example.myapplication;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Net {

    public Net (){}

    public void PushSQL(HashMap<String, String> map) {
        String response;
        String apiPath = "http://69.207.170.153:8237/restsrv/RestController.php?";
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put("HTTP_ACCEPT", "application/json");
        postDataParams.putAll(map);
        HttpConnectionService service = new HttpConnectionService();
        response = service.sendRequest(apiPath, postDataParams);
        if (response.equals("1")) {
            Log.d("HTTP response", "HTTP cmd passed!");
        }
    }

    public HashMap<String, HashMap<String, String>> PullSQL(HashMap<String, String> map) {
        Log.d("PullSQL", "Now pulling SQL");
        String response;
        String[] split;
        String apiPath = "http://69.207.170.153:8237/restsrv/RestController.php?";
        HashMap<String, String> postDataParams = new HashMap<>();
        HashMap<String, HashMap<String, String>> map2 = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        String[] tmp;
        String[] tmp2;
        postDataParams.put("HTTP_ACCEPT", "application/json");
        postDataParams.putAll(map);
        HttpConnectionService service = new HttpConnectionService();
        response = service.sendRequest(apiPath, postDataParams);
        if(!response.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                jsonArray = jsonObject.getJSONArray("output");
            } catch (JSONException e) {
                e.printStackTrace();
            }
         //   Log.d("getJSONArray out", "JSON Array was: " + jsonArray.toString());
            if (jsonArray.toString().contains("},{")) {
                split = jsonArray.toString().replace("},{", "~").split("~");
            } else {
                split = new String[1];
                split[0] = jsonArray.toString();
              //  Log.d("get single JSONArray out", "JSON Array was: " + split[0]);
            }
            for (String s : split) {
                tmp = s.replace("[", "").replace("]", "").replace("{", "").replace("}", "").split(",");
                if (tmp.length > 0 && tmp[0].length() > 0) {
                    String id = tmp[0].replace("\"", "").split(":")[1];
                    map2.put(id, new HashMap<String, String>());
                    for (int x = 0; x < tmp.length; x++) {
                        if (x == 0) {
                            //ignore the first line, its taken care of later
                        } else {
                       //     Log.d("Net", "LOG: " + tmp[x]);
                            if (!tmp[x].equals("\"notes\":\"\"")) {
                                tmp2 = tmp[x].replace("\"", "").split(":");
                                if (tmp2.length > 0 && !tmp2[1].equals("\"notes\":\"\"")) {
                                    map2.get(id).put(tmp2[0], tmp2[1]);
                                   // Log.d("map2 put", "put was: " + tmp2[0] + " " + tmp2[1] + " split len: " + split.length + " " + tmp.length);
                                }
                            }
                        }
                    }
                }
            }
        }
        return map2;
    }


        int serverResponseCode = 0;
        String upLoadServerUri = null;

         public String doUpload (File file, String destPath){
             StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
             StrictMode.setThreadPolicy(policy);
             upLoadServerUri = "http://69.207.170.153:8237/UploadToServer.php?";

             HttpURLConnection conn;
             String lineEnd = "\r\n";
             String twoHyphens = "--";
             String boundary = "*****";
             int bytesRead, bytesAvailable, bufferSize;
             byte[] buffer;
             int maxBufferSize = 1024 * 1024;
             File sourceFile = file;

             if (!sourceFile.isFile()) {

                 Log.e("uploadFile", "Source File not exist :"
                         + sourceFile.getAbsolutePath());
             }
             else
             {
                 try {

                     // open a URL connection to the Servlet
                     FileInputStream fileInputStream = new FileInputStream(sourceFile);
                     String dest = URLEncoder.encode(destPath, "UTF-8");
                     URL url = new URL(upLoadServerUri);
                     // Open a HTTP  connection to  the URL
                     conn = (HttpURLConnection) url.openConnection();
                     conn.setDoInput(true); // Allow Inputs
                     conn.setDoOutput(true); // Allow Outputs
                     conn.setUseCaches(false); // Don't use a Cached Copy
                     conn.setRequestMethod("POST");
                     conn.setRequestProperty("Connection", "Keep-Alive");
                     conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                     conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                     conn.setRequestProperty("uploaded_file", sourceFile.getAbsolutePath());

                     byte[] boundaryBytes =
                             ("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8);

                     try(OutputStream out = conn.getOutputStream()) {
                         // Send our header (thx Algoman)
                         out.write(boundaryBytes);

                         sendField(out, dest);

                         out.write(boundaryBytes);

                         out.write(("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFile.getAbsolutePath() + "\"" + lineEnd).getBytes(StandardCharsets.UTF_8));

                         out.write(lineEnd.getBytes(StandardCharsets.UTF_8));

                         // create a buffer of  maximum size
                         bytesAvailable = fileInputStream.available();

                         bufferSize = Math.min(bytesAvailable, maxBufferSize);
                         buffer = new byte[bufferSize];

                         // read file and write it into form...
                         bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                         while (bytesRead > 0) {

                             out.write(buffer, 0, bufferSize);
                             bytesAvailable = fileInputStream.available();
                             bufferSize = Math.min(bytesAvailable, maxBufferSize);
                             bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                         }

                         // send multipart form data necesssary after file data...
                         out.write(lineEnd.getBytes(StandardCharsets.UTF_8));
                         out.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes(StandardCharsets.UTF_8));
                     }

                     // Responses from the server (code and message)
                     serverResponseCode = conn.getResponseCode();
                     String serverResponseMessage = conn.getResponseMessage();

                     Log.i("uploadFile", "HTTP Response is : "
                             + serverResponseMessage + ": " + serverResponseCode);

                     if(serverResponseCode == 200){
                         Log.i("File_good", "File uploaded successfully!");
                     }

                     //close the streams //
                     fileInputStream.close();
                     conn.disconnect();

                 } catch (MalformedURLException ex) {
                     ex.printStackTrace();

                     Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                 }catch (FileNotFoundException ex) {

                     ex.printStackTrace();

                 } catch (final Exception e) {

                     e.printStackTrace();

                     Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
                 }
                 Log.e("Srv_response", String.valueOf(serverResponseCode));
                 //Toast.makeText(context, "Upload finished, reply: "+serverResponseCode, Toast.LENGTH_LONG).show();
                 return String.valueOf(serverResponseCode);
             } // End else block
             return "error";
         }

         private void sendField(OutputStream out, String field) throws IOException {
        String o = "Content-Disposition: form-data; name=\""
                +  URLEncoder.encode("path","UTF-8") + "\"\r\n\r\n";
        out.write(o.getBytes(StandardCharsets.UTF_8));
        out.write(URLEncoder.encode(field,"UTF-8").getBytes(StandardCharsets.UTF_8));
        out.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }
}