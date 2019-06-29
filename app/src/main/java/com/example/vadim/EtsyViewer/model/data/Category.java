
package com.example.vadim.EtsyViewer.model.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category
{

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("results")
    @Expose
    private List<CategoryItem> results = null;
    @SerializedName("params")
    @Expose
    private Object params;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("pagination")
    @Expose
    private ListingImage.ListingImagePagination pagination;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CategoryItem> getResults() {
        return results;
    }

    public void setResults(List<CategoryItem> results) {
        this.results = results;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ListingImage.ListingImagePagination getPagination() {
        return pagination;
    }

    public void setPagination(ListingImage.ListingImagePagination pagination) {
        this.pagination = pagination;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for(CategoryItem categoryItem : results){
            names.add(categoryItem.getName().replace("_"," "));
        }
        return names;
    }


    public class CategoryItem
    {
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("meta_title")
        @Expose
        private String metaTitle;
        @SerializedName("meta_keywords")
        @Expose
        private String metaKeywords;
        @SerializedName("meta_description")
        @Expose
        private String metaDescription;
        @SerializedName("page_description")
        @Expose
        private String pageDescription;
        @SerializedName("page_title")
        @Expose
        private String pageTitle;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("short_name")
        @Expose
        private String shortName;
        @SerializedName("long_name")
        @Expose
        private String longName;
        @SerializedName("num_children")
        @Expose
        private Integer numChildren;

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMetaTitle() {
            return metaTitle;
        }

        public void setMetaTitle(String metaTitle) {
            this.metaTitle = metaTitle;
        }

        public String getMetaKeywords() {
            return metaKeywords;
        }

        public void setMetaKeywords(String metaKeywords) {
            this.metaKeywords = metaKeywords;
        }

        public String getMetaDescription() {
            return metaDescription;
        }

        public void setMetaDescription(String metaDescription) {
            this.metaDescription = metaDescription;
        }

        public String getPageDescription() {
            return pageDescription;
        }

        public void setPageDescription(String pageDescription) {
            this.pageDescription = pageDescription;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getLongName() {
            return longName;
        }

        public void setLongName(String longName) {
            this.longName = longName;
        }

        public Integer getNumChildren() {
            return numChildren;
        }

        public void setNumChildren(Integer numChildren) {
            this.numChildren = numChildren;
        }
    }

    public class CategoryPagination{}

}


