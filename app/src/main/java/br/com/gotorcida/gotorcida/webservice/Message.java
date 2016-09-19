package br.com.gotorcida.gotorcida.webservice;

import org.json.JSONException;
import org.json.JSONObject;

public class Message extends JSONObject {
    private final JSONObject system;
    private final JSONObject data;

    public Message() {
        system = new JSONObject();
        data = new JSONObject();
    }

    public String getData(String key) {
        if (!data.has(key)) {
            //throw new MessageKeyNotFoundException("Specified value not found at message.");
        }
        String value = null;

        try {
            value = getString(key);
        }catch (JSONException e){

        }
        return value;
    }

    public void addData(String key, Object value) {
        try{
            data.put(key, value);
        }catch(JSONException e) {

        }
    }

    public void setResponse(Integer code, String message) {
        try{
            system.put("code", code);
            system.put("message", message);
        }
        catch(JSONException e){

        }

    }

    public JSONObject getSystem() {
        return system;
    }

    public JSONObject getData() {
        return data;
    }

    public JSONObject getResponse() {
        JSONObject response = new JSONObject();
        try{
            response.put("code", system.get("code"));
            response.put("message", system.get("message"));
        }catch(JSONException e) {

        }
        return response;
    }

    public String toJSON() {
        JSONObject object = new JSONObject();
        try{
            object.put("system", system.toString());
            object.put("data", data.toString());
        }catch(JSONException e){

        }
        return object.toString();
    }

}
