package br.com.gotorcida.gotorcida.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class UserTeamsListAdapter extends ArrayAdapter<JSONObject> {

    public UserTeamsListAdapter(Context context, ArrayList<JSONObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject json = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_user_teams, parent, false);
        }

        TextView teamId = (TextView) convertView.findViewById(R.id.userteams_textview_teamid);
        TextView teamName = (TextView) convertView.findViewById(R.id.userteams_textview_teamname);
        ImageView teamLogo = (ImageView) convertView.findViewById(R.id.userteams_imageview_imageteam);

        try {
            teamId.setText(json.getString("id"));
            teamName.setText(json.getString("name"));
            Glide.with(getContext()).load(URL_IMAGES_BASE + json.getString("urlImage")+".png").into(teamLogo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
