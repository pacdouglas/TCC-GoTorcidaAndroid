package br.com.gotorcida.gotorcida.webservice;

import android.os.AsyncTask;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

public class PostRequest extends AsyncTask<String, Void, Boolean> {

    private String url;
    private Message message;

    public PostRequest(String url) {
        this.url = url + "/";
    }

    @Override
    protected Boolean doInBackground(String... params) {
        final JSONParser jParser = new JSONParser();

        ObjectMapper mapper = new ObjectMapper();
        JSONObject json = null;

        try {
            json = jParser.postJSONToURL(this.url, params[0]);
            this.message = mapper.readValue(json.toString(), Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean backgroundProcessingResult = false;

        if (message != null) {
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
