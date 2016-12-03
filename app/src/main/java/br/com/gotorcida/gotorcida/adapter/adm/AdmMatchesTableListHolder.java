package br.com.gotorcida.gotorcida.adapter.adm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.dialog.adm.AdmMatchesEditDialog;
import br.com.gotorcida.gotorcida.dialog.adm.AdmMatchesResultDialog;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

public class AdmMatchesTableListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    final TextView firstTeamName;
    final TextView firstTeamScore;
    final ImageView firstTeamLogo;

    final TextView secondTeamName;
    final TextView secondTeamScore;
    final ImageView secondTeamLogo;
    final TextView eventCity;
    final TextView eventId;
    final TextView eventDate;

    private final FragmentManager fragmentManager;
    ItemClickListener itemClickListener;

    final Fragment fragment;

    View mView;

    public AdmMatchesTableListHolder(Fragment fragment, FragmentManager fragmentManager, View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        this.fragment = fragment;
        this.fragmentManager = fragmentManager;
        firstTeamName = (TextView) itemView.findViewById(R.id.item_matches_table_textview_firstTeamName);
        firstTeamScore = (TextView) itemView.findViewById(R.id.item_matches_table_textview_scoreFirstTeam);
        firstTeamLogo = (ImageView) itemView.findViewById(R.id.item_matches_table_imageview_firstTeam);

        secondTeamName = (TextView) itemView.findViewById(R.id.item_matches_table_textview_secondTeamName);
        secondTeamScore = (TextView) itemView.findViewById(R.id.item_matches_table_textview_scoreSecondTeam);
        secondTeamLogo = (ImageView) itemView.findViewById(R.id.item_matches_table_imageview_secondTeam);

        eventDate = (TextView) itemView.findViewById(R.id.item_matches_table_textview_eventDate);
        eventId = (TextView) itemView.findViewById(R.id.item_matches_table_textview_eventId);
        eventCity = (TextView) itemView.findViewById(R.id.item_maches_table_textview_city);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        builder.setPositiveButton("Atribuir Resultado", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                AdmMatchesResultDialog admMatchesResultDialog = new AdmMatchesResultDialog(eventId.getText().toString());
                admMatchesResultDialog.setTargetFragment(fragment, 666);
                admMatchesResultDialog.show(transaction,  "");
            }
        });
        builder.setNeutralButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                AdmMatchesEditDialog admMatchesEditDialog = new AdmMatchesEditDialog(eventId.getText().toString());
                admMatchesEditDialog.setTargetFragment(fragment, 666);
                admMatchesEditDialog.show(transaction,  "");
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public boolean onLongClick(View v) {
        final CardView cardView = (CardView) v.findViewById(R.id.cardView_sports);
        final ColorStateList colorStateList = cardView.getCardBackgroundColor();

        cardView.setCardBackgroundColor(Color.GREEN);
        cardView.setCardElevation(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cardView.setCardBackgroundColor(colorStateList);
            }
        });
        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteEventTask deleteEventTask = new DeleteEventTask(eventId.getText().toString());
                deleteEventTask.execute();
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

    public class DeleteEventTask extends AsyncTask {

        private final String eventId;

        public DeleteEventTask(String eventId) {
            this.eventId = eventId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest;
            postRequest = new PostRequest(Constants.URL_SERVER_JSON_DELETE_EVENT + "/" + eventId);
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
