package com.jakob.tonsleymaps;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* Written by Jakob Pennington
 *
 * A Fragment to to display maps, including titles and legends
 */
public class MapViewFragment extends Fragment {

    public MapViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

}
