package com.ameron32.apps.tapnotes.v2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.frmk.OnItemClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/18/2015.
 */
public class _DummyAdapter extends RecyclerView.Adapter<_DummyAdapter.ViewHolder> {

  private OnItemClickListener mListener;

  public void setItemClickListener(OnItemClickListener l) {
    mListener = l;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
    final View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(position + "");
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.itemClicked(v, position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return 100;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @InjectView(android.R.id.text1)
    TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }
}
