package br.com.gotorcida.gotorcida.activity.user;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_NEW_USER;

public class RegisterActivity extends AppCompatActivity {

    private DatePicker dpResult;
    private Button btnSignup;

    private EditText edtFullname;
    private TextInputLayout edtPassword;
    private EditText email;

    private EditText edtDateOfBirth;

    private int year;
    private int month;
    private int day;
    boolean register;
    static final int DATE_DIALOG_ID = 999;
    Intent it;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = false;

        dpResult = (DatePicker) findViewById(R.id.dpResult);
        edtDateOfBirth = (EditText) findViewById(R.id.input_dateOfBirth);
        edtFullname = (EditText) findViewById(R.id.input_name);
        edtPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        email = (EditText) findViewById(R.id.input_email);
        addListenerOnButton();

        btnSignup = (Button) findViewById(R.id.btn_signup);
        it = new Intent();
        bundle = new Bundle();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostRequest service = new PostRequest(URL_SERVER_NEW_USER);
                JSONObject userData = new JSONObject();

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
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void addListenerOnButton() {
        edtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(RegisterActivity.this, edtDateOfBirth);
                showDialog(DATE_DIALOG_ID);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth + 1;
            day = selectedDay;

            String stringDay = String.valueOf(day).length() > 1 ? String.valueOf(day) :  "0" + String.valueOf(day);
            String stringMonth = String.valueOf(month).length() > 1 ? String.valueOf(month) :  "0" + String.valueOf(month);
            String stringYear = String.valueOf(year);

            edtDateOfBirth.setText(stringDay + "/" + stringMonth + "/" + stringYear);
            // set selected date into datepicker also
            dpResult.init(year, month, day, null);

        }
    };

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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
                    Toast.makeText(RegisterActivity.this, postRequest.getMessage().getSystem().get("message").toString(), Toast.LENGTH_SHORT).show();
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
        }
    }
}