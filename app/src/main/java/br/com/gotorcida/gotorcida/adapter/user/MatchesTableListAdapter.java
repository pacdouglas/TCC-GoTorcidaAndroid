package br.com.gotorcida.gotorcida.adapter.user;

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

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class MatchesTableListAdapter extends RecyclerView.Adapter {

    List<JSONObject> events;
    Context context;
    MatchesTableListHolder holder;

    public MatchesTableListAdapter(List<JSONObject> events, Context context){
        this.events = events;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_matches_table, parent, false);
        holder = new MatchesTableListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        JSONObject data = events.get(position);
        try {
            holder.firstTeamName.setText(data.getString("firstTeamName"));
            holder.firstTeamScore.setText(data.getString("firstTeamScore"));
            holder.secondTeamName.setText(data.getString("secondTeamName"));
            holder.secondTeamScore.setText(data.getString("secondTeamScore"));
            holder.eventDate.setText(data.getString("eventDate"));

            Glide.with(context).load(URL_IMAGES_BASE + data.getString("firstTeamImageURL")+".png").into(holder.firstTeamLogo);
            Glide.with(context).load(URL_IMAGES_BASE + data.getString("secondTeamImageURL")+".png").into(holder.secondTeamLogo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}

