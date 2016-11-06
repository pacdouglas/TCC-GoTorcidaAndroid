package br.com.gotorcida.gotorcida.adapter.adm;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.adapter.user.TeamNewsListHolder;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class AdmTeamNewsListAdapter extends RecyclerView.Adapter {

    List<JSONObject> news;
    Context context;
    JSONObject data;
    Fragment fragment;
    AdmTeamNewsListHolder holder;
    FragmentManager fragmentManager;

    public AdmTeamNewsListAdapter(List<JSONObject> news, Context context, FragmentManager fragmentManager, Fragment fragment){
        this.news = news;
        this.fragment = fragment;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_team_news, parent, false);
        holder = new AdmTeamNewsListHolder(fragmentManager, fragment, view, "team");
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        data = news.get(position);
        holder.setIsRecyclable(false);

        try {
            holder.newsDate.setText(data.getString("formatedRegistrationDate"));
            holder.newsTitle.setText(data.getString("title"));
            holder.newsBody.setText(data.getString("description"));
            holder.teamId.setText(data.getString("teamId"));
            holder.newsId.setText(data.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

}
