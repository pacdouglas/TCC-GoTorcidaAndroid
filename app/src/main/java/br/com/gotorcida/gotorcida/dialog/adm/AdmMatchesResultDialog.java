package br.com.gotorcida.gotorcida.dialog.adm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.Constants;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_SET_EVENT_RESULT;

@SuppressLint("ValidFragment")
public class AdmMatchesResultDialog extends DialogFragment {
    View mView;
    private String mEventId;

    private EditText mResultFirstTeam;
    private EditText mResultSecondTeam;
    private ImageView mLogoFirstTeam;
    private ImageView mLogoSecondTeam;
    private LinearLayout mLayout;
    private ProgressBar mProgressBar;
    private JSONObject parameters;

    public AdmMatchesResultDialog(String eventId){
        super();
        this.mEventId = eventId;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_matches_result, null);
        builder.setView(mView);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.adm_matches_result_progressbar);
        mLayout = (LinearLayout) mView.findViewById(R.id.adm_matches_result_layout);
        mResultFirstTeam = (EditText) mView.findViewById(R.id.adm_matches_result_edittext_result_firstteam);
        mResultSecondTeam = (EditText) mView.findViewById(R.id.adm_matches_result_edittext_result_secondteam);
        mLogoFirstTeam = (ImageView) mView.findViewById(R.id.adm_matches_result_imageview_firstteamlogo);
        mLogoSecondTeam = (ImageView) mView.findViewById(R.id.adm_matches_result_imageview_secondteamlogo);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mResultFirstTeam.getText().toString().isEmpty() || mResultSecondTeam.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Erro: Campos não foram preenchidos", Toast.LENGTH_SHORT).show();
                }else{
                    parameters = new JSONObject();
                    try {
                        parameters.put("resultFirstTeam", mResultFirstTeam.getText().toString());
                        parameters.put("resultSecondTeam", mResultSecondTeam.getText().toString());
                        SaveEventResultTask saveEventResultTask = new SaveEventResultTask();
                        saveEventResultTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        LoadEventTask loadEventTask = new LoadEventTask();
        loadEventTask.execute();

        return builder.create();
    }

    public class SaveEventResultTask extends AsyncTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_SET_EVENT_RESULT);
            postRequest.execute(parameters.toString());
            return null;
        }
    }

    public class LoadEventTask extends AsyncTask{
        String firstTeamName = null;
        String urlFirstTeam = null;
        String secondTeamName = null;
        String urlSecondTeam = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest;
            getRequest = new GetRequest(Constants.URL_SERVER_JSON_FIND_EVENT, mEventId);
            getRequest.execute();

            JSONObject requestResult = getRequest.getMessage().getData();

            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500){
                    Toast.makeText(getActivity(), "Não foi possível carregar os dados.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject event = new JSONObject(requestResult.getString("event"));
                    JSONObject firstTeam = new JSONObject(event.getString("firstTeam"));
                    JSONObject secondTeam = new JSONObject(event.getString("secondTeam"));
                    firstTeamName = firstTeam.getString("name");
                    urlFirstTeam = firstTeam.getString("urlImage");
                    secondTeamName = secondTeam.getString("name");
                    urlSecondTeam = secondTeam.getString("urlImage");
                    //TODO: Verificar o winner e colocar se existir
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mResultFirstTeam.setHint(firstTeamName);
            mResultSecondTeam.setHint(secondTeamName);

            Glide.with(getActivity()).load(URL_IMAGES_BASE + urlFirstTeam+".png")
                    .error(R.drawable.ic_team_no_logo).into(mLogoFirstTeam);
            Glide.with(getActivity()).load(URL_IMAGES_BASE + urlSecondTeam+".png")
                    .into(mLogoSecondTeam);

            mProgressBar.setVisibility(View.GONE);
            mLayout.setVisibility(View.VISIBLE);
        }
    }

}
