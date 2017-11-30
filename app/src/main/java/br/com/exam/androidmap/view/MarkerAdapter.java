package br.com.exam.androidmap.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.model.Marker;
import br.com.exam.androidmap.presenter.MapPresenter;

public class MarkerAdapter extends RecyclerView.Adapter {

    private List<Marker> markers;
    private final Context context;
    private final MapPresenter presenter;

    public MarkerAdapter(Context context, MapPresenter presenter) {
        this.markers = presenter.getBookmarkList();
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_marker, parent, false);

        MarkerViewHolder holder = new MarkerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MarkerViewHolder markerViewHolder = (MarkerViewHolder) holder;
        Marker marker = markers.get(position);
        markerViewHolder.name.setText(marker.name);
    }

    @Override
    public long getItemId(int position) {
        return markers.get(position).id;
    }

    @Override
    public int getItemCount() {
        return markers.size();
    }

    public class MarkerViewHolder extends RecyclerView.ViewHolder {
        final TextView name;

        public MarkerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
        }
    }

    public void updateList(List<Marker> markers) {
        this.markers = markers;
        notifyDataSetChanged();
    }
}
