package br.com.gotorcida.gotorcida.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;

/**
 * Created by dougl on 19/09/2016.
 */

public class SportAdapter extends ArrayAdapter<JSONObject> {

    public SportAdapter(Context context, ArrayList<JSONObject> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        JSONObject json = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_sports, parent, false);
        }
        // Lookup view for data population
        TextView sportName = (TextView) convertView.findViewById(R.id.name_sport);
        // Populate the data into the template view using the data object
        try {
            sportName.setText(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
