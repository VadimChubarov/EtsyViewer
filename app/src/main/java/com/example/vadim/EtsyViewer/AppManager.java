package com.example.vadim.EtsyViewer;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class AppManager implements MainInterface.Presenter
{
    private CoreProcess coreProcess;
    private MainActivity mainActivity;
    private static AppManager appManager;
    private AppListener appListener;
    private ArrayList<RecyclerItemData> searchResults;

        private AppManager(MainActivity mainActivity)
        {
            this.mainActivity = mainActivity;
            this.coreProcess = new CoreProcess(mainActivity);
            this.appListener = new AppListener();
            this.searchResults = new ArrayList<>();
        }

       public static AppManager getInstance(MainActivity mainActivity)
       {
            if( appManager == null)
            {
                appManager = new AppManager(mainActivity);
            }
            return appManager;
        }

        public static void onDestroy()
        {
           appManager = null;
        }

        public static AppManager getInstance() {return appManager;}


    public CoreProcess getCoreProcess() {return coreProcess;}

    public MainActivity getMainActivity() {return mainActivity;}

    public AppListener getAppListener() {return appListener;}

    public ArrayList<RecyclerItemData> getSearchResults() {return searchResults;}

    public RecyclerItemData getSearchResultsItem (int listingId)
    {
        for(RecyclerItemData item : searchResults)
        {
            if(item.getListingId()==listingId){return item;}
        }
        return null;
    }

    public RecyclerItemData getSavedItem (int listingId)
    {
        for(RecyclerItemData item : coreProcess.getAllSavedListings())
        {
            if(item.getListingId()==listingId){return item;}
        }
        return null;
    }

    @Override
    public void createListOfCategories()
    {
       if(coreProcess.isReadyForSearch()){new ApiRequestManager().execute("Categories");}
    }

    @Override
    public void createListOfSearchResults()
    {
        String category = mainActivity.getSelectedCategory();
        String keyWords = mainActivity.getSearchKeyWord();

        if(coreProcess.isReadyForSearch() && keyWords.length()>0)
        {
            mainActivity.showLoadingDialog(true);
            new ApiRequestManager().execute("Listings",category,keyWords);
        }
    }

    public void refreshListOfSearchResults(String category, String keyWords)
    {
        if(coreProcess.isReadyForSearch()){new ApiRequestManager().execute("Refresh",category,keyWords);}
        else{mainActivity.getCurrentSearchScreen().showProgressBar("refresh",false);}
    }

    public void createNextPageOfSearchResults(String category, String keywords)
    {
        if(coreProcess.isReadyForSearch() && coreProcess.getNextListingPage()!=0)
        {
            String nextPage = String.valueOf(coreProcess.getNextListingPage());
            new ApiRequestManager().execute("Pagination",category,keywords,nextPage);
            mainActivity.getCurrentSearchScreen().showProgressBar("pagination",true);
        }
    }

    @Override
    public void createDetailedScreen(int listingId)
    {
        mainActivity.showItemDetailsScreen(listingId);
    }

    @Override
    public List<RecyclerItemData> getSavedListings()
    {
        return coreProcess.getAllSavedListings();
    }

    @Override
    public void saveListing(RecyclerItemData itemData)
    {
        coreProcess.saveListing(itemData);
    }

    @Override
    public void deleteListing(int listingId) {
        coreProcess.deleteListing(listingId);
    }

    public void deleteSelectedListings(List<RecyclerItemData> selectedItems) {
        for(RecyclerItemData item : selectedItems) {
            deleteListing(item.getListingId());
        }
        if(coreProcess.getAllSavedListings().size() == 0){
            mainActivity.getFavoritesTabFragment().showAddFavorites(true);
        }
    }

    public class AppListener
    {
        public void onSearchSubmitClick() {
            if (coreProcess.isReadyForSearch()) {
                createListOfSearchResults();
            }
        }

        public void onPictureClick (int pictureId, RecyclerItemData picturesData) {
            mainActivity.showFullScreen(pictureId,picturesData);
        }

        public void onDownloadPictureClick (Drawable picture, String pictureName) {
            Bitmap pictureBitmap = ((BitmapDrawable)picture).getBitmap();
            new ImageStorageManager().savePicture(pictureBitmap,pictureName,"Etsy_Viewer_Pictures");
        }

        public void onFavoriteListingChecked (RecyclerItemData listingItem, boolean isChecked) {
            int listingId = listingItem.getListingId();
            if(isChecked){
                if(getSavedItem(listingId)==null) {
                try{ saveListing(getSearchResultsItem(listingId)); }
                catch(NullPointerException e){saveListing(listingItem);}
                MessageService.showMessage("Item added to favorites");
                }
                else{ MessageService.showMessage("Item already in favorites"); }
            }
            else {
                deleteListing(listingId);
                MessageService.showMessage("Item deleted from favorites");
            }
        }

        public void onAddFavoritesClick() {
            mainActivity.getTabLayout().getTabAt(0).select();
        }

        public void onNoSearchResultsClick() {
            mainActivity.getCurrentSearchScreen().onBackPressed();
            mainActivity.getTabLayout().getTabAt(0);
        }
    }
}
