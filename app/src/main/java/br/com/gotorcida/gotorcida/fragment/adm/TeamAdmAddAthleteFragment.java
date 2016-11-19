package br.com.gotorcida.gotorcida.fragment.adm;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.gotorcida.gotorcida.R;

import static android.app.Activity.RESULT_OK;

public class TeamAdmAddAthleteFragment extends Fragment{
    private static final int RESULT_LOAD_IMAGE = 1997;

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
    public TeamAdmAddAthleteFragment(String teamId) {
        this.mTeamId = teamId;
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

            mPhotoProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_adm_add_athlete, container, false);

        mName = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_name);
        mDateOfBirth = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_date_of_birth);
        mCity = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_city);
        mEmail = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_email);
        mWebSite = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_website);
        mFacebook = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_facebook);
        mInstagram = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_instagram);
        mTwitter = (EditText) mView.findViewById(R.id.adm_add_athlete_edittext_twitter);

        mPhotoProfile = (ImageView) mView.findViewById(R.id.adm_add_athlete_imageview_profilephoto);
        mBtnSelectPhoto = (Button) mView.findViewById(R.id.adm_add_athlete_button_selectphoto);
        mBtnSend = (Button) mView.findViewById(R.id.adm_add_athlete_button_send);

        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.getText().toString().isEmpty()) {
                    Toast.makeText(mView.getContext(), "SAPORRA", Toast.LENGTH_LONG).show();
                }
            }
        });



        return mView;
    }

}
