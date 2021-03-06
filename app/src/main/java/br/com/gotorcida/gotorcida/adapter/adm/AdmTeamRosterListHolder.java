package br.com.gotorcida.gotorcida.adapter.adm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.AthleteInfoActivity;
import br.com.gotorcida.gotorcida.dialog.adm.AdmNewsEditDialog;
import br.com.gotorcida.gotorcida.dialog.adm.AdmRosterUpdateDialog;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_REMOVE_ATHLETE_OF_TEAM;

public class AdmTeamRosterListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    TextView nameAthlete;
    final TextView athleteId;
    TextView athletePosition;
    TextView athleteNumber;
    ImageView athleteImageProfile;
    final FragmentManager fragmentManager;
    final Fragment fragment;
    final String teamId;
    ItemClickListener itemClickListener;

    public AdmTeamRosterListHolder(FragmentManager fragmentManager, Fragment fragment, View itemView, String teamId) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.teamId = teamId;
        nameAthlete = (TextView) itemView.findViewById(R.id.team_textview_athlete_name);
        athleteId = (TextView) itemView.findViewById(R.id.team_teamviwer_athlete_id);
        athletePosition = (TextView) itemView.findViewById(R.id.team_roster_athlete_position);
        athleteNumber = (TextView) itemView.findViewById(R.id.team_roster_athlete_number);
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
    @Override
    public boolean onLongClick(View v) {
        final CardView cardView = (CardView) v.findViewById(R.id.card_view_athlete);
        final ColorStateList colorStateList = cardView.getCardBackgroundColor();

        cardView.setCardBackgroundColor(Color.GREEN);
        cardView.setCardElevation(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cardView.setCardBackgroundColor(colorStateList);
            }
        });
        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemoveAthleteTask removeAthleteTask = new RemoveAthleteTask(teamId, athleteId.getText().toString());
                removeAthleteTask.execute();
            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                cardView.setCardBackgroundColor(colorStateList);
            }
        });
        builder.create();
        builder.show();
        return false;
    }


    public class RemoveAthleteTask extends AsyncTask {

        private final String teamId;
        private final String athleteId;

        public RemoveAthleteTask(String teamId, String athleteId) {
            this.teamId = teamId;
            this.athleteId = athleteId;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_REMOVE_ATHLETE_OF_TEAM+"/"+this.teamId+"/"+this.athleteId);
            postRequest.execute("delete");
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            Integer targetRequestCode = fragment.getTargetRequestCode();
            Activity activity = fragment.getActivity();
            Intent intent = activity.getIntent();
            fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent);
        }
    }
}
