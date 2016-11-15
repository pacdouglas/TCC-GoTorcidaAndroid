package br.com.gotorcida.gotorcida.dialog.user;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.MailSender;

public class MyTeamHereDialog extends DialogFragment {
    Spinner mSpinner;
    View mView;
    EditText mSportName;
    EditText mTeamName;
    EditText mTel;
    Spinner mState;
    EditText mCity;
    EditText mObs;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Envie-nos uma mensagem e tenha sua equipe no GoTorcida!");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_my_team_here, null);

        mSportName = (EditText) mView.findViewById(R.id.my_team_here_edit_text_sport);
        mTeamName = (EditText) mView.findViewById(R.id.my_team_here_edit_text_team_name);
        mTel = (EditText) mView.findViewById(R.id.my_team_here_edit_text_contact);
        mState = (Spinner) mView.findViewById(R.id.my_team_here_spinner_state);
        mCity = (EditText) mView.findViewById(R.id.my_team_here_edit_text_city);
        mObs = (EditText) mView.findViewById(R.id.my_team_here_edit_text_obs);


        mSpinner = (Spinner) mView.findViewById(R.id.my_team_here_spinner_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        builder.setView(mView);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String auxBody = "Esporte :" + mSportName.getText().toString()
                                + "\n\n Nome do Time: " + mTeamName.getText().toString()
                                + "\n\n Telefone: " + mTel.getText().toString()
                                + "\n\n Cidade: " + mCity.getText().toString() + " - "+mState.getSelectedItem().toString()
                                + "\n\n Obs: " + mObs.getText().toString();

                SendMailTask sendMail = new SendMailTask(mTeamName.getText().toString(), auxBody);
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
        String bodyMail = "";
        String subjectMail = "";

        public SendMailTask(String subjectMail, String bodyMail) {
            this.bodyMail = bodyMail;
            this.subjectMail = subjectMail;
        }

        protected void onPreExecute() {
            Toast.makeText(mView.getContext(), "Enviando mensagem...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Object doInBackground(Object... args) {
            MailSender mailSender = new MailSender(getContext(), "douglas.pac@gmail.com", "Proposta GoTorcida - "+subjectMail, bodyMail);
            send = mailSender.send();
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            if(send){
                Toast.makeText(mView.getContext(), "Mensagem Enviada Com Sucesso! Aguarde nosso retorno", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mView.getContext(), "Erro no envio da mensagem. Tente novamente", Toast.LENGTH_LONG).show();
            }
        }

    }
}
