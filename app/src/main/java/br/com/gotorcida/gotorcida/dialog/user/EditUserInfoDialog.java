package br.com.gotorcida.gotorcida.dialog.user;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.gotorcida.gotorcida.R;
import br.com.gotorcida.gotorcida.activity.user.HomeUserActivity;
import br.com.gotorcida.gotorcida.utils.Mask;
import br.com.gotorcida.gotorcida.utils.SaveSharedPreference;
import br.com.gotorcida.gotorcida.webservice.PostRequest;

import static br.com.gotorcida.gotorcida.utils.Constants.URL_SERVER_UPDATE_USER;

public class EditUserInfoDialog extends DialogFragment {
    View mView;

    EditText mNickname, mOldPassword, mNewPassword, mRepeatNewPassword, mCelNumber;

    JSONObject mParameters = null;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_edit_user_info, null);
        builder.setView(mView);

        mNickname = (EditText) mView.findViewById(R.id.edit_user_info_edittext_nickname);
        mOldPassword = (EditText) mView.findViewById(R.id.edit_user_info_edittext_oldpassword);
        mNewPassword = (EditText) mView.findViewById(R.id.edit_user_info_edittext_newpassword);
        mRepeatNewPassword = (EditText) mView.findViewById(R.id.edit_user_info_edittext_repeatnewpassword);
        mCelNumber = (EditText) mView.findViewById(R.id.edit_user_info_edittext_celnumber);
        mCelNumber.addTextChangedListener(Mask.insert("(##)#-####-####", mCelNumber));

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!mNewPassword.getText().toString().equals(mRepeatNewPassword.getText().toString())){
                    Toast.makeText(getContext(), "Senha inválida", Toast.LENGTH_LONG).show();
                }else{
                    mParameters = new JSONObject();
                    try {
                        mParameters.put("nickname", mNickname.getText().toString());
                        mParameters.put("oldPassword", mOldPassword.getText().toString());
                        mParameters.put("newPassword", mNewPassword.getText().toString());
                        mParameters.put("celNumber", mCelNumber.getText().toString());

                        UserUpdateTask userUpdateTask = new UserUpdateTask();
                        userUpdateTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    public class UserUpdateTask extends AsyncTask{
        boolean test;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            test = false;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PostRequest postRequest = new PostRequest(URL_SERVER_UPDATE_USER + "/" + SaveSharedPreference.getUserName(getActivity()));
            test = postRequest.execute(mParameters.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(test){
                Toast.makeText(mView.getContext(), "Dados alterados com sucesso", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(mView.getContext(), "Erro: Dados incorretos", Toast.LENGTH_LONG).show();
            }
        }
    }
}
