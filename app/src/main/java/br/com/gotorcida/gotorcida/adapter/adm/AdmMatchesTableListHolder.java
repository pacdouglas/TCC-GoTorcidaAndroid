package br.com.gotorcida.gotorcida.adapter.adm;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.dialog.adm.AdmMatchesResultDialog;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class AdmMatchesTableListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    final TextView firstTeamName;
    final TextView firstTeamScore;
    final ImageView firstTeamLogo;

    final TextView secondTeamName;
    final TextView secondTeamScore;
    final ImageView secondTeamLogo;

    final TextView eventId;
    final TextView eventDate;
    ItemClickListener itemClickListener;
    View mView;

    public AdmMatchesTableListHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

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
        FragmentTransaction transaction = ((FragmentActivity)mView.getContext()).getSupportFragmentManager().beginTransaction();
        AdmMatchesResultDialog admMatchesResultDialog = new AdmMatchesResultDialog(eventId.getText().toString());
        admMatchesResultDialog.show(transaction,  "");
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
                //Todo: excluir o bagulho
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
}
