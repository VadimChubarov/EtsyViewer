package com.example.vadim.EtsyViewer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements MainInterface.View
{
    private SerachTabFragment searchTabFragment;
    private FavoritesTabFragment favoritesTabFragment;
    private ProgressDialog progressDialog;
    private Intent searchResultsIntent;
    private Intent itemDetailsIntent;
    private Intent fullScreenIntent;
    private WeakReference<SearchResultsActivity> currentSearchScreenRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createTabViewPager();

        searchResultsIntent = new Intent(this,SearchResultsActivity.class);
        itemDetailsIntent = new Intent(this,ItemDetailsActivity.class);
        fullScreenIntent = new Intent(this,FullscreenActivity.class);

        progressDialog = LoadingDialogManager.getProgressDialog("Loading...",false,this);

        AppManager appManager = AppManager.getInstance(this);
        appManager.createListOfCategories();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.onDestroy();
    }

    private void createTabViewPager() {
        searchTabFragment = new SerachTabFragment();
        favoritesTabFragment = new FavoritesTabFragment();

        ViewPager tabViewPager = findViewById(R.id.viewpager);
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager(),this);
        tabViewPagerAdapter.addFragment(searchTabFragment,"Find products",R.drawable.magnifying_glass1);
        tabViewPagerAdapter.addFragment(favoritesTabFragment, "Favorites",R.drawable.star1);
        tabViewPager.setAdapter(tabViewPagerAdapter);

        TabLayout tabLayout =  findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabLayout.getTabAt(0).setCustomView(tabViewPagerAdapter.getTabView(0));
        tabLayout.getTabAt(1).setCustomView(tabViewPagerAdapter.getTabView(1));
    }

    public void showLoadingDialog(boolean show)
    {
        if (show)
        {progressDialog.show();}
        else {progressDialog.dismiss();}
    }

    public Intent getItemDetailsIntent() {return itemDetailsIntent;}

    public Intent getFullScreenIntent() {return fullScreenIntent;}

    public void showCategories(String[] categories)
    {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_item_selected,categories);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        searchTabFragment.setSpinner(spinnerAdapter);
        searchTabFragment.showProgressBar(false);
    }

    @Override
    public String getSearchKeyWord()
    {
        return searchTabFragment.getSearchBar().getText().toString();
    }

    public String getSelectedCategory()
    {
        return searchTabFragment.getSpinner().getSelectedItem().toString();
    }

    @Override
    public void showSearchScreen()
    {
        searchResultsIntent.putExtra("category",getSelectedCategory());
        searchResultsIntent.putExtra("keyWords",getSearchKeyWord());

        startActivity(searchResultsIntent);
    }

    public void showItemDetailsScreen(int listingId)
    {
        itemDetailsIntent.putExtra("id",listingId);
        startActivity(itemDetailsIntent);
    }

    public void showFullScreen(int pictureId, RecyclerItemData picturesData) {
        fullScreenIntent.putExtra("pictureId",pictureId);
        fullScreenIntent.putExtra("picturesData",picturesData);
        startActivity(fullScreenIntent);
    }

    public SearchResultsActivity getCurrentSearchScreen() {return currentSearchScreenRef.get();}

    public void setCurrentSearchScreen(SearchResultsActivity currentSearchScreen)
    {   if(currentSearchScreen!=null){this.currentSearchScreenRef = new WeakReference<>(currentSearchScreen);}
        else{this.currentSearchScreenRef.clear();}
    }
}






