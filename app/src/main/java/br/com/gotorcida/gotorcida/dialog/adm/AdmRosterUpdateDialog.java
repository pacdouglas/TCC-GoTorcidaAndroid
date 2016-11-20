package br.com.gotorcida.gotorcida.dialog.adm;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.CollectionUtils;
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.utils.StringWithTag;
import br.com.gotorcida.gotorcida.webservice.GetRequest;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static android.app.Activity.RESULT_OK;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_ATHLETE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_POSITIONS_BY_SPORT;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_UPDATE_ATHLETE;

public class AdmRosterUpdateDialog extends DialogFragment {

    private static final int RESULT_LOAD_IMAGE = 1;

    View mView;
    EditText mName;
    EditText mDateOfBirth;
    EditText mCity;
    EditText mEmail;
    EditText mWebSite;
    EditText mFacebook;
    EditText mInstagram;
    EditText mTwitter;
    EditText mNumber;
    Spinner mSpinnerPosition;
    ImageView mPhotoProfile;
    Button mBtnSelectPhoto;
    ScrollView mLayout;
    ProgressBar mProgressBar;
    Bitmap mBitmap;


    private String mTeamId;
    private String mAthleteId;

    private ArrayList<StringWithTag> positionIds;

    public AdmRosterUpdateDialog(String teamID, String athleteId){
        super();
        this.mTeamId = teamID;
        this.mAthleteId = athleteId;
        this.mBitmap = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_adm_roster_update, null);

        mProgressBar = (ProgressBar) mView.findViewById(R.id.dialog_adm_roster_update_progressbar);
        mLayout = (ScrollView) mView.findViewById(R.id.dialog_adm_roster_update_layout);
        mName = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_name);
        mDateOfBirth = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_date_of_birth);
        mDateOfBirth.addTextChangedListener(Mask.insert("##/##/####", mDateOfBirth));
        mCity = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_city);
        mEmail = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_email);
        mWebSite = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_website);
        mFacebook = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_facebook);
        mInstagram = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_instagram);
        mTwitter = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_twitter);
        mSpinnerPosition = (Spinner) mView.findViewById(R.id.dialog_adm_roster_update_spinner_positions);
        mNumber = (EditText) mView.findViewById(R.id.dialog_adm_roster_update_edittext_number);
        mPhotoProfile = (ImageView) mView.findViewById(R.id.dialog_adm_roster_update_imageview_profilephoto);

        mBtnSelectPhoto = (Button) mView.findViewById(R.id.dialog_adm_roster_update_button_selectphoto);
        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, RESULT_LOAD_IMAGE);
            }
        });

        builder.setTitle("Alterando atleta");
        LoadAthleteInfoTask loadAthleteInfoTask = new LoadAthleteInfoTask();
        loadAthleteInfoTask.execute(mTeamId);

        builder.setView(mView);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Activity activity = getActivity();

                JSONObject jsonObject = new JSONObject();
                JSONObject athleteData = new JSONObject();
                JSONObject teamAthleteData = new JSONObject();
                try {
                    athleteData.put("name", mName.getText().toString());
                    athleteData.put("birthDate", mDateOfBirth.getText().toString());
                    athleteData.put("city", mCity.getText().toString());
                    athleteData.put("emailAddress", mEmail.getText().toString());
                    athleteData.put("website", mWebSite.getText().toString());
                    athleteData.put("facebook", mFacebook.getText().toString());
                    athleteData.put("instagram", mInstagram.getText().toString());
                    athleteData.put("twitter", mTwitter.getText().toString());

                    StringWithTag position = CollectionUtils.findByName(positionIds, mSpinnerPosition.getSelectedItem().toString());
                    teamAthleteData.put("position", position.getString() + "-" + position.getTag());
                    teamAthleteData.put("number", mNumber.getText().toString());

                    jsonObject.put("teamAthleteData", teamAthleteData);
                    jsonObject.put("athleteData", athleteData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                UpdateAthleteInfoTask updateAthleteInfoTask = new UpdateAthleteInfoTask(jsonObject);
                updateAthleteInfoTask.execute();

                Fragment fragment = getTargetFragment();
                Integer targetRequestCode = getTargetRequestCode();

                Intent intent = activity.getIntent();

                fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent);
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


    public class LoadAthleteInfoTask extends AsyncTask {

        private JSONObject athlete;
        private JSONArray positions;

        protected void onPreExecute() {
            mLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_ATHLETE, mAthleteId, mTeamId);
            getRequest.execute();
            JSONObject json = getRequest.getMessage().getData();
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getContext(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    this.athlete = new JSONObject(json.getString("athlete"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getRequest = new GetRequest(URL_SERVER_JSON_LIST_POSITIONS_BY_SPORT, params[0].toString());
            getRequest.execute();
            json = getRequest.getMessage().getData();
            try {
                if(getRequest.getMessage().getSystem().getInt("code") == 500) {
                    Toast.makeText(getContext(), getRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    this.positions = json.getJSONArray("positions");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Object result) {

            ArrayList<String> positionsStringList = new ArrayList<String>();
            ArrayAdapter adapter = null;

            positionIds = new ArrayList<StringWithTag>();

            if (positions != null && positions.length() > 0) {
                try {
                    for (int i = 0; i < positions.length(); i ++) {
                        JSONObject position = new JSONObject(positions.getString(i));
                        positionsStringList.add(position.getString("description"));
                        positionIds.add(new StringWithTag(position.getString("description"), position.getString("initials")));
                    }

                    adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            positionsStringList);

                    mSpinnerPosition.setAdapter(adapter);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            try {
                mName.setText(athlete.getString("name"));

                String position = athlete.getString("position").substring(0, athlete.getString("position").indexOf("-"));
                if (!position.equals(null)) {
                    int spinnerPosition = adapter.getPosition(position);
                    mSpinnerPosition.setSelection(spinnerPosition);
                }

                mNumber.setText(athlete.getString("number"));

                mDateOfBirth.setText(athlete.getString("formatedRegistrationDate"));
                mCity.setText(athlete.getString("city"));
                mEmail.setText(athlete.getString("emailAddress"));
                mWebSite.setText(athlete.getString("website"));
                mFacebook.setText(athlete.getString("facebook"));
                mInstagram.setText(athlete.getString("instagram"));
                mTwitter.setText(athlete.getString("twitter"));

                String urlImage = athlete.getString("urlImage");

                if (!urlImage.equals("") && !urlImage.equals("null") && !urlImage.isEmpty()) {
                    Glide.with(getContext()).load(URL_IMAGES_BASE + athlete.getString("urlImage") + ".png").into(mPhotoProfile);
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            mLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public class UpdateAthleteInfoTask extends AsyncTask {

        private final JSONObject postParameters;

        public UpdateAthleteInfoTask(JSONObject postParameters) {
            this.postParameters = postParameters;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            if(mBitmap != null){
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                try {
                    postParameters.put("image", encoded);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            PostRequest postRequest;
            postRequest = new PostRequest(URL_SERVER_JSON_UPDATE_ATHLETE + "/" + mTeamId + "/" + mAthleteId);
            postRequest.execute(postParameters.toString());
            return null;
        }

        @Override
        public void onPostExecute(Object result) {

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

            mBitmap = BitmapFactory.decodeFile(picturePath);
            mPhotoProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
