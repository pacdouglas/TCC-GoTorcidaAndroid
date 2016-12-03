package br.com.gotorcida.gotorcida.activity.user;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.GetRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_IMAGES_BASE;
import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_JSON_FIND_ATHLETE;

public class AthleteInfoActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView athleteNameAndAge;
    TextView athletePositionNumber;
    TextView athleteWebsite;
    TextView athleteEmail;
    String athleteId;

    ImageView athletePhoto;
    ImageView athleteFacebook;
    ImageView athleteTwitter;
    ImageView athleteInstagram;

    private String facebookLink;
    private String twitterLink;
    private String instagramLink;
    private String teamId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_info);
        Bundle bundle = getIntent().getExtras();
        athleteId = bundle.getString("athleteId");
        teamId = bundle.getString("teamId");
        athleteNameAndAge = (TextView) findViewById(R.id.athlete_textview_name_and_age);
        athleteEmail = (TextView) findViewById(R.id.athlete_textview_teamemail);
        athletePositionNumber = (TextView) findViewById(R.id.athlete_textview_position_number);
        athleteWebsite = (TextView) findViewById(R.id.athlete_textview_website);
        athletePhoto = (ImageView) findViewById(R.id.athlete_imageview_perfil_photo);
        athleteFacebook = (ImageView) findViewById(R.id.athlete_imageview_facebook);
        athleteTwitter = (ImageView) findViewById(R.id.athlete_imageview_twitter);
        athleteInstagram = (ImageView) findViewById(R.id.athlete_imageview_instagram);
        progressBar = (ProgressBar) findViewById(R.id.athlete_perfil_progress);

        athleteFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookAppLink;
                PackageManager packageManager = getPackageManager();

                String url = "http://www.facebook.com/";
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        facebookAppLink =  "fb://facewebmodal/f?href=" + url + facebookLink;
                    } else {
                        facebookAppLink =  "fb://page/" + url + facebookLink;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    facebookAppLink = url + facebookLink;
                }

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(facebookAppLink));
                startActivity(facebookIntent);
            }
        });

        athleteTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=" + twitterLink));
                    startActivity(intent);

                }catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/#!/" + twitterLink)));
                }
            }
        });

        athleteInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/" + instagramLink);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + instagramLink)));
                }
            }
        });

        AthleteInfoTask athleteInfoTask = new AthleteInfoTask();
        athleteInfoTask.execute();
    }


    public class AthleteInfoTask extends AsyncTask{

        JSONObject athlete;
        boolean success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            athleteFacebook.setVisibility(View.GONE);
            athleteTwitter.setVisibility(View.GONE);
            athleteInstagram.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            success = true;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            GetRequest getRequest = new GetRequest(URL_SERVER_JSON_FIND_ATHLETE, athleteId, teamId);
            getRequest.execute();

            try {
                if (getRequest.getMessage().getSystem().get("code").equals(200)) {
                    athlete = new JSONObject(getRequest.getMessage().getData().getString("athlete"));
                }else{
                   success = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(success){
                try {
                    String auxImg = athlete.getString("urlImage");
                    if(!auxImg.equals("null") && !auxImg.isEmpty() && !auxImg.equals("")){
                        Glide.with(AthleteInfoActivity.this).load(URL_IMAGES_BASE + auxImg +".png").asBitmap().centerCrop().into(new BitmapImageViewTarget(athletePhoto) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                athletePhoto.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    }
                    athleteNameAndAge.setText(athlete.getString("name") + ",  " + athlete.getString("age") + " anos");

                    String position = athlete.getString("position");
                    position = position.substring(0, position.indexOf("-"));

                    athletePositionNumber.setText(position + " #" + athlete.getString("number"));
                    athleteWebsite.setText(athlete.getString("website"));
                    athleteEmail.setText(athlete.getString("emailAddress"));

                    if (!athlete.getString("facebook").equals("")) {
                        athleteFacebook.setVisibility(View.VISIBLE);
                        facebookLink = athlete.getString("facebook");
                    }

                    if (!athlete.getString("twitter").equals("")) {
                        athleteTwitter.setVisibility(View.VISIBLE);
                        twitterLink = athlete.getString("twitter");
                    }

                    if (!athlete.getString("instagram").equals("")) {
                        athleteInstagram.setVisibility(View.VISIBLE);
                        instagramLink = athlete.getString("instagram");
                    }

                    try {
                        athleteWebsite.append(athlete.getString("website"));
                        Linkify.addLinks(athleteWebsite, Linkify.WEB_URLS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        athleteEmail.append(athlete.getString("emailAddress"));
                        Linkify.addLinks(athleteEmail, Linkify.EMAIL_ADDRESSES);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }else{
                Toast.makeText(AthleteInfoActivity.this, "Erro de comunicação com o servidor. Tente novamente mais tarde", Toast.LENGTH_LONG).show();
            }
        }
    }

}
