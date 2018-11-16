package com.example.vadim.EtsyViewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;


public class FavoritesTabFragment extends Fragment
{
    private RecyclerView favoritesRecycler;
    private RecyclerAdapter recyclerAdapter;
    private boolean isFavoritesScreenActive;
    private ContextMenuManager favoritesContextMenu;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View myFragmentView = inflater.inflate(R.layout.favorites_fragment,
                container, false);

        favoritesRecycler = myFragmentView.findViewById(R.id.favorites_recycler);
        runSearchResultsRecycler();
        showRecyclerItems();

        return myFragmentView ;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        showRecyclerItems();
        isFavoritesScreenActive = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        isFavoritesScreenActive = false;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        favoritesContextMenu.showContextMenu(menu,menuInfo);
    }

    private void runSearchResultsRecycler()
    {
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerAdapter = new RecyclerAdapter();
        favoritesRecycler.setAdapter(recyclerAdapter);
    }

    public void showRecyclerItems()
    {
        recyclerAdapter.clearItems();
        if(AppManager.getInstance().getSavedListings().size()>0)
        {recyclerAdapter.setItems(AppManager.getInstance().getSavedListings());}
    }

    public boolean isFavoritesScreenActive() {
        return isFavoritesScreenActive;
    }

    public void setFavoritesContextMenu(ContextMenuManager favoritesContextMenu) {
        this.favoritesContextMenu = favoritesContextMenu;
    }
}
