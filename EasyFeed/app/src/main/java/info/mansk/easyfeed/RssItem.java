package info.mansk.easyfeed;

/**
 * Created by Ahmed on 9/15/2016.
 */
public class RssItem {
    private String ImageURL;

    public RssItem() {
    }

    public RssItem(String imageURL, String title, String itemURL, String description, String date) {
        ImageURL = imageURL;
        Title = title;
        ItemURL = itemURL;
        Description = description;
        Date    =   date;
    }

    private String Title;
    private String ItemURL;
    private String Description;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    private String Date;
    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getItemURL() {
        return ItemURL;
    }

    public void setItemURL(String itemURL) {
        ItemURL = itemURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
