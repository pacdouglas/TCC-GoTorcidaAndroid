package br.com.gotorcida.gotorcida.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;

public class TeamNewsListHolder extends RecyclerView.ViewHolder {

    final TextView newsDate;
    final TextView newsTitle;
    final TextView newsBody;
    final TextView newsId;

    public TeamNewsListHolder(View itemView) {
        super(itemView);
        newsDate = (TextView) itemView.findViewById(R.id.team_textview_newsdate);
        newsTitle = (TextView) itemView.findViewById(R.id.team_textview_newstitle);
        newsBody = (TextView) itemView.findViewById(R.id.team_textview_newsbody);
        newsId = (TextView) itemView.findViewById(R.id.team_textview_newsid);
    }
}
