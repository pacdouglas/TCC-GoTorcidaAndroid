package br.com.gotorcida.gotorcida.adapter.user;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;

public class TeamsListHolder extends RecyclerView.ViewHolder {

    final TextView teamID;
    final CheckBox teamName;
    final ImageView teamImg;

    public TeamsListHolder(View itemView) {
        super(itemView);
        teamID = (TextView) itemView.findViewById(R.id.selectteam_textview_teamid);
        teamName = (CheckBox) itemView.findViewById(R.id.selectteam_checkbox_teamname);
        teamImg = (ImageView) itemView.findViewById(R.id.selectteam_imageview_imageteam);
    }
}
