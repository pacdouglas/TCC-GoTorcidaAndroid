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

public class TeamNewsListAdapter extends ArrayAdapter<JSONObject> {

    public TeamNewsListAdapter(Context context, ArrayList<JSONObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject json = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_team_news_list_item, parent, false);
        }

        TextView newsId = (TextView) convertView.findViewById(R.id.team_textview_newsid);
        TextView newsDate = (TextView) convertView.findViewById(R.id.team_textview_newsdate);
        TextView newsTitle = (TextView) convertView.findViewById(R.id.team_textview_newstitle);

        try {
            newsId.setText(json.getString("id"));
            newsDate.setText(json.getString("formatedRegistrationDate"));
            newsTitle.setText(json.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
