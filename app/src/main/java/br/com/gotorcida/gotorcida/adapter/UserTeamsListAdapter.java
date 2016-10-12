package br.com.gotorcida.gotorcida.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;

public class UserTeamsListAdapter extends ArrayAdapter<JSONObject> {

    public UserTeamsListAdapter(Context context, ArrayList<JSONObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject json = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_user_teams_list_item, parent, false);
        }

        TextView sportId = (TextView) convertView.findViewById(R.id.userteams_textview_teamid);
        TextView sportName = (TextView) convertView.findViewById(R.id.userteams_textview_teamname);

        try {
            sportId.setText(json.getString("id"));
            sportName.setText(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
