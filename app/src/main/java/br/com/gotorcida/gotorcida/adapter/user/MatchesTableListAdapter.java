package br.com.gotorcida.gotorcida.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class MatchesTableListAdapter extends RecyclerView.Adapter {

    List<JSONObject> events;
    Context context;
    JSONObject data;
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
        data = events.get(position);
        holder.setIsRecyclable(false);
        try {
            holder.firstTeamName.setText(data.getString("firstTeamName"));

            String aux = data.getString("firstTeamScore");
            if(aux.equals("-1")){
                holder.firstTeamScore.setText("?");
            }else{
                holder.firstTeamScore.setText(aux);
            }
            aux = data.getString("secondTeamScore");
            if(aux.equals("-1")){
                holder.secondTeamScore.setText("?");
            }else{
                holder.secondTeamScore.setText(aux);
            }

            holder.secondTeamName.setText(data.getString("secondTeamName"));
            holder.eventDate.setText(data.getString("eventDate") + " - " + data.getString("time"));
            holder.eventCity.setText(data.getString("location"));

            Glide.with(context).load(URL_IMAGES_BASE + data.getString("firstTeamImageURL")+".png").into(holder.firstTeamLogo);
            Glide.with(context).load(URL_IMAGES_BASE + data.getString("secondTeamImageURL")+".png").into(holder.secondTeamLogo);
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(int pos) {

                }
            });
            holder.eventId.setText(data.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}

