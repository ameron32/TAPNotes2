package com.ameron32.apps.tapnotes.v2.ui.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.levelupstudio.recyclerview.ExpandableRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Micah on 7/4/2015.
 */
public class DummyProgramGroupHolder extends ExpandableRecyclerView.ExpandableViewHolder {

    @InjectView(R.id.dateTextView)          public TextView dateTextView;
    @InjectView(R.id.attendanceEditText)    public EditText attendanceTextView;
    @InjectView(R.id.baptismEditText)       public EditText baptismTextView;

    public DummyProgramGroupHolder(View v, int type){
        super(v);

        ButterKnife.inject(this, v);

        /*Not sure if BK will work when baptismTextView is null... so leaving this here for now - Micah
        if (type ==0){
            dateTextView = (TextView)v.findViewById(R.id.dateTextView);
            attendanceTextView = (EditText)v.findViewById(R.id.attendanceEditText);
        }else{
            dateTextView = (TextView)v.findViewById(R.id.dateTextView);
            attendanceTextView = (EditText)v.findViewById(R.id.attendanceEditText);
            baptismTextView = (EditText)v.findViewById(R.id.baptismEditText);
        }
        */
    }

}
