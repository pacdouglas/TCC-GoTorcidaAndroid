package br.com.gotorcida.gotorcida.adapter.adm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.NewsDetailsActivity;
import br.com.gotorcida.gotorcida.dialog.adm.AdmNewsEditDialog;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

import static java.security.AccessController.getContext;

public class AdmTeamNewsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
}
