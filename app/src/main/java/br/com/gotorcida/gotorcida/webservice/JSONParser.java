package br.com.gotorcida.gotorcida.webservice;


import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

    public JSONObject getJSONFromUrl(String urlString) throws Exception {
        URL url;
        HttpURLConnection conn;
        InputStream in;
        JSONArray jsonArray;
        String response;
        JSONObject json;

        url = new URL(urlString);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "*/*");

        try{

            in = new BufferedInputStream(conn.getInputStream());
            response = IOUtils.toString(in, "UTF-8");
            json = new JSONObject(response);
        }catch(Exception e){
            return null;
        }
        return json;
    }

    public JSONObject postJSONToURL(String urlString, String params) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(params);
        writer.flush();
        writer.close();
        os.close();
        conn.connect();

        InputStream in = new BufferedInputStream(conn.getInputStream());
        String response = IOUtils.toString(in, "UTF-8");
        JSONObject json = new JSONObject(response);

        return json;
    }
}