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

public class TeamsListAdapter extends RecyclerView.Adapter {

    List<JSONObject> leagues;
    Context context;

    public TeamsListAdapter(List<JSONObject> leagues, Context context){
        this.leagues = leagues;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_teams, parent, false);
        TeamsListHolder holder = new TeamsListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        JSONObject data = leagues.get(position);

        TeamsListHolder holder = (TeamsListHolder) viewHolder;
        try {
            holder.leagueID.setText(data.getString("id"));
            holder.leagueName.setText(data.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return leagues.size();
    }

}

