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

public class TeamNewsListAdapter extends RecyclerView.Adapter {

    List<JSONObject> news;
    Context context;
    JSONObject data;
    TeamNewsListHolder holder;
    public TeamNewsListAdapter(List<JSONObject> news, Context context){
        this.news = news;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_team_news, parent, false);
        holder = new TeamNewsListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        data = news.get(position);

        try {
            holder.newsDate.setText(data.getString("formatedRegistrationDate"));
            holder.newsTitle.setText(data.getString("title"));
            holder.newsBody.setText(data.getString("description"));



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

}
