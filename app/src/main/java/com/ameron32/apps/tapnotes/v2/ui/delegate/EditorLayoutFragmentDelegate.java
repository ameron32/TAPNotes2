package com.ameron32.apps.tapnotes.v2.ui.delegate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.frmk.FragmentDelegate;
import com.ameron32.apps.tapnotes.v2.model.IBible;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.ISanitizer;
import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.WrappedScripture;
import com.ameron32.apps.tapnotes.v2.ui.renderer.MentionSpanRenderer;
import com.jmpergar.awesometext.AwesomeTextHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by klemeilleur on 7/6/2015.
 */
public class EditorLayoutFragmentDelegate extends FragmentDelegate
    implements IEditorDelegate
{

  public static EditorLayoutFragmentDelegate create(Fragment fragment) {
    final EditorLayoutFragmentDelegate delegate = new EditorLayoutFragmentDelegate();
    delegate.setFragment(fragment);
    return delegate;
  }

  private static final IEditorDelegate.IEditorDelegateCallbacks stubCallbacks
          = new IEditorDelegate.IEditorDelegateCallbacks() {

    @Override
    public void onSubmitClicked(String editorText,
                                INote.NoteType type,
                                @Nullable INote note) {
      // stub only
    }

    @Override
    public void setSanitizerCallbacks(ISanitizer.ISanitizerCallbacks callbacks) {
      // stub only
    }
  };

  private IEditorDelegate.IEditorDelegateCallbacks mCallbacks;

  protected EditorLayoutFragmentDelegate() {
  }

  @InjectView(R.id.spinner_note_type)
  Spinner spinner;
  @InjectView(R.id.button_submit_note)
  ImageView submitButton;
  @InjectView(R.id.edittext_note_editor)
  EditText noteText;

  private ScriptureWatcher watcher;
  private ISanitizer sanitizer;
  private IBible bible;


  private static final String SCRIPTURE_PATTERN = "(@)([0-9,A-Z])\\w+(\\s)(\\d)+([\\s,:])([\\d-,])+";

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    //during super.onViewCreated()--Sanitizer is instantiated
    super.onViewCreated(view, savedInstanceState);
    confirmHostFragmentHasNecessaryCallbacks();
    ButterKnife.inject(this, view);
    setupSpinner();
    setOnClicks();


/*    AwesomeTextHandler awesomeTextViewHandler = new AwesomeTextHandler();
    awesomeTextViewHandler
            .addViewSpanRenderer(HASHTAG_PATTERN, new HashtagsSpanRenderer())
            .addViewSpanRenderer(SCRIPTURE_PATTERN, new MentionSpanRenderer())
            .setView(noteText);*/
  }

  @Override
  public void onSanitizerCreated(ISanitizer sanitizer) {
    Log.i("Sanitizer", "Sanitizer created");
    this.sanitizer = sanitizer;
    mCallbacks.setSanitizerCallbacks(watcher); // call after Sanitizer initialization
    watcher = new ScriptureWatcher(sanitizer);
    watcher.setBible(bible);
    noteText.addTextChangedListener(watcher);
  }

  @Override
  public void onBibleCreated(IBible bible) {
      this.bible = bible;
    }


  @Override
  public void onDestroyView() {
    ButterKnife.reset(this);
    mCallbacks = stubCallbacks;
    super.onDestroyView();
  }

  private void confirmHostFragmentHasNecessaryCallbacks() {
    if (getFragment() instanceof IEditorDelegate.IEditorDelegateCallbacks) {
      mCallbacks = ((IEditorDelegate.IEditorDelegateCallbacks) getFragment());
    } else {
      throw new IllegalStateException("host fragment " +
              "should implement " + IEditorDelegate.IEditorDelegateCallbacks.class.getSimpleName() +
              "to support callback methods.");
    }
  }


  @Override
  public void updateEditorText(String newEditorText, @Nullable INote note) {
    // THIS METHOD SHOULD BE CALLED WHEN THE USER EDITS A NOTE
    // THE NOTE ID IS PROVIDED HERE.
    // RETURN THE NOTE ID AS A PARAMETER WITH THE CALLBACK METHOD
    // onSubmitClicked(text, type, noteId) TO NOTIFY APP TO MODIFY
    // EXISTING NOTE.
    // TODO: MICAH delegate method
    noteText.setText(newEditorText);
    noteText.setTag(note);


  }

  private void setOnClicks() {

    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (noteText.getTag() != null) {
          mCallbacks.onSubmitClicked(noteText.getText().toString(), getNoteType(), (INote) noteText.getTag());
        } else {
          mCallbacks.onSubmitClicked(noteText.getText().toString(), getNoteType(), null);
        }

        noteText.setText("");
        noteText.setTag(null);


      }
    });
  }

  private void setupSpinner() {
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
            R.array.spinner_options, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
    spinner.setAdapter(adapter);
  }

  private INote.NoteType getNoteType() {
    switch (spinner.getSelectedItemPosition()) {

      case 3:
        return INote.NoteType.BAPTISM_COUNT;
      case 2:
        return INote.NoteType.ATTENDANCE_COUNT;
      case 1:
        return INote.NoteType.SPEAKER;
      case 0:
      default:
        return INote.NoteType.STANDARD;

    }
  }


  public class ScriptureWatcher implements TextWatcher, ISanitizer.ISanitizerCallbacks  {

    String string = "";
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String nums = "0123456789";
    boolean replacing = false;
    char last;
    char ntl;
    char ntntl;

    boolean added = true;

    private IBible bible;

    private void setBible(IBible b){
      bible = b;
    }

    public ScriptureWatcher(ISanitizer s){
      sanitizer = s;
      sanitizer.setCallbacks(this);
    }
    ISanitizer sanitizer;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      if (count>after){
        added = false;
      }else
        added = true;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {

      if (added) {
        string = s.toString();
        if (string.contains("@")) {
          if (!replacing) {
            if (endofnumberstring(string)) {
              if (bible!=null)
              sanitizer.testForScriptures(this.bible, string);
            }
          }
        }
      }
    }


    private boolean endofnumberstring(String s) {
      int len = s.length();

      if (len < 3) return false;

      last = s.toUpperCase().charAt(len - 1);
      ntl = s.toUpperCase().charAt(len - 2);
      ntntl = s.toUpperCase().charAt((len - 3));

      //Ends with #.
      if ((last == '.') && (nums.indexOf(ntl) != -1)) return true;

      //Ends with # and two spaces
      if ((last == ' ') && (ntl == ' ') && (nums.indexOf(ntntl) != -1)) return true;

      //Ends with #,
      if ((last == ',') && (nums.indexOf(ntl) != -1)) return true;

      //Ends with # space letter
      if ((alpha.indexOf(last) != -1) && (ntl == ' ') && (nums.indexOf(ntntl) != -1)) return true;

      //Ends with # letter
      if ((alpha.indexOf(last) != -1) && (nums.indexOf(ntl) != -1)) return true;

      return false;

    }


    @Override
    public void onSanitizerResults(WrappedScripture scripture) {


      if (scripture!=null){
        replacing = true;
        //TODO: Replace old text with correct scripture name.
        String oldText = noteText.getText().toString();
        String subToReplace = scripture.replacedText.substring(0,scripture.replacedText.length()-2);
        String newText = scripture.newText;
        oldText = oldText.replace(subToReplace, newText);

        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 43, 0));

        Spannable textSpan = new SpannableString(oldText);
        textSpan.setSpan(fcs, 0, oldText.length()-2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        noteText.setText(textSpan);


        int position = noteText.length();
        Editable etext = noteText.getText();
        Selection.setSelection(etext, position);
        replacing=false;

      }
    }
  }

}
