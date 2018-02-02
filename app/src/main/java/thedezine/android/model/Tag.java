package thedezine.android.model;

/**
 * Created by Tushar Saha on 12/11/15.
 * Audacity IT Solutions Ltd.
 */
public class Tag {

    private String id;
    private String platform;
    private String url;

    public Tag() {

    }

    public Tag(String platform) {
        this.platform = platform;
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}
