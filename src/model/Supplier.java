package model;

public class Supplier {
    private final String id;
    private final String name;
    private String contactInfo;
    private boolean isActive;

    // Class constructor
    public Supplier(String id, String name, String contactInfo, boolean isActive) {
        this.id= id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.isActive = isActive;
    }

    // Class methods
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
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        this.isActive = active;
    }
    public String getDisplayName() {
        return name + " (" + id + ")";
    }
}