package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.content.Context;

import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.ScriptureFinder;

import java.util.ArrayList;

/**
 * Created by Micah on 7/24/2015.
 */
public class ScriptureAppender {

    Bible bible;
    ScriptureFinder finder;
    String[] appendedScripts;
    private static final String SCRIPTURE_START_TAG = "<<!<";
    private static final String SCRIPTURE_END_TAG = ">!>>";
    int bookNumber;
    int chapter;
    int[] verses;
    String[] validNames;
    private static final String digits = "0123456789";

    public ScriptureAppender(Bible b, String[] validNames) {
        bible = b;
        this.validNames = validNames;
        finder = new ScriptureFinder();

    }

    public INote appendScriptures (INote note) {
        String text = note.getNoteText();

        //First, get the location of all scriptures in the note.
        ArrayList<Integer>scriptureTagIndices = new ArrayList<>();
        int index = text.indexOf(SCRIPTURE_START_TAG);
        while (index >= 0) {
            scriptureTagIndices.add(index);
            index = text.indexOf(SCRIPTURE_START_TAG, index + 1);
        }

        appendedScripts = new String[scriptureTagIndices.size()];

        //Now, for each tag, we parse it, lookup the verse(s), and append that info to the note.
        //Be sure to NEVER edit the actual tags in the text so that it renders properly.

        int start;
        int stop;
        int book=0;
        int chapter=0;
        int[] verses;
        for (int i=0; i<scriptureTagIndices.size(); i++) {

            start = scriptureTagIndices.get(i) + 4; //Because the opening part of the tag is 4 chars long.
            stop = text.substring(start).indexOf("<");
            String info = text.substring(start, stop);
            String name = text.substring(stop + 1, text.indexOf(">"));
            String[] vals = info.split(" ");
            verses = new int[vals.length - 2];
            try {
                for (int j = 0; j < vals.length; j++) {
                    if (j == 0) {
                        book = Integer.valueOf(vals[j]);
                    } else {
                        if (j == 1) {
                            chapter = Integer.valueOf(vals[j]);
                        } else {
                            verses[j - 2] = Integer.valueOf(vals[j]);
                        }
                    }

                }
            } catch (NumberFormatException e) {
                //Do nothing
            }

            //At this point, should have book, chapter and verses.
            try {
                String[] verseTexts = finder.getVerses(bible, book, chapter, verses);
                if (verseTexts.length > 0) {
                    text = text + "\n\n" + name + "\n";
                    for (int k = 0; k < verseTexts.length; k++) {
                        if (k == 0) {
                            text = text + verseTexts[k];
                        } else {
                            if (areTextsContiguous(verseTexts[k - 1], verseTexts[k])) {
                                text = text + verseTexts[k];
                            } else {
                                text = text + "\n" + verseTexts[k];
                            }
                        }
                    }
                }
            } catch (ScriptureFinder.ScriptureNotFoundException e) {
                //Do nothing.
            }


        }
        return note;
    }

    private boolean areTextsContiguous(String first, String next){
        int firstInt = findFirstInt(first);
        int nextInt = findFirstInt((next));

        return (nextInt == firstInt+1);
    }

    private int findFirstInt(String text){
        for (int i=0; i<text.length(); i++){
            if (digits.contains(text.substring(i, i+1))){
                for (int j=0; j<text.length(); j++){
                    if (!digits.contains(text.substring(j, j+1))){
                        try{
                            return Integer.valueOf(text.substring(i, j));
                        }catch (NumberFormatException e){
                            //Do nothing.
                        }
                    }
                }
            }
        }
        return -1;
    }


}