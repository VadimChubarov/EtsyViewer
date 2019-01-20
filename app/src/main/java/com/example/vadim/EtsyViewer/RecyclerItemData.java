package com.example.vadim.EtsyViewer;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RecyclerItemData implements Serializable
{
    private boolean isSelected;
    private int listingId;
    private String header;
    private String description;
    private String imageURL;
    private String [] pictureURL;
    private ArrayList<String> fullscreenURL;
    private ArrayList<Integer> fullscreenId;
    private String price;
    private String currency;


    public RecyclerItemData(int listingId ,
                            String header,
                            String description,
                            String imageURL,
                            String[] picturesURL,
                            ArrayList<String> fullScreenURL,
                            ArrayList<Integer> fullscreenId,
                            String price,
                            String currency)
    {
        this.listingId = listingId;
        this.header = header;
        this.description = description;
        this.imageURL = imageURL;
        this.pictureURL = picturesURL;
        this.fullscreenURL = fullScreenURL;
        this.fullscreenId = fullscreenId;
        this.price = price;
        this.currency = currency;

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

    public String[] getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String []pictureURL) {
        this.pictureURL = pictureURL;
    }

    public ArrayList<String> getFullscreenURL() {
        return fullscreenURL;
    }

    public void setFullscreenURL(ArrayList<String> fullscreenURL) {
        this.fullscreenURL = fullscreenURL;
    }

    public ArrayList<Integer> getFullscreenId() {
        return fullscreenId;
    }

    public void setFullscrenId(ArrayList<Integer> fullscrenId) {
        this.fullscreenId = fullscrenId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return currency+" "+price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecyclerItemData that = (RecyclerItemData) o;
        return isSelected == that.isSelected &&
                listingId == that.listingId &&
                Objects.equals(header, that.header) &&
                Objects.equals(description, that.description) &&
                Objects.equals(imageURL, that.imageURL) &&
                Arrays.equals(pictureURL, that.pictureURL) &&
                Objects.equals(fullscreenURL, that.fullscreenURL) &&
                Objects.equals(fullscreenId, that.fullscreenId) &&
                Objects.equals(price, that.price) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(isSelected, listingId, header, description, imageURL, fullscreenURL, fullscreenId, price, currency);
        result = 31 * result + Arrays.hashCode(pictureURL);
        return result;
    }
}
