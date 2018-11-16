package com.example.vadim.EtsyViewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SerachTabFragment extends Fragment
{
    private Spinner spinner;
    private EditText searchBar;

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
}
