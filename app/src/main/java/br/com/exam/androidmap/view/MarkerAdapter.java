package br.com.exam.androidmap.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MarkerViewHolder markerViewHolder = (MarkerViewHolder) holder;
        Marker marker = markers.get(position);
        markerViewHolder.name.setText(marker.name);

        markerViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            final int pos = position;

            @Override
            public void onClick(View v) {
                showDeleteDialog(pos);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return markers.get(position).id;
    }

    @Override
    public int getItemCount() {
        return markers.size();
    }

    public void updateList(List<Marker> markers) {
        this.markers = markers;
        notifyDataSetChanged();
    }

    public class MarkerViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView delete;

        public MarkerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            delete = view.findViewById(R.id.delete);
        }
    }

    public void showDeleteDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(R.string.dialog_delete_desc).setTitle(R.string.dialog_delete_title);

        builder.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Marker marker = markers.get(position);
                presenter.deleteBookmark(marker);
                Toast.makeText(context, context.getString(R.string.dialog_delete_ok) , Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
