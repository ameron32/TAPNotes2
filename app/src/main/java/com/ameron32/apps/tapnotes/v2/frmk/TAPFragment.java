package com.ameron32.apps.tapnotes.v2.frmk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.di.Injector;
import com.ameron32.apps.tapnotes.v2.frmk.DelegatedFragment;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.DummyFragmentDelegate;

import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.lifecycle.LifecycleEvent;
import rx.android.lifecycle.LifecycleObservable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by klemeilleur on 6/12/2015.
 */
public abstract class TAPFragment extends DelegatedFragment {

  public TAPFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return DummyFragmentDelegate.create(this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Injector.INSTANCE.inject(this);
    super.onCreate(savedInstanceState);
  }

  public Context getContext() {
    return getActivity();
  }



  private final BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();

  public Observable<LifecycleEvent> lifecycle() {
    return lifecycleSubject.asObservable();
  }



  //TODO: PR this to RxAndroid Framework
  public <T> Observable<T> bindLifecycle(Observable<T> observable, LifecycleEvent lifecycleEvent) {
    Observable<T> boundObservable = AppObservable.bindFragment(this, observable);
    return LifecycleObservable.bindUntilLifecycleEvent(lifecycle(), boundObservable, lifecycleEvent);
  }

  //TODO: PR this to RxAndroid Framework
  public <T> Observable<T> bindLifecycle(Observable<T> observable) {
    Observable<T> boundObservable = AppObservable.bindFragment(this, observable);
    return LifecycleObservable.bindActivityLifecycle(lifecycle(), boundObservable);
  }



  private CompositeSubscription mCompositeSubscription;

  public void addToCompositeSubscription(Subscription s) {
    if (mCompositeSubscription == null || mCompositeSubscription.isUnsubscribed()) {
      mCompositeSubscription = new CompositeSubscription();
    }
    mCompositeSubscription.add(s);
  }

  public void unsubscribeAll() {
    mCompositeSubscription.unsubscribe();
  }
}
