package com.ameron32.apps.tapnotes.v2.ui.mc_adapter;

import android.view.View;
import android.widget.TextView;


import com.ameron32.apps.tapnotes.v2.R;
import com.levelupstudio.recyclerview.ExpandableRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Micah on 7/4/2015.
 */
public class DummyProgramItemHolder extends ExpandableRecyclerView.ExpandableViewHolder {

    @InjectView(R.id.program_item_textview)public TextView title_textView;

public DummyProgramItemHolder(View view, int type){

        super(view);
        ButterKnife.inject(this, view);

}


}
