package br.com.gotorcida.gotorcida.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;

public class SportsListHolder extends RecyclerView.ViewHolder {

    final TextView sportID;
    final CheckBox sportName;

    public SportsListHolder(View itemView) {
        super(itemView);
        sportID = (TextView) itemView.findViewById(R.id.selectsport_textview_sportid);
        sportName = (CheckBox) itemView.findViewById(R.id.selectsport_checkbox_sportname);
    }
}
