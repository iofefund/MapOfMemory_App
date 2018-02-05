package org.mapofmemory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.mapofmemory.entities.DayOfMemory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class DayOfMemoryItemFragment extends Fragment {
    @BindView(R.id.future_dom_date) TextView dateView;
    @BindView(R.id.future_dom_title) TextView titleView;
    @BindView(R.id.future_dom_image) ImageView imageView;

    private String image, date, title;
    public static DayOfMemoryItemFragment newInstance(DayOfMemory dayOfMemory) {
        DayOfMemoryItemFragment fragmentFirst = new DayOfMemoryItemFragment();
        Bundle args = new Bundle();
        args.putString("image", dayOfMemory.getImage());
        args.putString("date", dayOfMemory.getDate());
        args.putString("title", dayOfMemory.getTitle());
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = getArguments().getString("image");
        title = getArguments().getString("title");
        date = getArguments().getString("date");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_dom, container, false);
        ButterKnife.bind(this, view);
        dateView.setText(AppConfig.convertDate(date));
        titleView.setText(title);
        Picasso.with(getActivity()).load(image).into(imageView);
        return view;
    }
}
