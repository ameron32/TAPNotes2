package com.ameron32.apps.tapnotes.v2.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.di.Injector;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 6/12/2015.
 */
public abstract class TAPFragment extends Fragment {

  public TAPFragment() {}

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    Injector.INSTANCE.inject(this);
    super.onCreate(savedInstanceState);
  }
}
