package com.jakob.tonsleymaps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/* Written by Jakob Pennington
 *
 * An Adapter to fill a RecyclerView with data from the database
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private LayoutInflater inflater;
    private ClickListener clickListener;
    List<SearchResult> searchResultList = Collections.emptyList();

    public SearchAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    // A viewHolder is created for each visible item in the RecyclerView, plus a few spares
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchItemView = inflater.inflate(R.layout.search_item, parent, false);
        SearchViewHolder holder = new SearchViewHolder(searchItemView);
        return holder;
    }

    // Bind a SearchResult to a ViewHolder when the View becomes visible
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        SearchResult currentLocation = searchResultList.get(position);
        holder.mTitle.setText(currentLocation.getTitle());
        holder.mDescription.setText(currentLocation.getDescription());
        holder.itemView.setTag(currentLocation);
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }

    // Update the List when the user types a query into the search bar
    public void setSearchResultList(List<SearchResult> list){
        // Update the list then refresh the RecyclerView
        this.searchResultList = list;
        notifyDataSetChanged();
    }

    // ViewHolder to display an object from the database
    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mDescription;

        public SearchViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.search_item_title);
            mDescription = (TextView) itemView.findViewById(R.id.search_item_description);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void itemClicked (View view, int position);
    }
}
