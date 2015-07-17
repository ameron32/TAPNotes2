package com.ameron32.apps.tapnotes.v2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public class ScreenGridAdapter extends RecyclerView.Adapter<ScreenGridAdapter.ViewHolder> {

  private static final int BOOK_COUNT = 66;

  public ScreenGridAdapter() {
    // TODO needs book/chapter/verse information from outside
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_box, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.text.setText("" + position);
  }

  @Override
  public int getItemCount() {
    return BOOK_COUNT;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.text1)
    TextView text;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }
}
