package com.ameron32.apps.tapnotes.v2.ui.mc_adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.ITalk;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.ui.delegate.IProgramDelegate;
import com.levelupstudio.recyclerview.ExpandableRecyclerView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Micah on 7/7/2015.
 */
public class ProgramAdapter extends ExpandableRecyclerView.ExpandableAdapter<ProgramAdapter.ViewHolder, Integer> implements IProgramDelegate{


    private static final int VIEW_TYPE_GROUP_WITH_BAPTISM = 100;
    private static final int VIEW_TYPE_GROUP_WITHOUT_BAPTISM = 101;
    private static final int VIEW_TYPE_CHILD_MUSIC = 0;
    private static final int VIEW_TYPE_CHILD_SONG = 1;
    private static final int VIEW_TYPE_CHILD_TALK = 2;
    private static final int VIEW_TYPE_CHILD_SYMPOSIUMTALK = 3;

    //NOTE: When adding a new theme, add a field here for the name, and appropriate branches to the switch statements
    // in the setChildExpired method.
    private static final String THEME_TEAL2015 = "Teal2015Theme";
    private static final String THEME_ULTRABLACK = "UltraBlackTheme";
    private static final String THEME_MATERIALDARK = "MaterialDarkTheme";


    private ArrayList<DateTime> headers;
    private ArrayList<ArrayList<ITalk>> content;

    private ArrayList<IProgramDelegateCallbacks> mCallBackListeners;

    Context mContext;

    public ProgramAdapter(Context c){
        mContext = c;
        headers = new ArrayList<>();
        content = new ArrayList<>();
        mCallBackListeners = new ArrayList<>();
    }

    public void setCallBackListener(IProgramDelegateCallbacks listener){
        //TODO - Call this from the ProgramLayoutDelegate so Kris can pass his listeners through.
        mCallBackListeners.add(listener);
    }

    public void talkClicked(String talk_ID){
        for (IProgramDelegateCallbacks listener:mCallBackListeners){
            listener.onTalkClicked(talk_ID);
        }
    }

    @Override
    public void loadProgramTalks(List<ITalk> talks) {

        sortProgramData(talks);

    }

    private void sortProgramData(List<ITalk> talks){

        ArrayList<DateTime> dates = new ArrayList<>();
        ArrayList<ArrayList<ITalk>> sortedTalks = new ArrayList<>();

        for (ITalk t:talks){
            DateTime date = t.getDateAndTime(mContext.getResources().getConfiguration().locale);
            boolean addDate = true;
            for (DateTime d:dates){
                if (d.dayOfYear().get()== date.dayOfYear().get()){
                    addDate = false;
                }
            }
            if (addDate){
                dates.add(date);
            }
        }

        for (DateTime date:dates){
            ArrayList<ITalk>talksForThisDate = new ArrayList<>();
            for (ITalk t:talks){
                if (t.getDateAndTime(mContext.getResources().getConfiguration().locale).dayOfYear().get() == date.dayOfYear().get()){
                    talksForThisDate.add(t);
                }
            }

            Collections.sort(talksForThisDate, new ITalkComparator());
            sortedTalks.add(talksForThisDate);
        }



        content = sortedTalks;
        headers = dates;

    }

