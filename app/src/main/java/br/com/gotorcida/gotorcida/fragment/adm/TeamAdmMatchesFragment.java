package br.com.gotorcida.gotorcida.fragment.adm;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.dialog.adm.AdmMatchesInsertDialog;
import br.com.gotorcida.gotorcida.dialog.adm.AdmNewsEditDialog;

public class TeamAdmMatchesFragment extends Fragment{
    public static final int DIALOG_FRAGMENT = 666;
    View mView;
    String mTeamId;
    RecyclerView mMatchesList;
    ProgressBar mProgressBar;
    public TeamAdmMatchesFragment(String teamId) {
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_matches, container, false);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.fragment_adm_matches_progressbar);
        mMatchesList = (RecyclerView) mView.findViewById(R.id.fragment_adm_matches_listview_matches);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fragment_adm_matches_floatingbutton_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return mView;
    }

    private void showDialog() {
        AdmMatchesInsertDialog admMatchesInsertDialog = new AdmMatchesInsertDialog(mTeamId);
        admMatchesInsertDialog.setTargetFragment(this, DIALOG_FRAGMENT);
        admMatchesInsertDialog.show(getActivity().getSupportFragmentManager().beginTransaction(),  "");
    }

}
