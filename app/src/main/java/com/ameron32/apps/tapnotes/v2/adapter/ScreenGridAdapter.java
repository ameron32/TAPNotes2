package com.ameron32.apps.tapnotes.v2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public class ScreenGridAdapter extends RecyclerView.Adapter<ScreenGridAdapter.ViewHolder> {

  private static final int BOOK_COUNT = 66;
  private static final int FAKE_CHAPTER_COUNT = 5;
  private static final int FAKE_VERSE_COUNT = 10;

  public static final int SCREEN_BOOKS = 0;
  public static final int SCREEN_CHAPTERS = 1;
  public static final int SCREEN_VERSES = 2;

  private final int screenType;

  public ScreenGridAdapter(int screen) {
    // TODO needs book/chapter/verse information from outside
    if (screen != SCREEN_BOOKS) {
      throw new IllegalStateException("wrong constructor used for that screen");
    }
    screenType = SCREEN_BOOKS;


  }

  public ScreenGridAdapter(int screen, int bookNumber) {
    // TODO needs book/chapter/verse information from outside
    if (screen != SCREEN_CHAPTERS) {
      throw new IllegalStateException("wrong constructor used for that screen");
    }
    screenType = SCREEN_CHAPTERS;


  }

  public ScreenGridAdapter(int screen, int bookNumber, int chapterNumber) {
    // TODO needs book/chapter/verse information from outside
    if (screen != SCREEN_VERSES) {
      throw new IllegalStateException("wrong constructor used for that screen");
    }
    screenType = SCREEN_VERSES;


  }

  private Callbacks mCallbacks;

  public ScreenGridAdapter setCallbacks(Callbacks callbacks) {
    this.mCallbacks = callbacks;
    return this;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_grid_box, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textButton.setText("" + position);
    holder.textButton.setOnClickListener(
        new PositionListener(screenType, position)
            .setCallbacks(mCallbacks));
  }

  @Override
  public int getItemCount() {
    switch(screenType) {
      case SCREEN_BOOKS:
        return BOOK_COUNT;
      case SCREEN_CHAPTERS:
        return FAKE_CHAPTER_COUNT;
      case SCREEN_VERSES:
        return FAKE_VERSE_COUNT;
    }
    return 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.text_button)
    Button textButton;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }

  public static class PositionListener implements View.OnClickListener {

    private final int screenType;
    private final int position;
    private Callbacks callbacks;

    public PositionListener(int screenType, int position) {
      this.screenType = screenType;
      this.position = position;
    }

    public PositionListener setCallbacks(Callbacks callbacks) {
      this.callbacks = callbacks;
      return this;
    }

    @Override
    public void onClick(View v) {
      if (callbacks != null) {
        switch (screenType) {
          case SCREEN_BOOKS:
            callbacks.bookSelected(position);
            break;
          case SCREEN_CHAPTERS:
            callbacks.chapterSelected(position);
            break;
          case SCREEN_VERSES:
            callbacks.verseSelected(position);
            break;
        }
      }
    }
  }



  public interface Callbacks {

    void bookSelected(int bookNumber);
    void chapterSelected(int chapterNumber);
    void verseSelected(int verseNumber);
  }
}
