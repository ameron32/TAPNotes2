package com.ameron32.apps.tapnotes.v2.ui.renderer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ameron32.apps.tapnotes.v2.R;
import com.jmpergar.awesometext.AwesomeTextHandler;

/**
 * Created by Micah on 7/19/2015.
 */
public class ScriptureSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer
//    , AwesomeTextHandler.ViewSpanClickListener
{

    private final static int textSizeInDips = 14;

    private static final String SCRIPTURE_START_TAG = "<<!<";
    private static final String SCRIPTURE_END_TAG = ">!>>";

    @Override
    public View getView(String text, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TextView scripView = (TextView) inflater.inflate(R.layout.span_scripture_layout, null);

        text = text.replace(SCRIPTURE_START_TAG, "").replace(SCRIPTURE_END_TAG, "").replace("@", "");
        String info = text.substring(0, text.indexOf("<"));
        text = text.replace(info, "").replace("<", "");


        scripView.setText(text);
        scripView.setTextSize(dipsToPixels(context, textSizeInDips));

        return scripView;
    }

    public static int dipsToPixels(Context ctx, float dips) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dips * scale + 0.5f);
        return px;
    }
//
//    @Override
//    public void onClick(String text, Context context) {
//        // TODO KRIS handle editor scripture click according to UX
////        Toast.makeText(context, "Hello " + text, Toast.LENGTH_SHORT).show();
//    }
}
