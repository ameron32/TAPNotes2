package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.content.Context;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
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

    public ScriptureAppender(Bible b, Context c) {
        bible = b;
        this.validNames = c.getResources().getStringArray(R.array.bible_books);
        finder = new ScriptureFinder();

    }

    public String appendScriptures (INote note) {
        String text = note.getNoteText();
        StringBuilder appendBuilder = new StringBuilder();

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
            String info = text.substring(start, start+stop);
            String name = text.substring(start+stop + 1, start+stop+1+text.substring(start+stop+1).indexOf(">"));
            String[] vals = info.split(" ");
            verses = new int[vals.length-2];
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
                    String startTag = SCRIPTURE_START_TAG + String.valueOf(book) + " " + String.valueOf(chapter) + " ";
                    for (int y=0; y<verses.length; y++){
                        startTag = startTag + String.valueOf(verses[y])+" ";
                    }
                    startTag = startTag + "<";

                    if (i != 0) {
                        appendBuilder.append("<br><br>");
                    }
                    appendBuilder.append(name).append("<br>");
                    for (int k = 0; k < verseTexts.length; k++) {
                        if (k == 0) {
                            appendBuilder.append(verseTexts[k]);
                        } else {
                            if (areTextsContiguous(verseTexts[k - 1], verseTexts[k])) {
                                appendBuilder.append(verseTexts[k]);
                            } else {
                                appendBuilder.append("<br>").append(verseTexts[k]);
                            }
                        }
                    }
                }
            } catch (ScriptureFinder.ScriptureNotFoundException e) {
                //Do nothing.
            }


        }
        return appendBuilder.toString();
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
                            return Integer.valueOf(text.substring(i, i+j+1));
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