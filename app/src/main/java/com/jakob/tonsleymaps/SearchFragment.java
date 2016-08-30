package com.jakob.tonsleymaps;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/* Written by Jakob Pennington
 *
 * A Fragment to allow users to search a database of buildings, floors, rooms and people.
 */
public class SearchFragment extends Fragment implements SearchAdapter.ClickListener {
    private DrawerLayout drawerLayout;
    private SearchAdapter searchAdapter;
    private DatabaseAdapter mDatabaseAdapter;
    private List<SearchResult> mSearchResults;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, container, false);

        // Set up the RecyclerView
        RecyclerView searchRecyclerView = (RecyclerView) layout.findViewById(R.id.search_recycler_view);
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        searchAdapter = new SearchAdapter(getActivity());
        searchAdapter.setClickListener(this);
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set up Database Adapter
        mDatabaseAdapter = new DatabaseAdapter(getActivity());
        mDatabaseAdapter.createDatabase();
        mDatabaseAdapter.open();

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up back button in Search Fragment
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.navigation_drawer_layout);
        ImageView backButton = (ImageView) getActivity().findViewById(R.id.search_bar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        SearchView searchView = (SearchView) getActivity().findViewById(R.id.search_bar);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchListener());
    }

    @Override
    public void itemClicked(View view, int position) {
        // Get the object associated with the clicked view
        SearchResult clickedObject = (SearchResult) view.getTag();
        ((MainActivity)getActivity()).processSearchResult(clickedObject);
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    // Listen for changes to text entries into the SearchBar
    public class SearchListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            //Update RecyclerView when search term is submitted
            mSearchResults = mDatabaseAdapter.submitQuery(query);
            searchAdapter.setSearchResultList(mSearchResults);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // Update the RecyclerView each time a character is added or removed
            // Remove all values if search query is blank
            if (newText.length() != 0) {
                mSearchResults = mDatabaseAdapter.submitQuery(newText);
            } else {
                mSearchResults.clear();
            }
            searchAdapter.setSearchResultList(mSearchResults);
            return false;
        }
    }
}