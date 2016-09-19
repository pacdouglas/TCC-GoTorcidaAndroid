package br.com.gotorcida.gotorcida.model;

/**
 * Created by dougl on 19/09/2016.
 */

public class Sport {
    private int id;
    private String name;

    public Sport(int id, String name){
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
