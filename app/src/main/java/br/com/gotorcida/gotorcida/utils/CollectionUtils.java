package br.com.gotorcida.gotorcida.utils;

import java.util.List;

/**
 * Created by Ricardo Zanardo on 19/11/2016.
 */

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
}
