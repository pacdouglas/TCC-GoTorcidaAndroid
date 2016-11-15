package br.com.gotorcida.gotorcida.adapter.user;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.ItemClickListener;

public class TeamsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    final TextView teamID;
    final CheckBox teamName;
    final ImageView teamImg;
    final TextView sportName;
    ItemClickListener itemClickListener;

    public TeamsListHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        teamID = (TextView) itemView.findViewById(R.id.selectteam_textview_teamid);
        teamName = (CheckBox) itemView.findViewById(R.id.selectteam_checkbox_teamname);
        teamImg = (ImageView) itemView.findViewById(R.id.selectteam_imageview_imageteam);
        sportName = (TextView) itemView.findViewById(R.id.selectteam_textview_sport_name);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
        if(!teamName.isChecked()){
            teamName.setChecked(true);
        }else{
            teamName.setChecked(false);
        }
    }
}
