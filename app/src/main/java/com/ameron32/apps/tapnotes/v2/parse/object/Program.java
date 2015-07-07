package com.ameron32.apps.tapnotes.v2.parse.object;

import com.ameron32.apps.tapnotes.v2.model.IProgram;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ColumnableParseObject;
import com.parse.ParseClassName;

import static com.ameron32.apps.tapnotes.v2.parse.Constants.*;

/**
 * Created by klemeilleur on 6/29/2015.
 */
@ParseClassName(PROGRAM_OBJECT_NAME)
public class Program
    extends ColumnableParseObject
    implements IProgram {

  public static Program create() {
    final Program p = new Program();
    return p;
  }

  public Program() {
    // required empty
  }

  @Override
  public ITalk[] getTalks() {
    return null;
  }

  public String getName() {
    return this.getString(PROGRAM_NAME_STRING_KEY);
  }
}
