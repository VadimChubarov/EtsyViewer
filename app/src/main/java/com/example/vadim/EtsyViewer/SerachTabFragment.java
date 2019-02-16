package com.example.vadim.EtsyViewer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;


public class SerachTabFragment extends Fragment {

    private Spinner spinner;
    private ImageView spinnerImage;
    private EditText searchBar;
    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab_fragment, container, false);

        spinner = view.findViewById(R.id.spinner1);
        spinnerImage = view.findViewById(R.id.spinner_image);
        Button submitButton = view.findViewById(R.id.submit);
        searchBar = view.findViewById(R.id.search_bar1);
        progressBar = view.findViewById(R.id.categories_progress);

        if(spinner.getAdapter()==null){showProgressBar(true);}
        SearchTabClickListener searchTabClickListener = new SearchTabClickListener();
        submitButton.setOnClickListener(searchTabClickListener);

        return view;
    }

    public void setSpinner(ArrayAdapter<String> adapter) {
        DropDownListLimiter listLimiter = new DropDownListLimiter();
        listLimiter.setMaxDropDownHeight(this.getContext(),spinner,0.5);
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

    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
         spinnerImage.setVisibility(View.INVISIBLE);
        }
        else {
                progressBar.setVisibility(View.INVISIBLE);
             spinnerImage.setVisibility(View.VISIBLE);
            }
    }

    private class SearchTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.submit:
                    AppManager.getInstance().getAppListener().onSearchSubmitClick();
                    break;
            }
        }
    }
}
