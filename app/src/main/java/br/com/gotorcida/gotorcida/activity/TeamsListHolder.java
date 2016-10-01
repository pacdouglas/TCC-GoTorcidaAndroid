package br.com.gotorcida.gotorcida.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;

public class TeamsListHolder extends RecyclerView.ViewHolder {

    final TextView leagueID;
    final CheckBox leagueName;

    public TeamsListHolder(View itemView) {
        super(itemView);
        leagueID = (TextView) itemView.findViewById(R.id.selectleague_textview_leagueid);
        leagueName = (CheckBox) itemView.findViewById(R.id.selectleague_checkbox_leaguename);
    }
}
