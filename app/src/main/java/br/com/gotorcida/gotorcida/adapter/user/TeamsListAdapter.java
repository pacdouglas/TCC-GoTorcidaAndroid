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

public class TeamsListAdapter extends RecyclerView.Adapter {

    List<JSONObject> leagues;
    Context context;
    TeamsListHolder holder;

    public TeamsListAdapter(List<JSONObject> leagues, Context context){
        this.leagues = leagues;
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
        JSONObject data = leagues.get(position);
        holder.setIsRecyclable(false);

        TeamsListHolder holder = (TeamsListHolder) viewHolder;
        try {
            holder.teamID.setText(data.getString("id"));
            holder.teamName.setText(data.getString("name"));

            Glide.with(context).load(URL_IMAGES_BASE + data.getString("urlImage")+".png").into(holder.teamImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return leagues.size();
    }

}

