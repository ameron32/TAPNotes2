package com.ameron32.apps.tapnotes.v2.data;

import com.ameron32.apps.tapnotes.v2.data.model.IObject;

import java.util.List;

import rx.Observable;
import rx.Observer;

/**
 * Created by klemeilleur on 4/12/2016.
 */
public interface SyncEvent {

    void onCreate(DataAccess dataAccess);
    Observable<DataAccess.Progress> performAction();
    void onStopService(DataAccess dataAccess);
}
