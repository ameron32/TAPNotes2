package com.ameron32.apps.tapnotes.v2.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.data.model.ISearchableBible;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/17/2015.
 */
public class ScreenGridAdapter extends RecyclerView.Adapter<ScreenGridAdapter.ViewHolder> {

  private int BOOK_COUNT = 0;
  private int CHAPTER_COUNT = 0;
  private int VERSE_COUNT = 0;

  public static final int SCREEN_BOOKS = 0;
  public static final int SCREEN_CHAPTERS = 1;
  public static final int SCREEN_VERSES = 2;

  private final int screenType;

  public ScreenGridAdapter(int screen, ISearchableBible bible) {
    this.bible = bible;
    if (screen != SCREEN_BOOKS) {
      throw new IllegalStateException("wrong constructor used for that screen");
    }
    screenType = SCREEN_BOOKS;

    initBooks();
  }

  public ScreenGridAdapter(int screen, ISearchableBible bible, int bookNumber) {
    this.bible = bible;
    if (screen != SCREEN_CHAPTERS) {
      throw new IllegalStateException("wrong constructor used for that screen");
    }
    screenType = SCREEN_CHAPTERS;

    initBooks();
    initChapters(bookNumber);
  }

  public ScreenGridAdapter(int screen, ISearchableBible bible, int bookNumber, int chapterNumber) {
    this.bible = bible;
    if (screen != SCREEN_VERSES) {
      throw new IllegalStateException("wrong constructor used for that screen");
    }
    screenType = SCREEN_VERSES;

    initBooks();
    initChapters(bookNumber);
    initVerses(bookNumber, chapterNumber);
  }

  private ISearchableBible bible;

  private void initBooks() {
    BOOK_COUNT = bible.getBookCount();
  }

  private void initChapters(int bookNumber) {
    CHAPTER_COUNT = bible.getChapterCount(bookNumber);
  }

  private void initVerses(int bookNumber, int chapterNumber) {
    VERSE_COUNT = bible.getVerseCount(bookNumber, chapterNumber);
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
    if (isPositionSelected(position)) {
      holder.itemView.setSelected(true);
    } else {
      holder.itemView.setSelected(false);
    }
    holder.itemView.invalidate();

    if (screenType == SCREEN_BOOKS) {
      final String abbr = bible.getChapterAbbrev(position);
      Log.d("onBind", "abbr: " + abbr);
      holder.textButton.setText(abbr);
    } else {
      holder.textButton.setText(Integer.toString(position + 1));
    }

    holder.textButton.setOnClickListener(
        new PositionListener(screenType, position)
            .setCallbacks(mCallbacks));
  }

  private boolean isPositionSelected(int position) {
    switch(screenType) {
      case SCREEN_BOOKS:
        final int book = mCallbacks.getCurrentBook();
        if (position == book) {
          return true;
        }
        break;
      case SCREEN_CHAPTERS:
        final int chapter = mCallbacks.getCurrentChapter();
        if (chapter == position) {
          return true;
        }
        break;
      case SCREEN_VERSES:
        if (mCallbacks.getCurrentVerses().contains(position)) {
          return true;
        }
      default:
        // do nothing
    }
    return false;
  }

  @Override
  public int getItemCount() {
    switch(screenType) {
      case SCREEN_BOOKS:
        return BOOK_COUNT;
      case SCREEN_CHAPTERS:
        return CHAPTER_COUNT;
      case SCREEN_VERSES:
        return VERSE_COUNT;
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
    int getCurrentBook();
    int getCurrentChapter();
    List<Integer> getCurrentVerses();
  }
}
