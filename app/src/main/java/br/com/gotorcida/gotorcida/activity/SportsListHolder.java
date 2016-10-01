package br.com.gotorcida.gotorcida.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import br.com.gotorcida.gotorcida.R;

public class SportsListHolder extends RecyclerView.ViewHolder {

    final CheckBox sport;

    public SportsListHolder(View itemView) {
        super(itemView);
        sport = (CheckBox) itemView.findViewById(R.id.selectsport_checkbox_sportname);
    }
}
