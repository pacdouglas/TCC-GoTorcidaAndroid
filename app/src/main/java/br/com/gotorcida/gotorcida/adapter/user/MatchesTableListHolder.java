package br.com.gotorcida.gotorcida.adapter.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.EventDetailsActivity;
import br.com.gotorcida.gotorcida.activity.user.NewsDetailsActivity;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class MatchesTableListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    final TextView firstTeamName;
    final TextView firstTeamScore;
    final ImageView firstTeamLogo;

    final TextView secondTeamName;
    final TextView secondTeamScore;
    final ImageView secondTeamLogo;

    final TextView eventId;
    final TextView eventDate;
    ItemClickListener itemClickListener;
    public MatchesTableListHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        firstTeamName = (TextView) itemView.findViewById(R.id.item_matches_table_textview_firstTeamName);
        firstTeamScore = (TextView) itemView.findViewById(R.id.item_matches_table_textview_scoreFirstTeam);
        firstTeamLogo = (ImageView) itemView.findViewById(R.id.item_matches_table_imageview_firstTeam);

        secondTeamName = (TextView) itemView.findViewById(R.id.item_matches_table_textview_secondTeamName);
        secondTeamScore = (TextView) itemView.findViewById(R.id.item_matches_table_textview_scoreSecondTeam);
        secondTeamLogo = (ImageView) itemView.findViewById(R.id.item_matches_table_imageview_secondTeam);

        eventDate = (TextView) itemView.findViewById(R.id.item_matches_table_textview_eventDate);
        eventId = (TextView) itemView.findViewById(R.id.item_matches_table_textview_eventId);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
        String testando = firstTeamName.getText().toString();
        Intent it = new Intent(itemView.getContext(), EventDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("eventId", eventId.getText().toString());
        it.putExtras(bundle);
        itemView.getContext().startActivity(it);
    }
}
