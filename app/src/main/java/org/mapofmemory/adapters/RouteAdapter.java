package org.mapofmemory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.RouteInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 21.01.2018.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteHolder> {
    private List<RouteInfo> routeInfos;

    public class RouteHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.header) TextView header;
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.message) TextView message;
        private Context context;
        public RouteHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            this.context = v.getContext();
        }

        public Context getContext(){
            return context;
        }
    }


    public RouteAdapter(List<RouteInfo> routeInfos) {
        this.routeInfos = routeInfos;
    }

    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        RouteHolder vh = new RouteHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RouteHolder holder, int position) {
        RouteInfo routeInfo = routeInfos.get(position);
        holder.icon.setBackground(routeInfo.getIcon());
        holder.header.setText(routeInfo.getHeader());
        holder.message.setText(routeInfo.getMessage());
    }

    @Override
    public int getItemCount() {
        return routeInfos.size();
    }
}
