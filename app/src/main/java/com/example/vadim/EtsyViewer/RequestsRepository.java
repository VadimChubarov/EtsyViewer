package com.example.vadim.EtsyViewer;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestsRepository {

    private final String BASE_URL = "https://openapi.etsy.com/v2/";
    private API api;
    private Observable<Category> allCategoriesRequest;



    public RequestsRepository (){
        Retrofit retrofitService = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        this.api = retrofitService.create(API.class);
        this.allCategoriesRequest = api.findAllTopCategory();
    }

    public Observable<Category> getAllCategoriesRequest() {
        return allCategoriesRequest;
    }

    public Observable<Listing> getSearchListingsRequest(String category, String keyWords, int page) {
        return api.findAllListingActive(category,keyWords,10,page);
    }

    public Observable<ListingImage> getListnigImagesRequest(int listingId) {
        return api.findAllListingImages(listingId);
    }

}
