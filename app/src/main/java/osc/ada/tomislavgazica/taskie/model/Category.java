package osc.ada.tomislavgazica.taskie.model;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Category extends RealmObject implements Serializable{

    @Required
    @PrimaryKey
    private String ID;
    private String categoryName;

    public Category(){}

    public Category(String categoryName) {
        ID = UUID.randomUUID().toString();
        this.categoryName = categoryName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
