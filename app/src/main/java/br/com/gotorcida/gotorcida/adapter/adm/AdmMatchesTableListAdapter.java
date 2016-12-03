package br.com.gotorcida.gotorcida.adapter.adm;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class AdmMatchesTableListAdapter extends RecyclerView.Adapter {

    private final Fragment fragment;
    private final FragmentManager fragmentManager;
    List<JSONObject> events;
    Context context;
    JSONObject data;
    AdmMatchesTableListHolder holder;

    public AdmMatchesTableListAdapter(List<JSONObject> events, Context context, FragmentManager fragmentManager, Fragment fragment){
        this.events = events;
        this.context = context;
        this.fragment = fragment;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_matches_table, parent, false);

        holder = new AdmMatchesTableListHolder(fragment, fragmentManager, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        data = events.get(position);
        holder.setIsRecyclable(false);
        try {
            holder.firstTeamName.setText(data.getString("firstTeamName"));
            holder.firstTeamScore.setText(data.getString("firstTeamScore"));
            holder.secondTeamName.setText(data.getString("secondTeamName"));
            holder.secondTeamScore.setText(data.getString("secondTeamScore"));
            holder.eventDate.setText(data.getString("eventDate"));


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

