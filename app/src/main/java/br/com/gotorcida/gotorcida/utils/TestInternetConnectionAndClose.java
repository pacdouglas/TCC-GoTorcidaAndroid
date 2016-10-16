package br.com.gotorcida.gotorcida.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by dougl on 16/10/2016.
 */

public class TestInternetConnectionAndClose {
    public static void execute(final Activity activity){
        new AlertDialog.Builder(activity)
                .setTitle("Erro de Conexão")
                .setMessage("Verifique sua conexão com a internet.")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                }).create().show();
    }
}
