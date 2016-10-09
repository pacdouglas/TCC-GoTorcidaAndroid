package br.com.gotorcida.gotorcida.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.SelectSportActivity;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

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
            holder.sportID.setText(data.getString("id"));
            holder.sportName.setText(data.getString("name"));
            Glide.with(context).load(URL_IMAGES_BASE + data.getString("urlImage")+".png").into(holder.iconSport);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

}

