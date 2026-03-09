package model;
import java.util.*;

public class Supplier {
    private final String id;
    private final String name;
    private String contactInfo;

    // Class constructor
    public Supplier(String id, String name, String contact){
        this.id= id;
        this.name = name;
        this.contactInfo = contact;
    }

    //Class methods
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getContactInfo() {
        return contactInfo;
    }
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}