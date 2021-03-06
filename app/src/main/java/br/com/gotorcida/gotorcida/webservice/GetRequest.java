package br.com.gotorcida.gotorcida.webservice;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

public class GetRequest {

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

    public boolean execute(String... params) {
        final JSONParser jParser = new JSONParser();

        ObjectMapper mapper = new ObjectMapper();
        JSONObject json = null;

        try {
            System.out.println("**************************************");
            System.out.println("[GET] CALLED URL : " + this.url);
            System.out.println("**************************************");
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
