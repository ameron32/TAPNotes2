package com.ameron32.apps.tapnotes.v2.ui.program_selection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.data.DataManager;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.frmk.TAPFragment;
import com.ameron32.apps.tapnotes.v2.ui.delegate.ProgramSelectionLayoutFragmentDelegate;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProgramSelection2Fragment extends TAPFragment {

  @InjectView(R.id.viewPager)
  ViewPager pager;

  @Inject
  DataManager dataManager;

  public ProgramSelection2Fragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_program_selection_2, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    pager.setPageTransformer(false, new ParallaxTransformer());
  }

  @Override
  public void onStart() {
    super.onStart();
//        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
//        if (tabLayout != null) {
//            tabLayout.setupWithViewPager(pager);
//        }
  }

  @Override
  public void onResume() {
    super.onResume();
    requestPrograms();
  }

  @Override
  public void onDestroyView() {
    pager.setAdapter(null);
    ButterKnife.reset(this);
    super.onDestroyView();
  }

  Observer<List<IProgram>> programObserver = new Observer<List<IProgram>>() {
    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable e) {}

    @Override
    public void onNext(List<IProgram> iPrograms) {
      pager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), pager, iPrograms));
      pager.setCurrentItem(pager.getAdapter().getCount() -1, true);
    }
  };

  void requestPrograms() {
    addToCompositeSubscription(
        dataManager.syncPrograms()
            .concatMap(new Func1<List<IProgram>, Observable<List<IProgram>>>() {
              @Override
              public Observable<List<IProgram>> call(List<IProgram> iPrograms) {
                return dataManager.getPrograms();
              }
            })
            .observeOn(Schedulers.computation())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(programObserver));
  }

  @Override
  protected FragmentDelegate createDelegate() {
    return ProgramSelectionLayoutFragmentDelegate.create(ProgramSelection2Fragment.this);
  }

//  public static class Item {
//    private final String imageUrl;
//    private final String name;
//
//    public Item(String name, String imageUrl) {
//      this.imageUrl = imageUrl;
//      this.name = name;
//    }
//
//    public String getImageUrl() {
//      return imageUrl;
//    }
//
//    public String getName() {
//      return name;
//    }
//  }
//
//  private List<Item> getItems() {
//    List<Item> items = new ArrayList<>();
//    items.add(new Item("2015", "https://i.imgur.com/Mgx193t.png"));
//    items.add(new Item("2016", "https://i.imgur.com/N8H7jHr.png"));
//    return items;
//  }

  public static class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ViewPager pager;
    private final List<IProgram> items;

    public ViewPagerAdapter(FragmentManager fm, ViewPager pager, List<IProgram> items) {
      super(fm);
      this.pager = pager;
      this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
      ProgramViewPagerFragment fragment = ProgramViewPagerFragment.newInstance(items.get(position));
      fragment.notifyPosition(position);
      fragment.listenToPager(pager);
      return fragment;
    }

    @Override
    public int getCount() {
      return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return items.get(position).getName();
    }
  }
}
