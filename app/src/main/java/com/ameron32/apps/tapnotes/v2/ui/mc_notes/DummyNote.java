package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

/**
 * Created by Micah on 7/6/2015.
 */
public class DummyNote extends AbstractDataProvider.Data {

    String noteText;
    @Override
    public long getId() {
        return 0;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getSwipeReactionType() {
        return 0;
    }

    public void setText(String text){
        noteText=text;
    }

    @Override
    public String getText() {
        return noteText;
    }

    @Override
    public void setPinnedToSwipeLeft(boolean pinned) {

    }

    @Override
    public boolean isPinnedToSwipeLeft() {
        return false;
    }
}
