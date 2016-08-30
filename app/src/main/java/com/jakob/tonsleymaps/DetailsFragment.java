package com.jakob.tonsleymaps;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/* Written by Jakob Pennington
 *
 * A Fragment to display details about the selected Location, Floor, Room or Occupant
 */
public class DetailsFragment extends Fragment {
    private LinearLayout buttons;
    private TextView detailTitle;
    private TextView detailDescription;
    private Button callButton;
    private Button emailButton;

    private String phoneNumber;
    private String emailAddress;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        buttons = (LinearLayout) view.findViewById(R.id.button_layout);
        detailTitle = (TextView) view.findViewById(R.id.detail_title);
        detailDescription = (TextView) view.findViewById(R.id.detail_description);
        callButton = (Button) view.findViewById(R.id.detail_button_call);
        emailButton = (Button) view.findViewById(R.id.detail_button_email);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phoneNumber));
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null));
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        return view;
    }

    // Process SearchResults passed from the MainActivity
    public void updateDetails(SearchResult searchResult){
        String resultType = searchResult.getClass().getSimpleName();
        switch (resultType){
            case "Location":
                Location location = (Location) searchResult;
                buttons.setVisibility(View.GONE);
                detailTitle.setText(location.getTitle());
                detailDescription.setText(location.getDescription());
                break;
            case "Floor":
                Floor floor = (Floor) searchResult;
                buttons.setVisibility(View.GONE);
                detailTitle.setText(floor.getTitle());
                detailDescription.setText(floor.getDescription());
                break;
            case "Room":
                Room room = (Room) searchResult;
                buttons.setVisibility(View.GONE);
                detailTitle.setText(room.getTitle());
                detailDescription.setText(room.getDescription());
                break;
            case "Occupant":
                Occupant occupant = (Occupant) searchResult;
                buttons.setVisibility(View.VISIBLE);
                phoneNumber = occupant.getPhone();
                emailAddress = occupant.getEmail();
                detailTitle.setText(occupant.getTitle());
                detailDescription.setText(occupant.getDescription());
                break;
        }
    }

    // Update the DetailFragment based on the floor selection spinner
    public void updateFromSpinner (String mapName){
        switch (mapName){
            case "t1_ground":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("Ground Floor");
                detailDescription.setText("Flinders University - T1");
                break;
            case "t1_first":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("First Floor");
                detailDescription.setText("Flinders University - T1");
                break;
            case "t1_second":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("Second Floor");
                detailDescription.setText("Flinders University - T1");
                break;
            case "t1_third":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("Third Floor");
                detailDescription.setText("Flinders University - T1");
                break;
            case "t1_forth":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("Forth Floor");
                detailDescription.setText("Flinders University - T1");
                break;
            case "t1_fifth":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("Fifth Floor");
                detailDescription.setText("Flinders University - T1");
                break;
            case "pod_ground":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("Ground Floor");
                detailDescription.setText("Flinders University - Pod");
                break;
            case "precinct":
                buttons.setVisibility(View.GONE);
                detailTitle.setText("Tonsley Precinct");
                detailDescription.setText("");
                break;
        }
    }

}
