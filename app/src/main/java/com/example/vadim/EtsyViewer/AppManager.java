package com.example.vadim.EtsyViewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class AppManager implements MainInterface.Presenter
{
    private CoreProcess coreProcess;
    private MainActivity mainActivity;
    private static AppManager appManager;
    private AppListener appListener;
    private ArrayList<RecyclerItemData> searchResults;
    private SwipeRefreshLayout currentSearchLayout;
    private SelectableRecyclerAdapter currentRecyclerAdapter;

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

    public void setCurrentSearchLayout(SwipeRefreshLayout currentSearchLayout) {this.currentSearchLayout = currentSearchLayout;}

    public SwipeRefreshLayout getCurrentSearchLayout() {return currentSearchLayout;}

    public SelectableRecyclerAdapter getCurrentRecyclerAdapter() {return currentRecyclerAdapter;}

    public void setCurrentRecyclerAdapter(SelectableRecyclerAdapter currentRecyclerAdapter) {this.currentRecyclerAdapter = currentRecyclerAdapter;}


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
    }

    public void createNextPageOfSearchResults(String category, String keywords)
    {
        if(coreProcess.isReadyForSearch() && coreProcess.getNextListingPage()!=0)
        {
            String nextPage = String.valueOf(coreProcess.getNextListingPage());
            new ApiRequestManager().execute("Pagination",category,keywords,nextPage);
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
    public void deleteListing(int listingId)
    {
        coreProcess.deleteListing(listingId);

    }

    public void deleteSelectedListings(List<RecyclerItemData> selectedItems)
    {
        for(RecyclerItemData item : selectedItems)
        {
            deleteListing(item.getListingId());
        }
    }



    public class AppListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
                   {
                       case R.id.submit: if(coreProcess.isReadyForSearch()) {createListOfSearchResults();}break;

                       case R.id.SaveItemButton:

                           int listingId = mainActivity.getItemDetailsIntent().getIntExtra("id",0);
                           saveListing(getSearchResultsItem(listingId));

                           Toast toast = Toast.makeText(mainActivity,"Item saved", Toast.LENGTH_SHORT);
                           toast.show();
                   }
        }
    }
}
