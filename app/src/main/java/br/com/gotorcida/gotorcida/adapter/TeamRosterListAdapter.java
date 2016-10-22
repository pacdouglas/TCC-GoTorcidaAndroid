package br.com.gotorcida.gotorcida.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import br.com.gotorcida.gotorcida.R;

public class TeamRosterListAdapter extends RecyclerView.Adapter {

    List<JSONObject> athlete;
    Context context;
    JSONObject data;
    public TeamRosterListAdapter(List<JSONObject> athlete, Context context){
        this.athlete = athlete;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_team_roster, parent, false);
        TeamRosterListHolder holder = new TeamRosterListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        data = athlete.get(position);

        TeamRosterListHolder holder = (TeamRosterListHolder) viewHolder;
        try {
            holder.nameAthlete.setText(data.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return athlete.size();
    }

}

