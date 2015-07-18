package com.ameron32.apps.tapnotes.v2.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ScripturePickerLayoutFragmentDelegate;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public class ScripturePickerAdapter extends PagerAdapter {

  private static final int NUM_OF_ITEMS_PER_ROW = 3;

  private final Context context;

  public ScripturePickerAdapter(Context context) {
    this.context = context;
  }

  private OnPageNextClickedListener mListener;

  public void setOnPageNextClickedListener(OnPageNextClickedListener listener) {
    this.mListener = listener;
  }

  public static class PageHolder {

    @InjectView(R.id.button_forward)
    Button buttonNext;
    @InjectView(R.id.recycler_view)
    RecyclerView gridView;

    public PageHolder(View v) {
      ButterKnife.inject(this, v);
    }
  }

  @Override
  public Object instantiateItem(ViewGroup container, final int position) {
    final View child = LayoutInflater.from(container.getContext()).inflate(R.layout.item_recycler_grid, container, false);
    PageHolder holder = new PageHolder(child);
    holder.buttonNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mListener != null) {
          mListener.onPageNextClicked(position);
        }
      }
    });

    holder.gridView.setLayoutManager(new GridLayoutManager(context, NUM_OF_ITEMS_PER_ROW));
    holder.gridView.setAdapter(new ScreenGridAdapter());
    container.addView(child, position);
    return child;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object child) {
    ButterKnife.reset(child);
    container.removeViewAt(position);
  }

  @Override
  public int getCount() {
    return ScripturePickerLayoutFragmentDelegate.NUM_OF_PAGES;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }



  public interface OnPageNextClickedListener {

    void onPageNextClicked(int page);
  }
}
