package com.ameron32.apps.tapnotes.v2.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.IScripture;
import com.ameron32.apps.tapnotes.v2.scripture.Scripture;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ScripturePickerLayoutFragmentDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public class ScripturePickerAdapter extends PagerAdapter
    implements ScreenGridAdapter.Callbacks {

  private int NUM_OF_ITEMS_PER_ROW = 5;

  public static final int NUM_OF_PAGES = 3;
  public static final int SCREEN_BOOKS = 0;
  public static final int SCREEN_CHAPTERS = 1;
  public static final int SCREEN_VERSES = 2;

  public ScripturePickerAdapter() {

  }

  private int book = -1;
  private int chapter = -1;
  private List<Integer> verses = new ArrayList<>(1);
  private Scripture scripture;

  private OnPageNextClickedListener mListener;

  public void setOnPageNextClickedListener(OnPageNextClickedListener listener) {
    this.mListener = listener;
  }

  @Override
  public void bookSelected(int bookNumber) {
    book = bookNumber;
    mListener.onPageNextClicked(SCREEN_BOOKS, scripture);
  }

  @Override
  public void chapterSelected(int chapterNumber) {
    chapter = chapterNumber;
    mListener.onPageNextClicked(SCREEN_CHAPTERS, scripture);
  }

  @Override
  public void verseSelected(int verseNumber) {
    verses.add(verseNumber);
  }

  public Scripture attemptMakeScripture() {
    if (book >= 0 && chapter >= 0 && verses.size() >= 1) {
      scripture = Scripture.generate(book, chapter, fromVerses());
    }
    return null;
  }

  private int[] fromVerses() {
    final int[] vs = new int[verses.size()];
    for (int i = 0; i < vs.length; i++) {
      vs[i] = verses.get(i);
    }
    return vs;
  }

  public static class PageHolder {

    @InjectView(R.id.button_forward)
    Button buttonNext;
    @InjectView(R.id.recycler_view)
    RecyclerView gridView;

    private View itemView;

    public PageHolder(View v) {
      itemView = v;
      ButterKnife.inject(this, v);
    }

    public View getItemView() {
      return itemView;
    }
  }

  private static final int NOT_FOUND = -1;

  private final PageHolder[] holders = new PageHolder[NUM_OF_PAGES];

  @Override
  public Object instantiateItem(ViewGroup container, final int position) {
    NUM_OF_ITEMS_PER_ROW = container.getContext()
        .getResources()
        .getInteger(R.integer.number_of_items_per_row);

    PageHolder holder = holders[position];
    if (holder == null) {
      holder = createHolder(container);
      holders[position] = holder;
    }

    // bind
    bindHolder(position);
    container.addView(holder.getItemView());
    return holder.getItemView();
  }

  public void focusPage(int position) {
    bindHolder(position);
  }

  private void bindHolder(int position) {
    PageHolder holder = holders[position];
    bindHolder(holder, position);
  }

  private PageHolder createHolder(ViewGroup container) {
    final View child = LayoutInflater.from(container.getContext()).inflate(R.layout.item_recycler_grid, container, false);
    final PageHolder holder = new PageHolder(child);
    return holder;
  }

  private void bindHolder(PageHolder holder, int position) {
    holder.buttonNext.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (mListener != null) {
              attemptMakeScripture();
              mListener.onPageNextClicked(position, scripture);
            }
          }
        });

    holder.gridView.setLayoutManager(
        new GridLayoutManager(holder.gridView.getContext(), NUM_OF_ITEMS_PER_ROW));

    switch(position) {
      case SCREEN_BOOKS:
        holder.buttonNext.setVisibility(View.GONE);
        holder.gridView.setAdapter(
            new ScreenGridAdapter(SCREEN_BOOKS)
                .setCallbacks(this));
        break;
      case SCREEN_CHAPTERS:
        holder.buttonNext.setVisibility(View.GONE);
        if (book == NOT_FOUND) {
          holder.gridView.setAdapter(null);
        } else {
          holder.gridView.setAdapter(
              new ScreenGridAdapter(SCREEN_CHAPTERS, book)
                  .setCallbacks(this));
        }
        break;
      case SCREEN_VERSES:
        holder.buttonNext.setVisibility(View.VISIBLE);
        if (book == NOT_FOUND || chapter == NOT_FOUND) {
          holder.gridView.setAdapter(null);
        } else {
          holder.gridView.setAdapter(
              new ScreenGridAdapter(SCREEN_VERSES, book, chapter)
                  .setCallbacks(this));
        }
        break;
    }
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object child) {
    ButterKnife.reset(child);
    container.removeViewAt(position);
  }

  @Override
  public int getCount() {
    return NUM_OF_PAGES;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }



  public interface OnPageNextClickedListener {

    void onPageNextClicked(int page, @Nullable IScripture scripture);
  }
}
