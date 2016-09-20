package br.com.gotorcida.gotorcida.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.DownloadImageTask;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;



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
        CheckBox sportName = (CheckBox) convertView.findViewById(R.id.name_sport);

        // Populate the data into the template view using the data object
        try {
            sportName.setText(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            new DownloadImageTask((ImageView)convertView.findViewById(R.id.img_sport)).execute(URL_IMAGES_BASE+json.getString("description")+".png");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
