package com.example.vadim.EtsyViewer;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.lang.reflect.Field;

public class SerachTabFragment extends Fragment
{
    private Spinner spinner;
    private EditText searchBar;
    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View myFragmentView = inflater.inflate(R.layout.search_tab_fragment,
                container, false);

        spinner = myFragmentView.findViewById(R.id.spinner1);
        Button submitButton = myFragmentView.findViewById(R.id.submit);
        searchBar = myFragmentView.findViewById(R.id.search_bar1);
        progressBar = myFragmentView.findViewById(R.id.categories_progress);

        if(spinner.getAdapter()==null){showProgressBar(true);}

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);  // to be checked !!!

            // Set popupWindow height to 500px
            popupWindow.setHeight(1700);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        submitButton.setOnClickListener(AppManager.getInstance().getAppListener());

        return myFragmentView;
    }

    public void setSpinner(ArrayAdapter<String> adapter)
    {
        this.spinner.setAdapter(adapter);
    }

    public EditText getSearchBar()
    {
        return searchBar;
    }

    public Spinner getSpinner()
    {
        return spinner;
    }

    public void showProgressBar(boolean show)
    {
        if (show)
        {progressBar.setVisibility(View.VISIBLE);}
        else {progressBar.setVisibility(View.INVISIBLE);}
    }


}
