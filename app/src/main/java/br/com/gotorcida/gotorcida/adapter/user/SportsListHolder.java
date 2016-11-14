package br.com.gotorcida.gotorcida.adapter.user;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class SportsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    final TextView sportID;
    final CheckBox sportName;
    final ImageView iconSport;
    final TextView teamCount;
    ItemClickListener itemClickListener;

    public SportsListHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        sportID = (TextView) itemView.findViewById(R.id.selectsport_textview_sportid);
        sportName = (CheckBox) itemView.findViewById(R.id.selectsport_checkbox_sportname);
        iconSport = (ImageView) itemView.findViewById(R.id.img_sport);
        teamCount = (TextView) itemView.findViewById(R.id.selectsport_textview_sport_count);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
        if(!sportName.isChecked()){
            sportName.setChecked(true);
        }else{
            sportName.setChecked(false);
        }
    }

}
