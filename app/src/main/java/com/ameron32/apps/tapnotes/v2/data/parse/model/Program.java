package com.ameron32.apps.tapnotes.v2.data.parse.model;

import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.parse.frmk.ColumnableParseObject;
import com.parse.ParseClassName;

import static com.ameron32.apps.tapnotes.v2.data.parse.Constants.*;

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

  @Override
  public String getId() {
    return this.getObjectId();
  }
}
