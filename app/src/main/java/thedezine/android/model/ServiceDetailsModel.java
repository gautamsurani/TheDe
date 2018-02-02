package thedezine.android.model;


import java.util.List;

public class ServiceDetailsModel {

    private String message;
    private String msgcode;
    private String image;
    private String text;
    private String tech;
    private List<TechList> techList = null;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMsgcode() {
        return msgcode;
    }

    public void setMsgcode(String msgcode) {
        this.msgcode = msgcode;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public List<TechList> getTechList() {
        return techList;
    }

    public void setTechList(List<TechList> techList) {
        this.techList = techList;
    }
}
