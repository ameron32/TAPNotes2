package com.ameron32.apps.tapnotes.v2;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.ameron32.apps.tapnotes.v2.ui.ProgramSelectionActivity;

import org.junit.Before;

public class MyEspressoTest
    extends ActivityInstrumentationTestCase2<ProgramSelectionActivity> {

  private ProgramSelectionActivity mActivity;

  public MyEspressoTest() {
    super(ProgramSelectionActivity.class);
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    mActivity = getActivity();
  }

  public void testChangeText_sameActivity() {
    // Type text and then press the button.
//    onView(withId(R.id.editTextUserInput))
//        .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
//    onView(withId(R.id.changeTextButton)).perform(click());
//
//    // Check that the text was changed.
//    onView(withId(R.id.textToBeChanged))
//        .check(matches(withText(STRING_TO_BE_TYPED)));
  }
}
