package br.com.gotorcida.gotorcida.fragment.user;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_LIST_TEAMS;

@SuppressLint("ValidFragment")
public class TeamContactFragment extends Fragment {

    View mView;
    TextView mTeamWebSite;
    TextView mTeamEmail;
    TextView mTeamCity;
    ImageView mTeamFacebook;
    ImageView mTeamTwitter;
    ImageView mTeamInstagram;
    private String mTeamId;

    private String mFacebookLink = "";
    private String mTwitterLink = "";
    private String mInstagramLink = "";

    public TeamContactFragment(String teamId) {
        this.mTeamId = teamId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_team_contact, container, false);

        mTeamWebSite = (TextView) mView.findViewById(R.id.team_contact_textview_website);
        mTeamEmail = (TextView) mView.findViewById(R.id.team_contact_textview_email);
        mTeamCity = (TextView) mView.findViewById(R.id.team_contact_textview_city);
        mTeamFacebook = (ImageView) mView.findViewById(R.id.team_contact_imageview_facebook);
        mTeamTwitter = (ImageView) mView.findViewById(R.id.team_contact_imageview_twitter);
        mTeamInstagram = (ImageView) mView.findViewById(R.id.team_contact_imageview_instagram);


        mTeamFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookAppLink;
                PackageManager packageManager = getContext().getPackageManager();

                String url = "http://www.facebook.com/";
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        facebookAppLink =  "fb://facewebmodal/f?href=" + url + mFacebookLink;
                    } else {
                        facebookAppLink =  "fb://page/" + url + mFacebookLink;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    facebookAppLink = url + mFacebookLink;
                }

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(facebookAppLink));
                startActivity(facebookIntent);
            }
        });

        mTeamTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=" + mTwitterLink));
                    startActivity(intent);

                }catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/#!/" + mTwitterLink)));
                }
            }
        });

        mTeamInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/" + mInstagramLink);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + mInstagramLink)));
                }
            }
        });
        LoadTeamData loadTeamData = new LoadTeamData();
        loadTeamData.execute();
        return mView;
    }

    private class LoadTeamData extends AsyncTask {
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
                String aux = team.getString("facebook");
                if(aux.isEmpty() || aux.compareTo("null") == 0){
                    mTeamFacebook.setVisibility(View.GONE);
                }else{
                    mFacebookLink = aux;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String aux = team.getString("twitter");
                if(aux.isEmpty() || aux.compareTo("null") == 0){
                    mTeamTwitter.setVisibility(View.GONE);
                }else{
                    mTwitterLink = aux;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String aux = team.getString("instagram");
                if(aux.isEmpty() || aux.compareTo("null") == 0){
                    mTeamInstagram.setVisibility(View.GONE);
                }else{
                    mInstagramLink = aux;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mTeamCity.append(team.getString("city"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mTeamWebSite.append(team.getString("website"));
                Linkify.addLinks(mTeamWebSite, Linkify.WEB_URLS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mTeamEmail.append(team.getString("emailAddress"));
                Linkify.addLinks(mTeamEmail, Linkify.EMAIL_ADDRESSES);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
