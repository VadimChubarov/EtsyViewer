package com.example.vadim.EtsyViewer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface MainInterface
{
    interface View                                                                    // интерфейс View поддерживет класс MainActivity (главный экран)
                                                                                      // его ассистенты : FavoritesTabFragment (фрагмент со списком отобранных товаров)
                                                                                      // SearchTabFragment (фрагмент с инструментами поиска товаров)
                                                                                      // SearchResultsActivity (экран результатов поиска)
                                                                                      // ItemDetailsActivity (экран детального представления товаров)
    {
        void showCategories(String[] categories);                                     // вывод на экран списка категорий

        String getSearchKeyWord();                                                    // извлечение данных из строки поиска

        String getSelectedCategory();                                                 // извлечение данных из спиннера категорий

        void showSearchScreen();                                                      // вызов ассистента SearchResultsActivity (экран результатов поиска)

        void showItemDetailsScreen(int listingId);                                    // вызов ассистента ItemDetailsActivity (экран детального представления товаров)

    }

    interface Presenter                                                                 // интерфейс Presenter поддерживет класс AppManager (руководящий класс)
                                                                                        // его ассистенты : ApiRequestManager(отвечает за организацию действий с API)
                                                                                        // и AppListener (прослушивает события нажатия итд)
    {
       void createListOfCategories();                                                   // организация списка категорий

       void createListOfSearchResults();                                                // организация поиска товаров

       void refreshListOfSearchResults(String category, String keyWords);               // организация обновления списка найденных товаров

       void createNextPageOfSearchResults(String category, String keywords);            // огранизация пагинации списка найденных товаров

       void createDetailedScreen(int listingId);                                        // организация детального представления товара

       List<RecyclerItemData> getSavedListings();                                       // получение всех отобранныз товаров

       void saveListing(RecyclerItemData itemData);                                     // организация сохранения товара в отобранное

       void deleteListing(int listingId);                                               // организация удаления товара из отобранных

       RecyclerItemData getSavedItem (int listingId);                                   // получение товара из списка отобранных товаров

       RecyclerItemData getSearchResultsItem (int listingId);                           // получение товара из текущих результатов поиска

    }

    interface Model                                                                       // интерфейс Model поддерживает класс CoreProcess
    {
        boolean isReadyForSearch();                                                       // флаг состояния запросов на API (если выполняется запрос - false)

        String [] loadAllCategories();                                                    // запрос на получение списка категорий с API

        String[] getNamesOfCategories();                                                  // извлечение списка названий категорий из ответа API(POJO класса)

        void loadSearchListings(String categoty, String keyWords);                        // запрос на поиск товаров с API

        void loadSearchListingsPage(String categoty, String keyWords, String page);       // запрос на поиск товаров с API указанием страницы списка (pagination)

        int getNextListingPage();                                                         // получение следующей страницы из ответа API(POJO класса)

        void loadListingImageURL(int[] listingId);                                        // запросы и их обработка на получение URL изображений согласно результатов поиска

        int[] getSearchListingsId();                                                      // получение массива Id товаров по результатам текущего поиска

        RecyclerItemData getListingDetails(int listingId);                                // организация объекта c детальной информацией о товаре для RecyclerListView

        void saveListing(RecyclerItemData itemData);                                      // сохрание в БД и в избранное данный товар

        boolean isListingSaved(int listingId);                                            // проверка сохранен ли уже такой товар

        void deleteListing(int listingId);                                                // удаление из избранного и БД

        List<RecyclerItemData> getAllSavedListings();                                     // получение списка избранных товаров

        void saveSession();                                                               // сохранение данных в БД

        void recoverSession();                                                            // выгрузка из БД
    }

    interface API                                                                        // интерфейс для всех видов запросов на API
    {
        String ApiKey =  BuildConfig.ak;

        String findAllTopCategory = "taxonomy/categories";

        String findAllListingActive = "listings/active";

        String findAllListingImages = "listings/:listing_id/images";


        @GET(findAllTopCategory+"?api_key="+ApiKey)
        Call<Category> findAllTopCategory();

        @GET(findAllListingActive+"?api_key="+ApiKey)
        Call<Listing> findAllListingActive(@Query("category") String category, @Query("keywords") String keywords);

        @GET(findAllListingActive+"?api_key="+ApiKey)
        Call<Listing> findAllListingActive(@Query("category") String category, @Query("keywords") String keywords,@Query("page") Integer page);

        @GET(findAllListingImages+"?api_key="+ApiKey)
        Call<ListingImage> findAllListingImages (@Query("listing_id") Integer listing_id);
    }

    interface ResponcePOJO{}
}
