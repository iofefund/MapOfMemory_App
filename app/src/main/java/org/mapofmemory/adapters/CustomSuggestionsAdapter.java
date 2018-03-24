package org.mapofmemory.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import org.mapofmemory.R;
import org.mapofmemory.entities.MonumentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The Tronuo on 23.03.2018.
 */

public class CustomSuggestionsAdapter extends SuggestionsAdapter<String, CustomSuggestionsAdapter.SuggestionHolder> implements Filterable{
    public interface OnSuggestionClickListener{
        void onClick(String monumentEntity);
    }
    private OnSuggestionClickListener onSuggestionClickListener;

    public void setOnSuggestionClickListener(OnSuggestionClickListener onSuggestionClickListener) {
        this.onSuggestionClickListener = onSuggestionClickListener;
    }

    public CustomSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public int getSingleViewHeight() {
        return 80;
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.suggest_item, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(String suggestion, SuggestionHolder holder, int position) {
        holder.title.setText(suggestion);
        holder.itemView.setOnClickListener((View v) -> onSuggestionClickListener.onClick(suggestion));
        //holder.subtitle.setText("The price is " + suggestion.getPrice() + "$");
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (String item: suggestions_clone) {
                        if (item.toLowerCase().contains(term.toLowerCase()))
                            suggestions.add(item);
                    }
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (List<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    static class SuggestionHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        public SuggestionHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.suggestion_text);
        }
    }

}
