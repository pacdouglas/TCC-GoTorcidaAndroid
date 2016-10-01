package br.com.gotorcida.gotorcida.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import br.com.gotorcida.gotorcida.R;

public class SportsListAdapter extends RecyclerView.Adapter {

    List<JSONObject> sports;
    Context context;

    public SportsListAdapter(List<JSONObject> sports, Context context){
        this.sports = sports;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_sports, parent, false);
        SportsListHolder holder = new SportsListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        JSONObject data = sports.get(position);

        SportsListHolder holder = (SportsListHolder) viewHolder;
        try {
            holder.sport.setText(data.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

   /* @Override
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
*/
}