    private String getWeekday(DateTime d){
        StringBuilder title = new StringBuilder();
        title.append(getDayOfWeek(d.dayOfWeek().get()));
        return title.toString();
    }
    private String getDate(DateTime d){

        StringBuilder title = new StringBuilder();
        String date = d.dayOfMonth().getAsString();
        String month = getMonth(d.monthOfYear().get());
        String year = String.valueOf(d.getYear());

        switch(mContext.getResources().getConfiguration().locale.getLanguage()){
            case "es":{

                title.append(date).append(" de ").append(month).append(" ").append(year);
                return title.toString();
            }
            default:{
                title.append(month).append(" ").append(date).append(", ").append(year);
                return title.toString();
            }
        }

    }
    private String getDayOfWeek(int i){
        switch (i){
            case 1:
                return mContext.getResources().getString(R.string.monday);
            case 2:
                return mContext.getResources().getString(R.string.tuesday);
            case 3:
                return mContext.getResources().getString(R.string.wednesday);
            case 4:
                return mContext.getResources().getString(R.string.thursday);
            case 5:
                return mContext.getResources().getString(R.string.friday);
            case 6:
                return mContext.getResources().getString(R.string.saturday);
            case 7:
            default:
                return mContext.getResources().getString(R.string.sunday);
        }
    }
    private String getMonth(int i){
        switch (i){
            case 1:
                return mContext.getResources().getString(R.string.january);
            case 2:
                return mContext.getResources().getString(R.string.february);
            case 3:
                return mContext.getResources().getString(R.string.march);
            case 4:
                return mContext.getResources().getString(R.string.april);
            case 5:
                return mContext.getResources().getString(R.string.may);
            case 6:
                return mContext.getResources().getString(R.string.june);
            case 7:
                return mContext.getResources().getString(R.string.july);
            case 8:
                return mContext.getResources().getString(R.string.august);
            case 9:
                return mContext.getResources().getString(R.string.september);
            case 10:
                return mContext.getResources().getString(R.string.october);
            case 11:
                return mContext.getResources().getString(R.string.november);
            case 12:
            default:
                return mContext.getResources().getString(R.string.december);
        }
    }



    public abstract static class ViewHolder extends ExpandableRecyclerView.ExpandableViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class GroupViewHolderWithoutBaptism extends ViewHolder {

        @InjectView(R.id.weekdayTextView) TextView weekdayView;
        @InjectView(R.id.dateTextView) TextView dateTextView;
        public GroupViewHolderWithoutBaptism(@NonNull View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }

        @Override
        protected boolean canExpand() {
            return true;
        }
    }

    public static class GroupViewHolderWithBaptism extends ViewHolder {

        @InjectView(R.id.weekdayTextView) TextView weekdayView;
        @InjectView(R.id.dateTextView) TextView dateTextView;

        public GroupViewHolderWithBaptism(@NonNull View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @Override
        protected boolean canExpand() {
            return true;
        }
    }

    public static class ChildViewHolderWithSymposium extends ChildViewHolder{
        @InjectView(R.id.symposiumTextView) TextView symposiumTextView;

        public ChildViewHolderWithSymposium(@NonNull View itemView, ProgramAdapter adapter) {
            super(itemView, adapter);
            ButterKnife.inject(this, itemView);
        }

    }

    public static class ChildViewHolder extends ViewHolder {

        @InjectView (R.id.program_item_textview) TextView textView;
        public String item_ID;
        ProgramAdapter mAdapter;

        public boolean expired = false;

        public ChildViewHolder(@NonNull View itemView, ProgramAdapter adapter) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            mAdapter = adapter;

        }

        @Override
        protected boolean canExpand() {
            return false;
        }


        @Override
        protected boolean onViewClicked(final View myView) {

            ChildViewHolder h = null;
            if (myView.getTag() instanceof ChildViewHolder)
            h = (ChildViewHolder) myView.getTag();
            if (h!=null){
                mAdapter.talkClicked(h.item_ID);
            }

            myView.setSelected(true);

            return false;
        }

    }


