package com.jakob.tonsleymaps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

/* Written by Jakob Pennington
 *
 * An activity enabling users to view maps of the Tonsley Precinct and Flinders University Buildings.
 * Users can also search and view data about locations at Tonsley.
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private DetailsFragment detailsFragment;
    private MapView mapView;
    private Spinner spinner;
    private boolean spinnerFromTouch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Set up map spinner
        mapView = (MapView) findViewById(R.id.view_map);
        spinner = (Spinner) findViewById(R.id.spinner_map);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.maps_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerListener());

        detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_detail_view);
        setUpDrawerLayout(toolbar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Show keyboard if the search drawer is open
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            showKeyboard();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
    }

    private void setUpDrawerLayout(android.support.v7.widget.Toolbar toolbar) {
        // Set up navigation drawer
        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.navigation_drawer_layout), toolbar);

        // Listen for events regarding the DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Show keyboard when search drawer is opened
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    showKeyboard();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                hideKeyboard();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void showKeyboard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ActionBar menu actions
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.search_button){
            drawerLayout.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Update the drawable in the MapView and the appropriate details in DetailsFragment
            MapView mapView = (MapView) findViewById(R.id.view_map);

            // Only clear the placemarker if the spinner was changed by the user
            if(spinnerFromTouch){
                mapView.clearPlacemarker();
            }

            switch (position) {
                case 0:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("t1_ground");
                    }
                    updateMap("t1_ground");
                    break;
                case 1:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("t1_first");
                    }
                    updateMap("t1_first");
                    break;
                case 2:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("t1_second");
                    }
                    updateMap("t1_second");
                    break;
                case 3:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("t1_third");
                    }
                    updateMap("t1_third");
                    break;
                case 4:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("t1_forth");
                    }
                    updateMap("t1_forth");
                    break;
                case 5:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("t1_fifth");
                    }
                    updateMap("t1_fifth");
                    break;
                case 6:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("pod_ground");
                    }
                    updateMap("pod_ground");
                    break;
                case 7:
                    if (spinnerFromTouch){
                        detailsFragment.updateFromSpinner("precinct");
                    }
                    updateMap("precinct");
                    break;
            }
            spinnerFromTouch = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do Nothing
        }
    }

    // Update the spinner, map and legend after an item is selected in SearchFragment or in the spinner
    public void updateMap (String mapName){
        spinnerFromTouch = false;
        switch (mapName) {
            case "t1_ground":
                spinner.setSelection(0);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_ground));
                setLegend(R.drawable.legend_ground);
                break;
            case "t1_first":
                spinner.setSelection(1);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_first));
                setLegend(R.drawable.legend_first);
                break;
            case "t1_second":
                spinner.setSelection(2);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_second));
                setLegend(R.drawable.legend_second);
                break;
            case "t1_third":
                spinner.setSelection(3);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_third));
                setLegend(R.drawable.legend_third);
                break;
            case "t1_forth":
                spinner.setSelection(4);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_forth));
                setLegend(R.drawable.legend_forth);
                break;
            case "t1_fifth":
                spinner.setSelection(5);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_fifth));
                setLegend(R.drawable.legend_fifth);
                break;
            case "pod_ground":
                spinner.setSelection(6);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_pod));
                setLegend(R.drawable.legend_pod);
                break;
            case "precinct":
                spinner.setSelection(7);
                mapView.setmMap(getResources().getDrawable(R.drawable.map_precinct));
                setLegend(R.drawable.legend_precinct);
                break;
        }
    }

    private void setLegend(int resource) {
        // Update the legend to match the active map
        ImageView legend = (ImageView) findViewById(R.id.map_legend);
        legend.setImageResource(resource);
    }

    public void processSearchResult(SearchResult clickedObject){
        // Process the SearchResult selected in SearchFragment
        updateMap(clickedObject.getMapName());
        mapView.setPlacemarker(clickedObject.getMapX(), clickedObject.getMapY());
        detailsFragment.updateDetails(clickedObject);
    }
}
