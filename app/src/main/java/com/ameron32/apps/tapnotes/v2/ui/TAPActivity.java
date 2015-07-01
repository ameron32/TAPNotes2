package com.ameron32.apps.tapnotes.v2.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.di.Injector;
import com.ameron32.apps.tapnotes.v2.di.controller.ApplicationThemeController;
import com.ameron32.apps.tapnotes.v2.di.module.ActivityModule;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidActivityModule;
import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.lifecycle.LifecycleEvent;
import rx.android.lifecycle.LifecycleObservable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by klemeilleur on 6/18/2015.
 */
public class TAPActivity extends AppCompatActivity {

  private static final int NO_LAYOUT_RESOURCE = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Injector.INSTANCE.inject(this);
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    initializeActivityModules_Dagger1();
    lifecycleSubject.onNext(LifecycleEvent.CREATE);
    setContentView(findContentView(getLayoutResource()));
    setTheme(provideThemeResource());
    ButterKnife.inject(this);
  }

  private void initializeActivityModules_Dagger1() {
    Injector.INSTANCE.init(new ActivityModule(this));
    Injector.INSTANCE.init(new DefaultAndroidActivityModule(this));
  }

  @Override
  protected void onDestroy() {
    ButterKnife.reset(this);
    lifecycleSubject.onNext(LifecycleEvent.DESTROY);
    super.onDestroy();
  }

  protected @LayoutRes int getLayoutResource() {
    return NO_LAYOUT_RESOURCE;
  }

  protected @StyleRes int provideThemeResource() {
    return R.style.Teal2015Theme;
  }

  private View findContentView(@LayoutRes int contentLayout) {
    if (contentLayout == NO_LAYOUT_RESOURCE) {
      return new View(getContext());
    }
    return LayoutInflater.from(getContext()).inflate(contentLayout, (ViewGroup) getView(), false);
  }



  protected Context getContext() {
    return getActivity();
  }

  protected Activity getActivity() {
    return TAPActivity.this;
  }

  protected View getView() {
    return getWindow().getDecorView().findViewById(android.R.id.content);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }



  private final BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();

  public Observable<LifecycleEvent> lifecycle() {
    return lifecycleSubject.asObservable();
  }

  @Override
  protected void onStart() {
    super.onStart();
    lifecycleSubject.onNext(LifecycleEvent.START);
  }

  @Override
  protected void onResume() {
    super.onResume();
    lifecycleSubject.onNext(LifecycleEvent.RESUME);
  }

  @Override
  protected void onPause() {
    lifecycleSubject.onNext(LifecycleEvent.PAUSE);
    super.onPause();
  }

  @Override
  protected void onStop() {
    lifecycleSubject.onNext(LifecycleEvent.STOP);
    super.onStop();
  }



  //TODO: PR this to RxAndroid Framework
  public <T> Observable<T> bindLifecycle(Observable<T> observable, LifecycleEvent lifecycleEvent) {
    Observable<T> boundObservable = AppObservable.bindActivity(this, observable);
    return LifecycleObservable.bindUntilLifecycleEvent(lifecycle(), boundObservable, lifecycleEvent);
  }

  //TODO: PR this to RxAndroid Framework
  public <T> Observable<T> bindLifecycle(Observable<T> observable) {
    Observable<T> boundObservable = AppObservable.bindActivity(this, observable);
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
