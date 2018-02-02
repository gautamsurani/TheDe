package thedezine.android.model;


public class ClientTag {

    private String id;
    private String platform;
    private String url;

    public ClientTag() {

    }

    public ClientTag(String platform, String url) {
        this.platform = platform;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
