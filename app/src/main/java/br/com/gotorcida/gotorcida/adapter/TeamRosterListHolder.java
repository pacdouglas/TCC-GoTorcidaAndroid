package br.com.gotorcida.gotorcida.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;

public class TeamRosterListHolder extends RecyclerView.ViewHolder {

    final TextView nameAthlete;

    public TeamRosterListHolder(View itemView) {
        super(itemView);
        nameAthlete = (TextView) itemView.findViewById(R.id.team_textview_athlete_name);
    }
}
