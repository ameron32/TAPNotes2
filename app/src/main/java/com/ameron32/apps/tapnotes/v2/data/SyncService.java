package com.ameron32.apps.tapnotes.v2.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.data.model.IObject;
import com.ameron32.apps.tapnotes.v2.frmk.TAPService;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class SyncService extends TAPService {

    private static final String TAG = SyncService.class.getSimpleName();

    @Inject
    DataManager mDataManager;
    private Subscription mSubscription;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, SyncService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i(TAG, "Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Log.i(TAG, "Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.performSync()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<IObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "Synced successfully!");
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w(TAG, "Error syncing.");
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(List<IObject> ribot) {

                    }
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Log.i(TAG, "Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }
}
