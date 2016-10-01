package br.com.gotorcida.gotorcida.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.DownloadImageTask;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class SportAdapter extends ArrayAdapter<JSONObject> {

    SparseBooleanArray mCheckStates;

    public SportAdapter(Context context, ArrayList<JSONObject> sports) {
        super(context, 0, sports);
        mCheckStates = new SparseBooleanArray(sports.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject json = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_sports, parent, false);
        }

        TextView sportID = (TextView) convertView.findViewById(R.id.selectsport_textview_sportid);
        CheckBox sportName = (CheckBox) convertView.findViewById(R.id.selectsport_checkbox_sportname);
        try {
            sportName.setText(json.getString("name"));
            sportID.setText(json.getString("id"));
            //new DownloadImageTask((ImageView)convertView.findViewById(R.id.img_sport)).execute(URL_IMAGES_BASE+json.getString("description")+".png");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
