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
        runFavoritesRecycler();
        showRecyclerItems();

        return myFragmentView ;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        showRecyclerItems();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        recyclerAdapter.getRecyclerActionMode().activateActionMode(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser && recyclerAdapter!=null){recyclerAdapter.getRecyclerActionMode().activateActionMode(false);}
    }

    private void runFavoritesRecycler()
    {
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerAdapter = new SelectableRecyclerAdapter();
        recyclerAdapter.setSelectionColor("#33c43c00");
        favoritesRecycler.setAdapter(recyclerAdapter);
        new RecyclerAnimator().setDeleteAnimationSpeed(favoritesRecycler,250);
    }

    public void showRecyclerItems()
    {
        recyclerAdapter.clearItems();
        if(AppManager.getInstance().getSavedListings().size()>0)
        {recyclerAdapter.setItems(AppManager.getInstance().getSavedListings());}
    }

}
