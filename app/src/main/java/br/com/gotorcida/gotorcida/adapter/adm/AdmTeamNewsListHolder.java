package br.com.gotorcida.gotorcida.adapter.adm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.NewsDetailsActivity;
import br.com.gotorcida.gotorcida.dialog.adm.AdmNewsEditDialog;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_DELETE_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_REMOVE_ATHLETE_OF_TEAM;
import static java.security.AccessController.getContext;

public class AdmTeamNewsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    final TextView newsDate;
    final TextView newsTitle;
    final TextView newsBody;
    final TextView teamId;
    final String newsType;
    final TextView newsId;
    final FragmentManager fragmentManager;
    final Fragment fragment;
    ItemClickListener itemClickListener;
    Bundle bundle;

    public AdmTeamNewsListHolder(FragmentManager fragmentManager, Fragment fragment, View itemView, String newsType) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        newsDate = (TextView) itemView.findViewById(R.id.team_textview_newsdate);
        newsTitle = (TextView) itemView.findViewById(R.id.team_textview_newstitle);
        newsBody = (TextView) itemView.findViewById(R.id.team_textview_newsbody);
        teamId = (TextView) itemView.findViewById(R.id.team_textview_teamid);
        newsId = (TextView) itemView.findViewById(R.id.team_textview_newsid);
        this.newsType = newsType;
        bundle = new Bundle();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        AdmNewsEditDialog admNewsEditDialog = new AdmNewsEditDialog(teamId.getText().toString());
        admNewsEditDialog.setTargetFragment(fragment, 666);
        admNewsEditDialog.show(fragmentManager.beginTransaction(), newsId.getText().toString());
    }

    @Override
    public boolean onLongClick(View v) {
        final CardView cardView = (CardView) v.findViewById(R.id.cardView_news);
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
                RemoveNewsTask removeNewsTask = new RemoveNewsTask(newsId.getText().toString());
                removeNewsTask.execute();
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


    public class RemoveNewsTask extends AsyncTask {

        private final String newsId;

        public RemoveNewsTask(String newsId) {
            this.newsId = newsId;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_DELETE_NEWS+"/"+this.newsId);
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
