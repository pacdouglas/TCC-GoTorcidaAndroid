package br.com.gotorcida.gotorcida.fragment.adm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static android.app.Activity.RESULT_OK;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_ADD_ATHLETE;

@SuppressLint("ValidFragment")
public class TeamAdmAddAthleteFragment extends Fragment{
    private static final int RESULT_LOAD_IMAGE = 1;

    View mView;
    String mTeamId;
    EditText mName;
    EditText mDateOfBirth;
    EditText mCity;
    EditText mEmail;
    EditText mWebSite;
    EditText mFacebook;
    EditText mInstagram;
    EditText mTwitter;

    ImageView mPhotoProfile;
    Button mBtnSelectPhoto;
    Button mBtnSend;
    ScrollView mLayout;
    ProgressBar mProgressBar;
    TextView mSuccess;

    Bitmap mBitMap;
    public TeamAdmAddAthleteFragment(String teamId) {
        this.mTeamId = teamId;
        mBitMap = null;
    }
///
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_add_athlete, container, false);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.adm_add_athlete_progressbar);
        mLayout = (ScrollView) mView.findViewById(R.id.adm_add_athlete_layout);
        mName = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_name);
        mDateOfBirth = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_date_of_birth);
        mDateOfBirth.addTextChangedListener(Mask.insert("##/##/####", mDateOfBirth));
        mCity = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_city);
        mEmail = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_email);
        mWebSite = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_website);
        mFacebook = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_facebook);
        mInstagram = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_instagram);
        mTwitter = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_twitter);
        mSuccess = (TextView) mView.findViewById(R.id.adm_add_athlete_textview_success);

        mPhotoProfile = (ImageView) mView.findViewById(R.id.adm_add_athlete_imageview_profilephoto);

        mBtnSelectPhoto = (Button) mView.findViewById(R.id.adm_add_athlete_button_selectphoto);
        mBtnSend = (Button) mView.findViewById(R.id.adm_add_athlete_button_send);

        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(it, RESULT_LOAD_IMAGE);
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.getText().toString().isEmpty() || mDateOfBirth.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Campos Nome e Data de Nascimento Obrigatório", Toast.LENGTH_LONG).show();
                }else {

                    JSONObject jsonObject = new JSONObject();
                    JSONObject athleteData = new JSONObject();
                    try {
                        athleteData.put("name", mName.getText().toString());
                        athleteData.put("birthDate", mDateOfBirth.getText().toString());
                        athleteData.put("city", mCity.getText().toString());
                        athleteData.put("emailAddress", mEmail.getText().toString());
                        athleteData.put("website", mWebSite.getText().toString());
                        athleteData.put("facebook", mFacebook.getText().toString());
                        athleteData.put("instagram", mInstagram.getText().toString());
                        athleteData.put("twitter", mTwitter.getText().toString());
                        jsonObject.put("athleteData", athleteData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SendAthleteToServerTask sendAthleteToServerTask = new SendAthleteToServerTask(jsonObject);
                    sendAthleteToServerTask.execute();
                }
            }
        });
        return mView;
    }

    public class SendAthleteToServerTask extends AsyncTask {
        boolean resultPost;
        JSONObject mPostParameters;

        public SendAthleteToServerTask(JSONObject postParameters){
            this.mPostParameters = postParameters;
        }

        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
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
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_ADD_ATHLETE + "/" + mTeamId);
            resultPost = postRequest.execute(mPostParameters.toString());
            return null;
        }

        @Override
        public void onPostExecute(Object result) {
            if(resultPost == false){
                mSuccess.setText("Ocorreu um erro de comunicação. Tente novamente mais tarde");
            }
            mProgressBar.setVisibility(View.GONE);
            mSuccess.setVisibility(View.VISIBLE);
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
            mPhotoProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
