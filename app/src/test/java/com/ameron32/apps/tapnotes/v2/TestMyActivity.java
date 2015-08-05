package com.ameron32.apps.tapnotes.v2;

import com.ameron32.apps.tapnotes.v2.scripture.ScriptureTestingActivity;
import com.ameron32.apps.tapnotes.v2.ui.ProgramSelectionActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

//import static org.fest.assertions.api.ANDROID.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestMyActivity {

  private ScriptureTestingActivity mActivity;

  @Before
  public void setup() {
    mActivity = Robolectric.buildActivity(ScriptureTestingActivity.class).create().get();
  }

  @Test
  public void myActivityAppearsAsExpectedInitially() {
//    assertThat(mActivity.mClickMeButton).hasText("Click me!");
//    assertThat(mActivity.mHelloWorldTextView).hasText("Hello world!");
  }

  @Test
  public void clickingClickMeButtonChangesHelloWorldText() {
//    assertThat(mActivity.mHelloWorldTextView).hasText("Hello world!");
//    mActivity.mClickMeButton.performClick();
//    assertThat(mActivity.mHelloWorldTextView).hasText("HEY WORLD");
  }
}
