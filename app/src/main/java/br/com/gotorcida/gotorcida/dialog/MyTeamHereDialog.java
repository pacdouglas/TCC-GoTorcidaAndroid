package br.com.gotorcida.gotorcida.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.MailSender;

public class MyTeamHereDialog extends DialogFragment {
    Spinner mSpinner;
    View mView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Envie-nos uma mensagem e tenha sua equipe no GoTorcida!");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_my_team_here, null);

        mSpinner = (Spinner) mView.findViewById(R.id.my_team_here_spinner_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        builder.setView(mView);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SendMailTask sendMail = new SendMailTask();
                sendMail.execute();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    public class SendMailTask extends AsyncTask {
        boolean send = true;
        protected void onPreExecute() {
            Toast.makeText(mView.getContext(), "Enviando mensagem...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Object doInBackground(Object... args) {
            try {
                MailSender sender = new MailSender("gotorcida8cco@gmail.com", "equipegotorcida321");
                sender.sendMail("EMAIL DO APP",
                        "AE FOI SAPORRA",
                        "douglas.pac@gmail.com",
                        "ricardo.zanardo@yahoo.com");
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
                send = false;
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            if(!send){
                Toast.makeText(mView.getContext(), "Mensagem Enviada Com Sucesso! Aguarde nosso retorno", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mView.getContext(), "Erro no envio da mensagem. Tente novamente", Toast.LENGTH_LONG).show();
            }
        }

    }
}
