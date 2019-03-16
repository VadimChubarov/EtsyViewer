package com.example.vadim.EtsyViewer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface API {
        String ApiKey =  BuildConfig.ak;

        String findAllTopCategory = "taxonomy/categories";

        String findAllListingActive = "listings/active";

        String findAllListingImages = "listings/:listing_id/images";


        @GET(findAllTopCategory+"?api_key="+ApiKey)
        Observable<Category> findAllTopCategory();

        @GET(findAllListingActive+"?api_key="+ApiKey)
        Observable<Listing> findAllListingActive(@Query("category") String category, @Query("keywords") String keywords, @Query("limit") Integer limit ,@Query("page") Integer page);

        @GET(findAllListingImages+"?api_key="+ApiKey)
        Observable<ListingImage> findAllListingImages (@Query("listing_id") Integer listing_id);
    }
