package thedezine.android.model;


public class ProfileData {

    private String ID;
    private String image;
    private String name;
    private String desi;
    private String fb;
    private String email;


    public ProfileData(String iD, String image, String name, String desi, String fb, String email) {
        this.ID = iD;
        this.image = image;
        this.name = name;
        this.desi = desi;
        this.fb = fb;
        this.email = email;
    }

    public String getiD() {
        return ID;
    }

    public void setiD(String iD) {
        this.ID = iD;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesi() {
        return desi;
    }

    public void setDesi(String desi) {
        this.desi = desi;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
