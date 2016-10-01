package br.com.gotorcida.gotorcida.webservice;

import android.os.AsyncTask;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

public class GetRequest extends AsyncTask<String, Void, Boolean> {

    private String url;
    private Message message;

    public GetRequest(String url, String ... params) {
        this.url = url;

        if (params != null && params.length > 0){
            for (String parameters : params) {
                this.url = this.url + "/" + parameters;
            }
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        final JSONParser jParser = new JSONParser();

        ObjectMapper mapper = new ObjectMapper();
        JSONObject json = null;

        try {
            json = jParser.getJSONFromUrl(this.url);
            this.message = mapper.readValue(json.toString(), Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean backgroundProcessingResult = false;

        if (json != null) {
            try {
                backgroundProcessingResult = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            backgroundProcessingResult = false;
        }

        return backgroundProcessingResult;
    }

    public Message getMessage() {
        return this.message;
    }
}
