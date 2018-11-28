package com.example.vadim.EtsyViewer;


public class RecyclerItemData
{
    boolean isSelected;
    private int listingId;
    private String header;
    private String description;
    private String imageURL;
    private String pictureURL;
    private String price;


    public RecyclerItemData(int listingId ,String header, String description, String imageURL, String pictureURL, String price)
    {
        this.listingId = listingId;
        this.header = header;
        this.description = description;
        this.imageURL = imageURL;
        this.pictureURL = pictureURL;
        this.price = "$ "+price;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getListingId() {return listingId;}

    public void setListingId(int listingId) {this.listingId = listingId;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecyclerItemData that = (RecyclerItemData) o;

        if (listingId != that.listingId) return false;
        if (!header.equals(that.header)) return false;
        if (!description.equals(that.description)) return false;
        if (!imageURL.equals(that.imageURL)) return false;
        if (!pictureURL.equals(that.pictureURL)) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = listingId;
        result = 31 * result + header.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + imageURL.hashCode();
        result = 31 * result + pictureURL.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }
}
