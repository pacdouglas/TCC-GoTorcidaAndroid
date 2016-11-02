package br.com.gotorcida.gotorcida.adapter.user;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;

public class SportsListHolder extends RecyclerView.ViewHolder {

    final TextView sportID;
    final CheckBox sportName;
    final ImageView iconSport;
    final TextView teamCount;
    public SportsListHolder(View itemView) {
        super(itemView);
        sportID = (TextView) itemView.findViewById(R.id.selectsport_textview_sportid);
        sportName = (CheckBox) itemView.findViewById(R.id.selectsport_checkbox_sportname);
        iconSport = (ImageView) itemView.findViewById(R.id.img_sport);
        teamCount = (TextView) itemView.findViewById(R.id.selectsport_textview_sport_count);
    }
}
