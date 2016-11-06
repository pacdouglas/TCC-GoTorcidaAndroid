package br.com.gotorcida.gotorcida.dialog.user;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import br.com.gotorcida.gotorcida.R;

/**
 * Created by dougl on 23/10/2016.
 */

public class AboutDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_about_message)
                .setMessage("Alunos: \nDouglas Martins\nJosé Ricardo Zanardo Junior\nRafael Medeira Anjos\nRhamah Nemézio Nogueira")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}
