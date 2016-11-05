package br.com.gotorcida.gotorcida.adapter.user;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;

public class MatchesTableListHolder extends RecyclerView.ViewHolder {

    final TextView firstTeamName;
    final TextView firstTeamScore;
    final ImageView firstTeamLogo;

    final TextView secondTeamName;
    final TextView secondTeamScore;
    final ImageView secondTeamLogo;

    final TextView eventDate;

    public MatchesTableListHolder(View itemView) {
        super(itemView);
        firstTeamName = (TextView) itemView.findViewById(R.id.item_matches_table_textview_firstTeamName);
        firstTeamScore = (TextView) itemView.findViewById(R.id.item_matches_table_textview_scoreFirstTeam);
        firstTeamLogo = (ImageView) itemView.findViewById(R.id.item_matches_table_imageview_firstTeam);

        secondTeamName = (TextView) itemView.findViewById(R.id.item_matches_table_textview_secondTeamName);
        secondTeamScore = (TextView) itemView.findViewById(R.id.item_matches_table_textview_scoreSecondTeam);
        secondTeamLogo = (ImageView) itemView.findViewById(R.id.item_matches_table_imageview_secondTeam);

        eventDate = (TextView) itemView.findViewById(R.id.item_matches_table_textview_eventDate);
    }
}
