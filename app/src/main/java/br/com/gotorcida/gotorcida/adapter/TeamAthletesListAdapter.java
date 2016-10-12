package br.com.gotorcida.gotorcida.adapter;

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

public class TeamAthletesListAdapter extends ArrayAdapter<JSONObject> {

    public TeamAthletesListAdapter(Context context, ArrayList<JSONObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject json = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_team_athletes_list_item, parent, false);
        }

        TextView athleteId = (TextView) convertView.findViewById(R.id.team_textview_athleteid);
        TextView athleteName = (TextView) convertView.findViewById(R.id.team_textview_athletename);

        try {
            athleteId.setText(json.getString("id"));
            athleteName.setText(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
