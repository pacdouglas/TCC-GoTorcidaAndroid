package br.com.gotorcida.gotorcida.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class CollectionUtils {

    public static StringWithTag findByName(List<StringWithTag> list, String description) {

        StringWithTag searchItem = null;

        for (int i = 0; i <= list.size(); i++){
            if (list.get(i).getString().equals(description)) {
                searchItem = list.get(i);
                break;
            }
        }

        return searchItem;
    }

    public static boolean ValidateFields(Context context, EditText... params){
        for(int i = 0; i < params.length-1; i++){
            EditText editText = params[i];
            if(editText.getText().toString().isEmpty()){
                Toast.makeText(context, "Os campos com * são obrigatórios", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}