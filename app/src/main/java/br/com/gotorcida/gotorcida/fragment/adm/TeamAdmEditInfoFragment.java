package br.com.gotorcida.gotorcida.fragment.adm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static android.app.Activity.RESULT_OK;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_TEAM_UPDATE;

@SuppressLint("ValidFragment")
public class TeamAdmEditInfoFragment extends Fragment{
    private static final int RESULT_LOAD_IMAGE = 1;
    View mView;
    String mTeamId;
    ProgressBar progressBar;
    ScrollView layout;

    EditText mTeamName;
    EditText mTeamEmail;
    EditText mTeamWebSite;
    EditText mTeamFacebook;
    EditText mTeamTwitter;
    EditText mTeamInstagram;
    EditText mTeamCity;

    TextView mSucces;

    ImageView mTeamLogo;
    Button mBtnLoadImage;
    Button mSendPost;
    Bitmap mBitMap;

    public TeamAdmEditInfoFragment(String mTeamId) {
        this.mTeamId = mTeamId;
        this.mBitMap = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_edit_info, container, false);
        progressBar = (ProgressBar) mView.findViewById(R.id.adm_edit_info_progress);
        layout = (ScrollView) mView.findViewById(R.id.adm_edit_layout);
        mTeamName = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_name);
        mTeamEmail = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_email);
        mTeamWebSite = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_website);
        mTeamFacebook = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_facebook);
        mTeamTwitter = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_twitter);
        mTeamInstagram = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_instagram);
        mTeamCity = (EditText) mView.findViewById(R.id.adm_edit_info_edittext_team_city);
        mTeamLogo = (ImageView) mView.findViewById(R.id.adm_edit_info_imageview_team_logo);
        mSendPost = (Button) mView.findViewById(R.id.adm_edit_info_button_send);
        mSucces = (TextView) mView.findViewById(R.id.adm_edit_info_textview_sucess);
        mBtnLoadImage= (Button) mView.findViewById(R.id.adm_edit_info_button_selectphoto);

        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        mSucces.setVisibility(View.GONE);

        mBtnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(it, RESULT_LOAD_IMAGE);
            }
        });

        mSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject postParameters = new JSONObject();
                try {
                    postParameters.put("name", mTeamName.getText().toString());
                    postParameters.put("emailAddress", mTeamEmail.getText().toString());
                    postParameters.put("website", mTeamWebSite.getText().toString());
                    postParameters.put("facebook", mTeamFacebook.getText().toString());
                    postParameters.put("twitter", mTeamTwitter.getText().toString());
                    postParameters.put("instagram", mTeamInstagram.getText().toString());
                    postParameters.put("city", mTeamCity.getText().toString());

                    SaveInfoTask saveInfoTask = new SaveInfoTask(postParameters);
                    saveInfoTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        LoadTeamDataTask loadTeamDataTask = new LoadTeamDataTask();
        loadTeamDataTask.execute();
        return mView;
    }
    public class SaveInfoTask extends AsyncTask {
        boolean resultPost;
        JSONObject mPostParameters;

        public SaveInfoTask(JSONObject postParameters){
            this.mPostParameters = postParameters;
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_TEAM_UPDATE+"/"+mTeamId);

            if(mBitMap != null){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                try {
                    mPostParameters.put("image", encoded);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            resultPost = postRequest.execute(mPostParameters.toString());
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            mSucces.setVisibility(View.VISIBLE);
            if(resultPost == false){
                mSucces.setText("Ocorreu um erro de comunicação. Tente novamente mais tarde");
            }
        }
    }

    private class LoadTeamDataTask extends AsyncTask {

        JSONObject team;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_LIST_TEAMS, mTeamId);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getActivity(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                    getActivity().getFragmentManager().popBackStack();
                }else{
                    team = new JSONObject(getRequest.getMessage().getData().getString("team"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            try {
                mTeamName.setText(team.getString("name"));
                mTeamEmail.setText(team.getString("emailAddress"));
                mTeamWebSite.setText(team.getString("website"));
                mTeamFacebook.setText(team.getString("facebook"));
                mTeamTwitter.setText(team.getString("twitter"));
                mTeamInstagram.setText(team.getString("instagram"));
                mTeamCity.setText(team.getString("city"));
                Glide.with(getActivity()).load(URL_IMAGES_BASE + team.getString("urlImage")+".png").error(R.drawable.ic_team_no_logo).into(mTeamLogo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mBitMap = BitmapFactory.decodeFile(picturePath);
            mTeamLogo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
