package br.com.gotorcida.gotorcida.adapter.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.NewsDetailsActivity;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class TeamNewsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    final TextView newsDate;
    final TextView newsTitle;
    final TextView newsBody;
    final TextView teamId;
    final String newsType;
    final TextView newsId;
    ItemClickListener itemClickListener;
    Bundle bundle;

    public TeamNewsListHolder(View itemView, String newsType) {
        super(itemView);
        itemView.setOnClickListener(this);
        newsDate = (TextView) itemView.findViewById(R.id.team_textview_newsdate);
        newsTitle = (TextView) itemView.findViewById(R.id.team_textview_newstitle);
        newsBody = (TextView) itemView.findViewById(R.id.team_textview_newsbody);
        teamId = (TextView) itemView.findViewById(R.id.team_textview_teamid);
        newsId = (TextView) itemView.findViewById(R.id.team_textview_newsid);
        this.newsType = newsType;
        bundle = new Bundle();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
        bundle.putString("teamId", teamId.getText().toString());
        bundle.putString("id", newsId.getText().toString());
        bundle.putString("newsType", newsType);
        Intent it = new Intent(itemView.getContext(), NewsDetailsActivity.class);
        it.putExtras(bundle);
        itemView.getContext().startActivity(it);
    }
}
