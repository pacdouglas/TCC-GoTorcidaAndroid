package br.com.gotorcida.gotorcida.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.utils.CollectionUtils;
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_NEW_USER;

public class RegisterActivity extends AppCompatActivity {

    private Button btnSignup;
    private EditText edtFullname;
    private TextInputLayout edtPassword;
    private TextInputLayout edtPasswordConfirm;
    private EditText email;
    private EditText edtDateOfBirth;


    boolean register;
    boolean focus;
    Intent it;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = false;
        focus = false;
        edtDateOfBirth = (EditText) findViewById(R.id.input_dateOfBirth);
        edtDateOfBirth.addTextChangedListener(Mask.insert("##/##/####", edtDateOfBirth));

        edtFullname = (EditText) findViewById(R.id.input_name);
        edtPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        edtPasswordConfirm = (TextInputLayout) findViewById(R.id.input_layout_password_confirm);
        email = (EditText) findViewById(R.id.input_email);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        it = new Intent();
        bundle = new Bundle();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getEditText().getText().toString().equals(edtPasswordConfirm.getEditText().getText().toString())){
                    PostRequest service = new PostRequest(URL_SERVER_NEW_USER);
                    JSONObject userData = new JSONObject();

                    if(CollectionUtils.ValidateFields(RegisterActivity.this, email, edtPassword.getEditText(), edtFullname, edtDateOfBirth)){
                        try {
                            userData.put("emailAddress", email.getText().toString());
                            userData.put("password", edtPassword.getEditText().getText().toString());
                            userData.put("fullName", edtFullname.getText().toString());
                            userData.put("dateOfBirth", edtDateOfBirth.getText().toString());

                            RegisterUserTask saveDashboardOptionsTask = new RegisterUserTask(userData);
                            saveDashboardOptionsTask.execute();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "As senhas não são iguais", Toast.LENGTH_SHORT).show();
                }

            }
        });

        edtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!focus){
                    edtDateOfBirth.setText("");
                    focus = true;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class RegisterUserTask extends AsyncTask {
        private final JSONObject postParameters;

        public RegisterUserTask(JSONObject postParameters){
            this.postParameters = postParameters;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest = new PostRequest(URL_SERVER_NEW_USER);
            postRequest.execute(postParameters.toString());

            try {
                if (postRequest.getMessage().getSystem().get("code").equals(200)) {
                    register = true;
                    bundle.putString("user", postParameters.getString("emailAddress"));
                    bundle.putString("password", postParameters.getString("password"));
                    it.putExtras(bundle);
                    setResult(99, it);

                } else {
                    register = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(register)
                finish();
            else
                Toast.makeText(RegisterActivity.this, "Erro interno da aplicação", Toast.LENGTH_LONG).show();
        }
    }
}