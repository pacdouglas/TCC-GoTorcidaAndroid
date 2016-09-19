package br.com.gotorcida.gotorcida.webservice;

import android.os.AsyncTask;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

public class GetRequest extends AsyncTask<String, Void, Boolean> {

    private String url;
    private String params;
    private Message message;

    public GetRequest(String url) {
        this.url = url;
        this.params = "";
    }

    @Override
    protected Boolean doInBackground(String... params) {
        final JSONParser jParser = new JSONParser();

        for (int i = 0; i <= params.length - 1; i++) {
            this.params += params[i] + "/";
        }
        if(this.params != null && !this.params.isEmpty())
            this.params = this.params.substring(0, this.params.length() - 1);

        ObjectMapper mapper = new ObjectMapper();
        JSONObject json = null;

        try {
            json = jParser.getJSONFromUrl(this.url + this.params);
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
