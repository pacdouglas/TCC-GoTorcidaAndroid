package br.com.gotorcida.gotorcida.fragment.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.MailSender;

/**
 * Created by dougl on 15/10/2016.
 */

public class MyTeamHereFragment extends Fragment {
    View mView;
    Spinner spnState;
    Button btnSend;
    LinearLayout form;
    LinearLayout msgSuccess;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_team_here, container, false);

        form = (LinearLayout) mView.findViewById(R.id.my_team_here_form);
        msgSuccess = (LinearLayout) mView.findViewById(R.id.my_team_here_post_send_email);

        progressBar = (ProgressBar) mView.findViewById(R.id.my_team_here_progress);

        spnState = (Spinner) mView.findViewById(R.id.my_team_here_spinner_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnState.setAdapter(adapter);

        btnSend = (Button) mView.findViewById(R.id.my_team_here_button_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMailTask sendMail = new SendMailTask();
                sendMail.execute();
            }
        });

        return mView;
    }

    public class SendMailTask extends AsyncTask {
        boolean send = true;
        protected void onPreExecute() {
            form.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

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
        public void onProgressUpdate(Object... values) {

        }

        @Override
        public void onPostExecute(Object result) {
            progressBar.setVisibility(View.GONE);
            msgSuccess.setVisibility(View.VISIBLE);
            if(!send){
                TextView txtFinalMsg = (TextView) msgSuccess.findViewById(R.id.my_team_here_text_view_final_msg);
                String msg = "Ocorreu um erro no envio da mensagem. Tente novamente mais tarde ou acesse www.gotorcida.com.br";
                txtFinalMsg.setText(msg);
            }
        }

    }
}
