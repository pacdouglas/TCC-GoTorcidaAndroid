package br.com.gotorcida.gotorcida.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.SelectSportActivity;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;

public class SportsListAdapter extends RecyclerView.Adapter {

    List<JSONObject> sports;
    Context context;
    SportsListHolder holder;

    public SportsListAdapter(List<JSONObject> sports, Context context){
        this.sports = sports;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_select_sport, parent, false);
        holder = new SportsListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int pos = position;

        final JSONObject data = sports.get(position);
        holder.setIsRecyclable(false);

        try {
            holder.sportID.setText(data.getString("id"));
            holder.sportName.setText(data.getString("name"));
            holder.sportName.setChecked(data.getBoolean("isChecked"));
            holder.sportName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        data.put("isChecked", isChecked);
                        SelectSportActivity.setArrayListChecked(pos, isChecked);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Glide.with(context).load(URL_IMAGES_BASE + data.getString("urlImage")+".png").error(R.drawable.ic_team_no_logo).into(holder.iconSport);

            int auxCountTeams = data.getInt("activeTeams");

            if(auxCountTeams >= 2){
                holder.teamCount.setText(auxCountTeams+" equipes ativas");
            }else if(auxCountTeams == 1){
                holder.teamCount.setText(auxCountTeams+" equipe ativa");
            }else{
                holder.teamCount.setText("Nenhuma equipe ativa :(");
            }

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(int pos) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

}

