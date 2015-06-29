package com.ameron32.apps.tapnotes.v2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.IDualLayout;
import com.ameron32.apps.tapnotes.v2.scripture.ScriptureTestingActivity;

import butterknife.InjectView;


public class MNIActivity extends TAPActivity
    implements ProgramFragment.Callbacks, NotesFragment.TestCallbacks {

  @InjectView(R.id.drawer_layout)
  DrawerLayout mDrawerLayout;
  @InjectView(R.id.nav_view)
  NavigationView mNavigationView;

  IDualLayout mDualLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    findViews();

    setupDrawer();
    addNotesFragment();
    addProgramFragment();
    addEditorFragment();
  }

  private void findViews() {
    IDualLayout displacing = (IDualLayout) findViewById(R.id.pane_displacing_layout);
    IDualLayout overlapping = (IDualLayout) findViewById(R.id.pane_overlapping_layout);
    mDualLayout = (displacing != null) ? displacing : overlapping;
    if (mDualLayout == null) {
      throw new IllegalStateException("mDualLayout cannot be null.");
    }
  }

  @Override
  protected @LayoutRes int getLayoutResource() {
    return R.layout.activity_mni;
  }

  private void setupDrawer() {
    setupDrawerContent(mNavigationView);
  }

  private void addNotesFragment() {
    // TODO consider replacing the default NOTES with explanatory fragment
    final String tag = "notes";
    final String toolbarTitle = getString(R.string.generic_toolbar_title);
    final String text1 = "";
    final String imageUrl = "";
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.notes_container, NotesFragment.create(toolbarTitle, text1, imageUrl), tag)
        .commit();
  }

  private void addProgramFragment() {
    final String tag = "program";
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.program_container, ProgramFragment.create(), tag)
        .commit();
  }

  private void addEditorFragment() {
    final String tag = "editor";
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.editor_container, EditorFragment.create(), tag)
        .commit();
  }

  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            return true;
          }
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_tap, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case android.R.id.home:
        toggleProgramPane();
        return true;
      case R.id.action_settings:

        return true;
      case R.id.action_scripture_activity:
        startActivity(new Intent(getActivity(), ScriptureTestingActivity.class));
        return true;
      }

    return super.onOptionsItemSelected(item);
  }



  private void openNavigationDrawer() {
    mDrawerLayout.openDrawer(GravityCompat.START);
  }

  private void toggleAnimatingLayout() {
    mDualLayout.toggleLayout();
  }

  @Override
  public void toggleProgramPane() {
    toggleAnimatingLayout();
  }

  @Override
  public void itemClicked(int position) {
    final Fragment notes = NotesFragment.create("Item " + position, "Text1: " + position, "http://i.imgur.com/ofKKBCG.jpg");
    getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        .replace(R.id.notes_container, notes)
        .addToBackStack(Integer.toString(position))
        .commit();
  }
}