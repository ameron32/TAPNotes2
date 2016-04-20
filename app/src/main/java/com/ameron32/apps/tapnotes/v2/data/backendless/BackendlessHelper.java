package com.ameron32.apps.tapnotes.v2.data.backendless;

import com.ameron32.apps.tapnotes.v2.data.backendless.model.BProgram;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by klemeilleur on 3/24/2016.
 */
public abstract class BackendlessHelper implements RemoteHelper {

    @Override
    public Observable<List<IProgram>> getPrograms() {
        return Observable.create(new Observable.OnSubscribe<List<IProgram>>() {
            @Override
            public void call(Subscriber<? super List<IProgram>> subscriber) {
                List<BProgram> bPrograms = Backendless.Data.of(BProgram.class)
                        .find().getPage(10,0).getCurrentPage();
                List<IProgram> iPrograms = new ArrayList<>();
                iPrograms.addAll(bPrograms);
                subscriber.onNext(iPrograms);
            }
        });
    }
}
