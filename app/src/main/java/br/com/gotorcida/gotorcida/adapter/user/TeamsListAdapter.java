package br.com.gotorcida.gotorcida.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.SelectTeamActivity;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class TeamsListAdapter extends RecyclerView.Adapter {

    List<JSONObject> teams;
    Context context;
    TeamsListHolder holder;

    public TeamsListAdapter(List<JSONObject> teams, Context context){
        this.teams = teams;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_select_team, parent, false);
        holder = new TeamsListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int pos = position;
        final JSONObject data = teams.get(position);
        holder.setIsRecyclable(false);
        try {
            JSONObject sport = data.getJSONObject("sport");
            holder.sportName.setText(sport.getString("name"));

            holder.teamID.setText(data.getString("id"));
            holder.teamName.setText(data.getString("name"));

            holder.teamName.setChecked(data.getBoolean("isChecked"));
            holder.teamName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        data.put("isChecked", isChecked);
                        SelectTeamActivity.setArrayListChecked(pos, isChecked);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

            }
        });
        try {
            Glide.with(context).load(URL_IMAGES_BASE + data.getString("urlImage")+".png").error(R.drawable.ic_team_no_logo).into(holder.teamImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

}

