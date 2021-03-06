package com.birdjunior.moviesearch.ui;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;

import com.birdjunior.moviesearch.R;
import com.birdjunior.moviesearch.databinding.ActivitySearchBinding;
import com.birdjunior.moviesearch.models.Result;
import com.birdjunior.moviesearch.network.SearchStore;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchStore.DataListener {

    private SearchViewModel viewModel;
    private ResultsArrayAdapter resultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new SearchViewModel(this, SearchViewModel.SEARCH_START, this);

        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setViewModel(viewModel);

        // Set up RecyclerView and its adapter
        resultsAdapter = new ResultsArrayAdapter();
        RecyclerView results = binding.resultsRecyclerView;
        results.setAdapter(resultsAdapter);
        results.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    viewModel.performQuery(newText);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public void onItems(@NonNull List<Result> results) {
        resultsAdapter.clear();
        resultsAdapter.add(results);
    }

    @Override
    public void onNoResults() {
        // No-op, view model handles this for us
    }
}
