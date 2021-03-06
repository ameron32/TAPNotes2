package com.ameron32.apps.tapnotes.v2.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.data.DataManager;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.parse.ParseHelper;
import com.ameron32.apps.tapnotes.v2.frmk.object.Progress;
import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IProgramDelegate;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramLayoutFragmentDelegate;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static rx.android.lifecycle.LifecycleEvent.*;

/**
 * Created by klemeilleur on 6/15/2015.
 */
public class ProgramFragment extends TAPFragment
    implements IProgramDelegate.IProgramDelegateCallbacks, Toolbar.OnMenuItemClickListener {

  private static final String PROGRAM_OBJECT_ID_ARG = "PROGRAM_OBJECT_ID_ARG";

  @Inject
  DataManager dataManager;

  private Callbacks mCallbacks;
  private IProgramDelegate mDelegate;
  private String mProgramId;

  public static ProgramFragment create(String programId) {
    final ProgramFragment f = new ProgramFragment();
    final Bundle args = new Bundle();
    args.putString(PROGRAM_OBJECT_ID_ARG, programId);
    f.setArguments(args);
    return f;
  }

  public ProgramFragment() {}

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramLayoutFragmentDelegate.create(ProgramFragment.this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupProgram();
  }

  private void setupProgram() {
    if (getArguments() == null) {
      throw new IllegalStateException("should have args bundle. Generate using factory create() method.");
    }

    final Bundle args = getArguments();
    mProgramId = args.getString(PROGRAM_OBJECT_ID_ARG);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_mni_program, container, false);
    return rootView;
  }

  @InjectView(R.id.nav_toolbar)
  Toolbar mToolbar;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    confirmDelegateHasInterface();
    ButterKnife.inject(this, view);
    inflateMenu();
    setNavigation();

    giveTalksToDelegate();
  }

  private void inflateMenu() {
    mToolbar.inflateMenu(R.menu.program_overflow_menu);
    mToolbar.setOnMenuItemClickListener(this);
  }

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.program_item_jw_library:
        openApp(getContext(), Constants.PACKAGE_JW_LIBRARY);
        return true;
      case R.id.program_item_wol:
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINK_WATCHTOWER_ONLINE_LIBARY));
        startActivity(browserIntent);
        return true;
      case R.id.program_item_jw_language:
        openApp(getContext(), Constants.PACKAGE_JW_LANGUAGE);
        return true;
    }
    return false;
  }

  /** Open another app.
   * @param context current Context, like Activity, App, or Service
   * @param packageName the full package name of the app to open
   * @return true if likely successful, false if unsuccessful
   */
  private boolean openApp(Context context, String packageName) {
    PackageManager manager = context.getPackageManager();
    try {
      Intent i = manager.getLaunchIntentForPackage(packageName);
      if (i == null) {
        throw new PackageManager.NameNotFoundException();
      }
      i.addCategory(Intent.CATEGORY_LAUNCHER);
      context.startActivity(i);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  private void confirmDelegateHasInterface() {
    if (getDelegate() instanceof IProgramDelegate) {
      mDelegate = ((IProgramDelegate) getDelegate());
    } else {
      throw new IllegalStateException("delegate " +
          "should implement " + IProgramDelegate.class.getSimpleName() +
          " to allow necessary method calls.");
    }
  }

  private void giveTalksToDelegate() {
    cache = bindLifecycle(getLocalTalksObservable(), DESTROY).cache();
    cache.subscribe(notesObserver);
  }

  private final Observer<List<ITalk>> notesObserver = new Observer<List<ITalk>>() {

    private Progress mostRecentProgress;

    @Override
    public void onCompleted() {
//      if (mostRecentProgress != null) {
//        if (mostRecentProgress.failed) {
//          // failed to load talks locally... that's weird
//
//          return;
//        }
//      }


    }

    @Override
    public void onError(Throwable e) {
      e.printStackTrace();
    }

    @Override
    public void onNext(List<ITalk> progress) {
//      mostRecentProgress = progress;
      mTalks.clear();
      mTalks.addAll(progress);
      mDelegate.loadProgramTalks(progress);
    }
  };

  private final List<ITalk> mTalks = new ArrayList<>();
  private Observable<List<ITalk>> cache;

  private Observable<List<ITalk>> getLocalTalksObservable() {
    return dataManager.getProgram(mProgramId)
        .flatMap(new Func1<IProgram, Observable<List<ITalk>>>() {
          @Override
          public Observable<List<ITalk>> call(IProgram iProgram) {
            return dataManager.getTalks(iProgram);
          }
        })
        .subscribeOn(Schedulers.io());
//    return Observable.create(new Observable.OnSubscribe<Progress>() {
//      @Override
//      public void call(Subscriber<? super Progress> subscriber) {
//        try {
//          subscriber.onNext(new Progress(0, 1, false));
////          final IProgram program = ParseHelper.Queries.Local.getProgram(mProgramId);
////          final List<ITalk> talks = ParseHelper.Queries.Local.findAllProgramTalks((Program) program);
//          mTalks.clear();
//          mTalks.addAll(talks);
//
//          // LOCATION OF COMMON NON-RECURRING ACTIONS using _MiscUtils
//          //
//          // *********************************************************
//
//          subscriber.onNext(new Progress(1, 1, false));
//          subscriber.onCompleted();
//        } catch (ParseException e) {
//          e.printStackTrace();
//          subscriber.onNext(new Progress(0, 1, true));
//          subscriber.onCompleted();
//        }
//      }
//    }).subscribeOn(Schedulers.io());
  }

  private void setNavigation() {
    mToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
    mToolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickBackButton();
          }
        }
    );
  }


  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Callbacks) {
      mCallbacks = (Callbacks) activity;
    } else {
      throw new IllegalStateException("Activity must implement " + Callbacks.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    mDelegate = null;
    super.onDestroyView();
  }

  private void clickBackButton() {
    if (mCallbacks != null) {
      mCallbacks.toggleProgramPane();
    }
  }

  @Override
  public void onTalkClicked(ITalk talk) {
    if (mCallbacks != null) {
      mCallbacks.changeNotesFragmentTo(talk);
    }
  }



  public interface Callbacks {

    void toggleProgramPane();
    void changeNotesFragmentTo(ITalk talk);
  }
}

