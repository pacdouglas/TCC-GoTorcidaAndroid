package br.com.gotorcida.gotorcida.dialog.adm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import br.com.gotorcida.gotorcida.R;

public class AdmMatchesResultDialog extends DialogFragment {
    View mView;
    private String mEventId;

    public AdmMatchesResultDialog(String eventId){
        super();
        this.mEventId = eventId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_news, null);
        builder.setView(mView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
