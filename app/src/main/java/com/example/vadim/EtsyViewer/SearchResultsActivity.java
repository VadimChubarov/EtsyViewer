package com.example.vadim.EtsyViewer;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import java.util.Collection;

public class SearchResultsActivity extends AppCompatActivity {
    private SwipeRefreshLayout searchSwipeLayout;
    private RecyclerView searchResultsRecycler;
    private SimpleRecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ViewGroup noSearchResultsImage;
    private ProgressBar progressBar;
    private String category;
    private String keyWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        setUpToolbar();
        receiveData();
        initViews();
        runSearchResultsRecycler();
        showRecyclerItems(AppManager.getInstance().getSearchResults());
        AppManager.getInstance().getMainActivity().setCurrentSearchScreen(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().getMainActivity().setCurrentSearchScreen(null);
    }

    private void setUpToolbar(){
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
    }

    private void receiveData(){
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        keyWords = intent.getStringExtra("keyWords");
    }

    private void initViews(){
        searchSwipeLayout = findViewById(R.id.search_swipe_layout);
        searchResultsRecycler = findViewById(R.id.search_recycler);
        noSearchResultsImage = findViewById(R.id.vg_no_search_results_image);
        noSearchResultsImage.setOnClickListener(new SearchResultsScreenListener());
        progressBar = findViewById(R.id.pagination_loading);
        searchSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

    private void runSearchResultsRecycler() {
        searchSwipeLayout.setOnRefreshListener(() ->
                AppManager.getInstance().refreshListOfSearchResults(category, keyWords));

        layoutManager = new LinearLayoutManager(this);
        searchResultsRecycler.setLayoutManager(layoutManager);
        recyclerAdapter = new SimpleRecyclerAdapter();
        searchResultsRecycler.setAdapter(recyclerAdapter);

        OnScrollListener scrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                    AppManager.getInstance().createNextPageOfSearchResults(category, keyWords);
                }
            }
        };
        searchResultsRecycler.setOnScrollListener(scrollListener);
    }

    public void showProgressBar(String type, boolean show) {
        switch (type) {
            case "pagination":
                if (show) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.getLayoutParams().height = progressBar.getWidth();
                    progressBar.requestLayout();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.getLayoutParams().height = 0;
                    progressBar.requestLayout();
                }
                break;

            case "refresh":
                if (show) {
                    searchSwipeLayout.setRefreshing(true);
                } else {
                    searchSwipeLayout.setRefreshing(false);
                }
                break;
        }
    }

    protected void showRecyclerItems(Collection<RecyclerItemData> recyclerItems) {
        if (recyclerItems.size() > 0) {
            recyclerAdapter.setItems(recyclerItems);
        } else {
            showNoSearchResult(true);
        }
    }

    public void showNoSearchResult(boolean show) {
        if (show) {
            searchResultsRecycler.setVisibility(View.INVISIBLE);
            noSearchResultsImage.setVisibility(View.VISIBLE);
        } else {
            noSearchResultsImage.setVisibility(View.GONE);
            searchResultsRecycler.setVisibility(View.VISIBLE);
        }
    }

    private class SearchResultsScreenListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.vg_no_search_results_image:
                    AppManager.getInstance().getAppListener().onNoSearchResultsClick();
                    break;
            }
        }

    }
}

