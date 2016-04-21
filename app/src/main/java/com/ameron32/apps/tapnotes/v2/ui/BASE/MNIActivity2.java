package com.ameron32.apps.tapnotes.v2.ui.BASE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.data.DataManager;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.di.controller.ApplicationThemeController;
import com.ameron32.apps.tapnotes.v2.di.module.DefaultAndroidActivityModule;
import com.ameron32.apps.tapnotes.v2.di.module.MNIActivityModule;
import com.ameron32.apps.tapnotes.v2.frmk.TAPActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by klemeilleur on 4/21/2016.
 */
public class MNIActivity2 extends TAPActivity implements MainMvpView {

  private static final String TAG = MNIActivity2.class.getSimpleName();

  private static final String EXTRA_KEY_PROGRAM_ID = "EXTRA_KEY_PROGRAM_ID";

  /**
   * @param context
   *        required context for Intent
   * @param programId
   *        the objectId of the ParseObject for the Program to load
   * @return the intent ready to startActivity
   */
  public static Intent makeIntent(final Context context,
                                  final String programId) {
    final Intent i = new Intent(context, MNIActivity2.class);
    i.putExtra(EXTRA_KEY_PROGRAM_ID, programId);
    return i;
  }

  @Inject
  DataManager dataManager;

  MainPresenter mainPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    mainPresenter = new MainPresenter(dataManager);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mainPresenter.attachView(this);
  }

  @Override
  protected @LayoutRes
  int getLayoutResource() {
    // rather than setContentView(), provide inflatable layout here
    return R.layout.activity_mni;
  }

  @Inject
  ApplicationThemeController themeController;

  @Override
  protected int provideThemeResource() {
    return themeController.getTheme();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mainPresenter.detachView();
  }

  @Override
  public void showTalks(List<ITalk> talks) {

  }

  @Override
  public void showTalksEmpty() {

  }

  @Override
  public void showTalksError() {

  }

  @Override
  protected Object[] getModules() {
    return new Object[] {
        new MNIActivityModule(this),
        new DefaultAndroidActivityModule(this)
    };
  }
}
