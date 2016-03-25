package com.ameron32.apps.tapnotes.v2.uiparse.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ParseQueryAdapter modified for RecyclerView.
 * Includes AutoReloadable interface.
 * @param <T> Object type to load. extends ParseObject
 * @param <U> Typical RecyclerView.ViewHolder subclass for custom ViewHolding
 */
public abstract class
    broken_AbsPageRecyclerQueryAdapter
        <T extends ParseObject, U extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<U>
    implements AutoReloadable
{

  private final ReentrantReadWriteLock lock;

  private final ParseQueryAdapter.QueryFactory<T> mFactory;

  private final boolean paginationEnabled;
  private final int objectsPerPage;

  private final List<T> mItems;

  private int currentPage;
  private boolean hasNextPage;

  // PRIMARY CONSTRUCTOR
  public broken_AbsPageRecyclerQueryAdapter(final ParseQueryAdapter.QueryFactory<T> factory, final boolean hasStableIds) {
    lock = new ReentrantReadWriteLock(true);

    mFactory = factory;
    mItems = new ArrayList<T>();
    mDataSetListeners = new ArrayList<OnDataSetChangedListener>();
    mQueryListeners = new ArrayList<OnQueryLoadListener<T>>();
    setHasStableIds(hasStableIds);

    this.paginationEnabled = false;
    this.objectsPerPage = 1000;
    this.currentPage = 0;
    this.hasNextPage = false;
    loadObjects();
  }

  // ALTERNATE CONSTRUCTOR
  public broken_AbsPageRecyclerQueryAdapter(final String className, final boolean hasStableIds) {
    this(new ParseQueryAdapter.QueryFactory<T>() {

      @Override
      public ParseQuery<T> create() {
        return ParseQuery.getQuery(className);
      }
    }, hasStableIds);
  }

  // ALTERNATE CONSTRUCTOR
  public broken_AbsPageRecyclerQueryAdapter(final Class<T> clazz, final boolean hasStableIds) {
    this(new ParseQueryAdapter.QueryFactory<T>() {

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
    if (hasStableIds()) {
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

  protected void onFilterQuery(ParseQuery<T> query) {
    // provide override for filtering query
  }

  protected boolean isQueryFromLocalDatastore() {
    return false;
  }

  public void loadObjects() {
    loadObjects(0, true);
  }

  private void loadObjects(final int page, final boolean shouldClear) {

    // onLoading()
    dispatchOnLoading();

    // get query
    final ParseQuery<T> query = mFactory.create();

    // onFilterQuery()
    onFilterQuery(query);
    if (isQueryFromLocalDatastore()) {
      query.fromLocalDatastore();
    }

    // set query page
    if (page >= getCurrentPage()) {
      setPageOnQuery(page, query);
    }

    // (local page cache)? create a new page for caching results

    // findQuery
    query.findInBackground(new FindCallback<T>() {
      @Override
      public void done(
          List<T> queriedItems,
          ParseException e){
        if (e == null && (queriedItems == null || queriedItems.isEmpty()) ||
            determineQueryFailed(query, e)) {
          // nothing to do
          return;
        }

        if (e != null &&
            (e.getCode() == ParseException.CONNECTION_FAILED ||
                e.getCode() != ParseException.CACHE_MISS)) {
          // determine if there is another page, and set hasNextPage accordingly
          setHasNextPage(true);
          dispatchOnLoaded(queriedItems, e);
          return;
        }

        if (e != null) {
          Log.e(getClass().getSimpleName(), e.getLocalizedMessage());
          return;
        }

        // if this is the firstCallback and shouldClear is true, clear cache
        if (shouldClear) {
          mItems.clear();
          setCurrentPage(page);
        }

        // adjust currentPage for new page
        if (page >= getCurrentPage()) {
          setCurrentPage(page);
          setHasNextPage(determineHasNextPage(queriedItems, query));
        }

        // setHasNextPage() for page-overflow


        // cache the page
        mItems.addAll(queriedItems);

        // notifyDataSetChanged()
        notifyDataSetChanged();
        fireOnDataSetChanged();
        // onLoaded()
        dispatchOnLoaded(queriedItems, e);
      }
    });
  }

  private boolean determineQueryFailed(final ParseQuery<T> query, final ParseException e) {
    return (query.getCachePolicy() != ParseQuery.CachePolicy.CACHE_ONLY ||
        e == null || e.getCode() != 120);
  }

  private boolean determineHasNextPage(final List<T> queriedItems, final ParseQuery<T> query) {
    return query.getLimit() == queriedItems.size();
  }

  protected void setPageOnQuery(int page, ParseQuery<T> query) {
    query.setLimit(this.objectsPerPage + 1);
    query.setSkip(page * this.objectsPerPage);
  }


  public interface OnDataSetChangedListener {
    public void onDataSetChanged();
  }



  private final List<OnDataSetChangedListener> mDataSetListeners;

  public void addOnDataSetChangedListener(OnDataSetChangedListener listener) {
    mDataSetListeners.add(listener);
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



  public List<T> getItems() {
    return mItems;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public boolean hasNextPage() {
    return hasNextPage;
  }

  public void setHasNextPage(boolean hasNextPage) {
    this.hasNextPage = hasNextPage;
  }
}

