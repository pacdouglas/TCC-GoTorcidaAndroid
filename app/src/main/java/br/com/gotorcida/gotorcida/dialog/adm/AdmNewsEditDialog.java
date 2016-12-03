package br.com.gotorcida.gotorcida.dialog.adm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.CollectionUtils;
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_INSERT_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_NEWS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_UPDATE_NEWS;

@SuppressLint("ValidFragment")
public class AdmNewsEditDialog extends DialogFragment {
    View mView;

    EditText newsTitle;
    EditText newsDescription;
    EditText newsDate;

    ProgressBar progressBar;
    LinearLayout form;

    private boolean updating;
    private String newsID;
    private String teamID;

    public AdmNewsEditDialog(String teamID){
        super();
        this.teamID = teamID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_news, null);

        progressBar = (ProgressBar) mView.findViewById(R.id.dialog_adm_news_progressbar);
        form = (LinearLayout) mView.findViewById(R.id.dialog_adm_news_form);

        newsTitle = (EditText) mView.findViewById(R.id.dialog_adm_news_title);
        newsDescription = (EditText) mView.findViewById(R.id.dialog_adm_news_news);
        newsDate = (EditText) mView.findViewById(R.id.dialog_adm_news_date);
        newsDate.addTextChangedListener(Mask.insert("##/##/####", newsDate));

        updating = !this.getTag().toString().equals("");
        newsID = this.getTag();

        if (this.getTag().equals("")) {
            builder.setTitle("Incluíndo notícia");
        } else {
            builder.setTitle("Alterando notícia");
            LoadNewsTask loadNewsTask = new LoadNewsTask();
            loadNewsTask.execute(newsID);
        }

        builder.setView(mView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fragment = getTargetFragment();
                Integer targetRequestCode = getTargetRequestCode();

                Activity activity = getActivity();
                Intent intent = activity.getIntent();

                fragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, intent);
            }
        });

        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();

        if (d != null) {
            final Button positiveButton  = (Button) d.getButton(Dialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (CollectionUtils.ValidateFields(getContext(), newsDate, newsDescription, newsTitle)) {
                        positiveButton.setEnabled(false);

                        JSONObject postParameters = new JSONObject();
                        try {
                            if (!newsID.equals("")) {
                                postParameters.put("id", newsID);
                            }

                            postParameters.put("title", newsTitle.getText().toString());
                            postParameters.put("description", newsDescription.getText().toString());
                            postParameters.put("date", newsDate.getText().toString());
                            postParameters.put("user", SaveSharedPreference.getUserName(getContext()));
                            postParameters.put("teamId", teamID);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        SaveNewsTask saveNewsTask = new SaveNewsTask(updating, postParameters);
                        saveNewsTask.execute();

                        Fragment fragment = getTargetFragment();
                        Integer targetRequestCode = getTargetRequestCode();

                        Activity activity = getActivity();
                        Intent intent = activity.getIntent();

                        fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent);
                    }
                }
            });
        }
    }

    public class LoadNewsTask extends AsyncTask {

        JSONObject news = null;

        protected void onPreExecute() {
            form.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_NEWS, params[0].toString());
            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getContext(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    this.news = new JSONObject(json.getString("news"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            if (news != null) {
                try {
                    newsTitle.setText(news.getString("title"));
                    newsDescription.setText(news.getString("description"));
                    newsDate.setText(news.getString("formatedRegistrationDate"));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            form.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public class SaveNewsTask extends AsyncTask {

        private final boolean isUpdatingNews;
        private final JSONObject postParameters;

        public SaveNewsTask(boolean isUpdatingNews, JSONObject postParameters) {
            this.isUpdatingNews = isUpdatingNews;
            this.postParameters = postParameters;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {

            PostRequest postRequest;

            if (isUpdatingNews) {
                postRequest = new PostRequest(URL_SERVER_JSON_UPDATE_NEWS);
            } else {
                postRequest = new PostRequest(URL_SERVER_JSON_INSERT_NEWS);
            }

            postRequest.execute(postParameters.toString());
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            AdmNewsEditDialog.this.dismiss();
        }
    }

}
