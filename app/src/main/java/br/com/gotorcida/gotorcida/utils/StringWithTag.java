package br.com.gotorcida.gotorcida.utils;

/**
 * Created by dougl on 02/11/2016.
 */

public class StringWithTag {
    public String string;
    public Object tag;

    public StringWithTag(String stringPart, Object tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }

    public Object getTag(){
        return tag;
    }

    public String getString() { return string; }
}