    @NonNull
    @Override
    protected ProgramAdapter.ViewHolder onCreateExpandableViewHolder(ViewGroup viewGroup, int i) {
       final View v;
        switch(i) {
            case VIEW_TYPE_CHILD_MUSIC:
            case VIEW_TYPE_CHILD_SONG:
            case VIEW_TYPE_CHILD_TALK:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insert_program_layout_list_item, viewGroup, false);
                ChildViewHolder child = new ChildViewHolder(v, this);
                v.setTag(child);
                return child;
            case VIEW_TYPE_CHILD_SYMPOSIUMTALK:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insert_program_layout_symposium_item, viewGroup, false);
                ChildViewHolderWithSymposium childSymp = new ChildViewHolderWithSymposium(v, this);
                v.setTag(childSymp);
                return childSymp;
            case VIEW_TYPE_GROUP_WITH_BAPTISM:
                v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insert_program_layout_list_header1, viewGroup, false);
                return new GroupViewHolderWithBaptism(v);
            case VIEW_TYPE_GROUP_WITHOUT_BAPTISM:
            default:
                v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insert_program_layout_list_header2, viewGroup, false);
                return new GroupViewHolderWithoutBaptism(v);
        }
    }

    @Override
    protected void onBindGroupView(ProgramAdapter.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof  GroupViewHolderWithBaptism){
            GroupViewHolderWithBaptism holder = (GroupViewHolderWithBaptism)viewHolder;
            holder.weekdayView.setText(getWeekday(headers.get(i)));
            holder.dateTextView.setText(getDate(headers.get(i)));

        }else if (viewHolder instanceof GroupViewHolderWithoutBaptism){
            GroupViewHolderWithoutBaptism holder = (GroupViewHolderWithoutBaptism)viewHolder;
            holder.weekdayView.setText(getWeekday(headers.get(i)));
            holder.dateTextView.setText(getDate(headers.get(i)));

        }else{
            return;
        }


    }

    @Override
    protected void onBindChildView(ProgramAdapter.ViewHolder viewHolder, int i, int i2) {
        if (!(viewHolder instanceof ChildViewHolder)) {
            // fail: ensure onCreate is generating the correct ChildViewHolder 
            return;
        }


        ChildViewHolder holder;

        switch (content.get(i).get(i2).getEventType()){

            case SYMPOSIUMTALK:
                ChildViewHolderWithSymposium cvhws = (ChildViewHolderWithSymposium) viewHolder;
                cvhws.symposiumTextView.setText(content.get(i).get(i2).getSymposiumTitle());
                cvhws.textView.setText(content.get(i).get(i2).getTalkTitle());
                holder = cvhws;
            break;

            case MUSIC:
            case SONG:
            case TALK:
            default:
                ChildViewHolder cvh = (ChildViewHolder) viewHolder;
                cvh.textView.setText(content.get(i).get(i2).getTalkTitle());
                holder = cvh;
            break;
        }



        String themeName = "";
        try {
            themeName = getTheme(mContext);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (holder.expired){
            setChildExpired(holder, themeName);
        }

    }

    private void setChildExpired(ChildViewHolder cvh, String themeName){

        switch (themeName){
            case THEME_TEAL2015:
                cvh.textView.setBackgroundResource(R.color.teal_listItem_dark);
                return;
            case THEME_MATERIALDARK:
                cvh.textView.setBackgroundResource(R.color.materialdark_listItem_dark);
                return;
            case THEME_ULTRABLACK:
                cvh.textView.setBackgroundResource(R.color.ultrablack_listItem_dark);
            default:
                return;
        }

    }

    private String getTheme(Context context) throws PackageManager.NameNotFoundException {
        return context.getResources().getResourceEntryName(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).applicationInfo.theme);
    }

    @Override
    protected int getGroupCount() {
        return headers.size();
    }

    @Override
    protected int getChildrenCount(int i) {
        return content.get(i).size();
    }

    @Override
    protected int getGroupViewType(int i) {
        if (i==1)
        return VIEW_TYPE_GROUP_WITH_BAPTISM;
    else
        return VIEW_TYPE_GROUP_WITHOUT_BAPTISM;
    }

    @Override
    protected int getChildViewType(int i, int i2) {

        switch(content.get(i).get(i2).getEventType()){
            case MUSIC:
                return VIEW_TYPE_CHILD_MUSIC;
            case SONG:
                return VIEW_TYPE_CHILD_SONG;
            case SYMPOSIUMTALK:
                return VIEW_TYPE_CHILD_SYMPOSIUMTALK;
            case TALK:
            default:
                return VIEW_TYPE_CHILD_TALK;
        }
    }

    @Override
    public Integer getGroup(int i) {
        return i;
    }


    public class ITalkComparator implements Comparator<ITalk> {

        @Override
        public int compare(ITalk lhs, ITalk rhs) {

            return lhs.getDateAndTime(mContext.getResources().getConfiguration().locale).compareTo(rhs.getDateAndTime(mContext.getResources().getConfiguration().locale));
        }
    }
}
