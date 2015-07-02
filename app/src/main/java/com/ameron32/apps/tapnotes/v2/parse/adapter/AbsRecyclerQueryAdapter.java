package com.ameron32.apps.tapnotes.v2.parse.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter.QueryFactory;

import java.util.ArrayList;
import java.util.List;

import bolts.Capture;

/**
 * ParseQueryAdapter modified for RecyclerView.
 * Includes AutoReloadable interface.
 * @param <T> Object type to load. extends ParseObject
 * @param <U> Typical RecyclerView.ViewHolder subclass for custom ViewHolding
 */
public abstract class
      AbsRecyclerQueryAdapter
        <T extends ParseObject, U extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<U>
    implements AutoReloadable
{


  private final QueryFactory<T> mFactory;
  private final boolean hasStableIds;

  private final List<T> mItems;

  // PRIMARY CONSTRUCTOR
  public AbsRecyclerQueryAdapter( final QueryFactory<T> factory, final boolean hasStableIds) {
    mFactory = factory;
    mItems = new ArrayList<T>();
    mDataSetListeners = new ArrayList<OnDataSetChangedListener>();
    mQueryListeners = new ArrayList<OnQueryLoadListener<T>>();
    this.hasStableIds = hasStableIds;

    setHasStableIds(hasStableIds);
    loadObjects();
  }

  // ALTERNATE CONSTRUCTOR
  public AbsRecyclerQueryAdapter(final String className, final boolean hasStableIds) {
    this(new QueryFactory<T>() {

      @Override
      public ParseQuery<T> create() {
        return ParseQuery.getQuery(className);
      }
    }, hasStableIds);
  }

  // ALTERNATE CONSTRUCTOR
  public AbsRecyclerQueryAdapter(final Class<T> clazz, final boolean hasStableIds) {
    this(new QueryFactory<T>() {

      @Override
      public ParseQuery<T> create() {
        return ParseQuery.getQuery(clazz);
      }
    }, hasStableIds);
  }


  /*
   *  REQUIRED RECYCLERVIEW METHOD OVERRIDES
   */

  @Override
  public long getItemId(int position) {
    if (hasStableIds) {
      return position;
    }
    return super.getItemId(position);
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  public T getItem(int position) {
    return mItems.get(position);
  }


  public List<T> getItems() {
    return mItems;
  }


  protected void onFilterQuery(ParseQuery<T> query) {
    // provide override for filtering query
  }

  public void loadObjects() {
    dispatchOnLoading();
    final ParseQuery<T> query = mFactory.create();
    onFilterQuery(query);
    query.findInBackground(new FindCallback<T>() {

      @Override
      public void done(
          List<T> queriedItems,
          ParseException e) {
        if (e == null) {
          mItems.clear();
          mItems.addAll(queriedItems);
//          mItems = queriedItems;
          dispatchOnLoaded(queriedItems, e);
          notifyDataSetChanged();
          fireOnDataSetChanged();
        }
      }
    });
  }

  public interface OnDataSetChangedListener {
    public void onDataSetChanged();
  }


  private final List<OnDataSetChangedListener> mDataSetListeners;

  public void addOnDataSetChangedListener(OnDataSetChangedListener listener) {
    if (!mDataSetListeners.contains(listener)) {
      mDataSetListeners.add(listener);
    }
  }

  public void removeOnDataSetChangedListener(OnDataSetChangedListener listener) {
    if (mDataSetListeners.contains(listener)) {
      mDataSetListeners.remove(listener);
    }
  }

  protected void fireOnDataSetChanged() {
    for (int i = 0; i < mDataSetListeners.size(); i++) {
      mDataSetListeners.get(i).onDataSetChanged();
    }
  }

  public interface OnQueryLoadListener<T> {

    public void onLoaded(
        List<T> objects, Exception e);

    public void onLoading();
  }


  private final List<OnQueryLoadListener<T>> mQueryListeners;

  public void addOnQueryLoadListener(
      OnQueryLoadListener<T> listener) {
    if (!(mQueryListeners.contains(listener))) {
      mQueryListeners.add(listener);
    }
  }

  public void removeOnQueryLoadListener(
      OnQueryLoadListener<T> listener) {
    if (mQueryListeners.contains(listener)) {
      mQueryListeners.remove(listener);
    }
  }

  private void dispatchOnLoading() {
    for (OnQueryLoadListener<T> l : mQueryListeners) {
      l.onLoading();
    }
  }

  private void dispatchOnLoaded(List<T> objects, ParseException e) {
    for (OnQueryLoadListener<T> l : mQueryListeners) {
      l.onLoaded(objects, e);
    }
  }

  public static class LimitReachedException extends Exception {
    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public LimitReachedException() {
      super();
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public LimitReachedException(String detailMessage) {
      super(detailMessage);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace, the
     * specified detail message and the specified cause.
     *
     * @param detailMessage the detail message for this exception.
     * @param throwable
     */
    public LimitReachedException(String detailMessage, Throwable throwable) {
      super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified cause.
     *
     * @param throwable the cause of this exception.
     */
    public LimitReachedException(Throwable throwable) {
      super(throwable);
    }
  }
}
