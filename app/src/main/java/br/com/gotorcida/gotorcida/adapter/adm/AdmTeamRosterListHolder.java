package br.com.gotorcida.gotorcida.adapter.adm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.AthleteInfoActivity;
import br.com.gotorcida.gotorcida.dialog.adm.AdmNewsEditDialog;
import br.com.gotorcida.gotorcida.dialog.adm.AdmRosterUpdateDialog;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class AdmTeamRosterListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView nameAthlete;
    TextView athleteId;
    TextView athletePosition;
    ImageView athleteImageProfile;
    final FragmentManager fragmentManager;
    final Fragment fragment;
    final String teamId;
    ItemClickListener itemClickListener;

    public AdmTeamRosterListHolder(FragmentManager fragmentManager, Fragment fragment, View itemView, String teamId) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.teamId = teamId;
        nameAthlete = (TextView) itemView.findViewById(R.id.team_textview_athlete_name);
        athleteId = (TextView) itemView.findViewById(R.id.team_teamviwer_athlete_id);
        athletePosition = (TextView) itemView.findViewById(R.id.team_roster_athlete_position);
        athleteImageProfile = (ImageView) itemView.findViewById(R.id.team_roster_athlete_photo);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        AdmRosterUpdateDialog admRosterUpdateDialog = new AdmRosterUpdateDialog(teamId, athleteId.getText().toString());
        admRosterUpdateDialog.setTargetFragment(fragment, 666);
        admRosterUpdateDialog.show(fragmentManager.beginTransaction(), athleteId.getText().toString());
    }
}
