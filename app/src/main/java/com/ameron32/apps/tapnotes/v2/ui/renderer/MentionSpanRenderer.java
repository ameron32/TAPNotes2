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
public class MentionSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer, AwesomeTextHandler.ViewSpanClickListener {

    @Override
    public View getView(String text, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TextView mentionView = (TextView) inflater.inflate(R.layout.span_scripture_layout, null);
        mentionView.setText(text);
        return mentionView;
    }

    @Override
    public void onClick(String text, Context context) {
        Toast.makeText(context, "Hello " + text, Toast.LENGTH_SHORT).show();
    }
}