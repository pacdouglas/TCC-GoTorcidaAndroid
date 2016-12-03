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

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.CollectionUtils;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static android.app.Activity.RESULT_OK;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_ADD_TEAM;

@SuppressLint("ValidFragment")
public class TeamAdmAddTeamFragment extends Fragment{
    View mView;
    private static final int RESULT_LOAD_IMAGE = 1;
    String mTeamId;
    EditText mTeamName;
    ImageView mTeamLogo;
    Button mBtnLoadImage;
    Button mBtnSend;
    TextView mSuccess;
    ProgressBar mProgressBar;
    Bitmap mBitMap;
    ScrollView mLayout;
    JSONObject mParameters;
    public TeamAdmAddTeamFragment(String teamId) {
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_add_team, container, false);
        mBitMap = null;
        mLayout = (ScrollView) mView.findViewById(R.id.adm_add_team_layout);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.adm_add_team_progressbar);
        mTeamLogo = (ImageView) mView.findViewById(R.id.adm_add_team_imageview_logo);
        mTeamName = (EditText) mView.findViewById(R.id.adm_add_team_edittext_name);
        mBtnLoadImage = (Button) mView.findViewById(R.id.adm_add_team_button_selectphoto);
        mBtnSend = (Button) mView.findViewById(R.id.adm_add_team_button_send);
        mSuccess = (TextView) mView.findViewById(R.id.adm_add_team_textview_success);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (CollectionUtils.ValidateFields(getContext(), mTeamName)) {
                try {
                    mParameters.put("name", mTeamName.getText().toString());
                    SendTeamTask sendTeamTask = new SendTeamTask();
                    sendTeamTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }
        });

        mBtnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(it, RESULT_LOAD_IMAGE);
            }
        });

        return mView;
    }

    public class SendTeamTask extends AsyncTask{
        boolean test;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            test = false;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            if(mBitMap != null){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                try {
                    mParameters.put("image", encoded);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_ADD_TEAM + "/" + mTeamId);
            test = postRequest.execute(mParameters.toString());
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(test){
                mSuccess.setText("Equipe Cadastrada Com Sucesso!");
            }else{
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
            mTeamLogo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

}
