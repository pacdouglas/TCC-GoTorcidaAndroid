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

        url = new URL(urlString);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "*/*");

        in = new BufferedInputStream(conn.getInputStream());
        response = IOUtils.toString(in, "UTF-8");

        JSONObject json = new JSONObject(response);

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

        JSONObject json = new JSONObject();
        JSONObject system = new JSONObject();
        JSONObject data = new JSONObject();

        system.put("code", conn.getResponseCode());
        system.put("message", conn.getResponseMessage());

        json.put("system", system);
        json.put("data", data);

        return json;
    }


    public JSONObject putJSONtoURL(String urlString, String params) throws Exception {

        System.out.println(urlString);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(params);
        osw.flush();
        osw.close();
        System.out.println(connection.getResponseCode());

       /* URL url = new URL(urlString);

        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
                httpCon.getOutputStream());
        out.write(params);
        out.close();
        httpCon.getOutputStream();*/

       /* OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(params);
        writer.flush();
        writer.close();
        os.close();
        conn.connect();*/

        return new JSONObject();
    }
}
