package com.ameron32.apps.tapnotes.v2.data.parse;

import com.ameron32.apps.tapnotes.v2.data.DataAccess;
import com.ameron32.apps.tapnotes.v2.data.SyncEvent;
import com.ameron32.apps.tapnotes.v2.data.model.IObject;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by klemeilleur on 4/12/2016.
 */
public class ParseSyncEvent implements SyncEvent {

  private DataAccess dataAccess;

  @Override
  public void onCreate(DataAccess dataManager) {
    this.dataAccess = dataManager;
  }

  @Override
  public Observable<List<IObject>> performAction() {
    return dataAccess.syncPrograms().map(new Func1<List<IProgram>, List<IObject>>() {
      @Override
      public List<IObject> call(List<IProgram> iPrograms) {
        return ParseUtil.toIObject(iPrograms);
      }
    });
  }

  @Override
  public void onStopService(DataAccess dataAccess) {
    dataAccess = null;
  }
}
