package info.mansk.easyfeed;

/**
 * Created by Ahmed on 9/15/2016.
 */
public class ListItem {

    public ListItem(String imageURL, String title, String description, String date, String link){
        this.ImageURL   =   imageURL;
        this.Title      =   title;
        this.Description    =   description;
        this.Date       =   date;
        this.Link       =   link;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String ImageURL;
    public String Title;
    public String Description;
    public String Date;

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String Link;
}
