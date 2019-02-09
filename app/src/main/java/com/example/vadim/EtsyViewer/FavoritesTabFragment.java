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
    private SelectableRecyclerAdapter recyclerAdapter;
    private ViewGroup addFavorites;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment,
                container, false);

        favoritesRecycler = view.findViewById(R.id.favorites_recycler);
        addFavorites = view.findViewById(R.id.vg_add_favorites_image);
        addFavorites.setOnClickListener(new FavoritesTabListener());
        runFavoritesRecycler();
        showRecyclerItems();

        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        showRecyclerItems();
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerAdapter.getRecyclerActionMode().activateActionMode(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser && recyclerAdapter!=null){recyclerAdapter.getRecyclerActionMode().activateActionMode(false);}
    }

    private void runFavoritesRecycler() {
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerAdapter = new SelectableRecyclerAdapter();
        recyclerAdapter.setSelectionColor("#33c43c00");
        favoritesRecycler.setAdapter(recyclerAdapter);
        new RecyclerAnimator().setDeleteAnimationSpeed(favoritesRecycler,250);
    }

    public void showRecyclerItems() {
        recyclerAdapter.clearItems();
        if(AppManager.getInstance().getSavedListings().size()>0) {
            recyclerAdapter.setItems(AppManager.getInstance().getSavedListings());
            showAddFavorites(false);
        }
        else showAddFavorites(true);
    }

    public void showAddFavorites(boolean show){
        if(show){
            favoritesRecycler.setVisibility(View.INVISIBLE);
            addFavorites.setVisibility(View.VISIBLE);
        }
        else{
            addFavorites.setVisibility(View.GONE);
            favoritesRecycler.setVisibility(View.VISIBLE);
        }
    }

    private class FavoritesTabListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.vg_add_favorites_image :
                    AppManager.getInstance().getAppListener().onAddFavoritesClick();
                    break;
            }
        }
    }
}
