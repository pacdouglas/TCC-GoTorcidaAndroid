package br.com.gotorcida.gotorcida.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class TeamRosterListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView nameAthlete;
    TextView athleteId;
    TextView athletePosition;
    ItemClickListener itemClickListener;

    public TeamRosterListHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        nameAthlete = (TextView) itemView.findViewById(R.id.team_textview_athlete_name);
        athleteId = (TextView) itemView.findViewById(R.id.team_teamviwer_athlete_id);
        athletePosition = (TextView) itemView.findViewById(R.id.team_roster_athlete_position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }
}
