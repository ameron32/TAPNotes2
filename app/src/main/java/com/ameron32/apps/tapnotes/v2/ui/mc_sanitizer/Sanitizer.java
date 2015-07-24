package com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.IBible;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.scripture.Scripture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Micah on 7/16/2015.
 */
public class Sanitizer implements ISanitizer{

    private String[] validNames;
    private int[] chapterAmts;

    private String book;
    private int bookNumber;
    private int chapter;
    private int[] verses;
    private String verseString;
    private static final String digits = "0123456789";

    private ISanitizerCallbacks mCallback;

    private String namePart;
    private String chapterPart;
    private String versePart;

    private IBible bible;

    public Sanitizer(Context context) {
        // TODO: MICAH CONSIDER MERGING bible_books & book_names
        // SEE strings_biblebooks.xml & strings_bible_book_reference.xml
        validNames = context.getResources().getStringArray(R.array.bible_books);
        chapterAmts = context.getResources().getIntArray(R.array.chapter_quantities);
    }

    @Override
    public void testForScriptures(IBible b, String s) {
        verseString = "";
        this.bible = b;
        //TODO: Not sure about this subs.length check... needed? - MC
        String[] subs = s.split("@");
        if (subs.length ==1) mCallback.onSanitizerResults(null);
       else{
            mCallback.onSanitizerResults(reallyCheck(s, subs));
        }
    }
    @Override
    public void setCallbacks(ISanitizerCallbacks callback) {
        mCallback = callback;
    }

    @Nullable
    private WrappedScripture reallyCheck(String s, String[] subs){

        int chStart = -1;
        int chEnd = -1;

        //We are only interested in the last one.
        String sub = subs[subs.length-1];
        sub = sub.trim();
        chStart = getChapterStartIndex(sub);
        if (chStart==-1)return null;
        else
        chEnd = getChapterEndIndex(sub, chStart);
        if (chEnd == -1) return null;

        namePart = sub.substring(0, chStart-1);
        chapterPart = sub.substring(chStart, chEnd);
        versePart = sub.substring(chEnd, sub.length()-1);

        book = getBookName();
        if (book == null){
            return null;
        }else{
            chapter = getChapter();
            if (chapter == -1){
                return null;
            }else{
                verses = getVerses();
                if (verses.length>0){
                    //We have actually established a valid book, valid chapter, and at least 1 valid verse.  Yay.
                    return getWrappedScrip(s, sub);
                }
            }
        }
        return null;
    }



    @Nullable
    private String getBookName(){

        String name = namePart.toUpperCase();
        for (int i=0; i<validNames.length; i++){
            if (validNames[i].startsWith(name)){
                if (uniqueNameStart(name)){
                    bookNumber = i;
                    return validNames[i];
                }
            }
        }
        return null;
    }
    private int getChapter(){

        int ch;
        try {
            ch = Integer.valueOf(chapterPart);
            if ((ch>0) && (ch<chapterAmts[bookNumber])){
                return ch;
            }
        }catch (NumberFormatException e){
            return -1;
        }
        return -1;
    }
    private int[] getVerses(){

        ArrayList<Integer>verses = new ArrayList<>();
        verseString = "";

        //First, seperate the comma-dilineated blocks.  Commas are the FIRST way one can denote blocks of verses.
        String[]blocks = versePart.split(",");

        //Now, we will analyze each block.
        for (int i=0; i<blocks.length; i++){
            String block = blocks[i];

            //Cut any unnecessary whitespace off the block.
            block = block.trim();
            //Get rid of any colon used to separate the chapter from the verse.  Don't need it now.
            block = block.replace(":", "");

            //Does the block consists only single verses explicitly written, (e.g. "5 6 9 11"
            // or could a span of verses be included (e.g. "5 6 7-11 15"
            //We figure this by detecting if a "-" is in the block.
            if (block.contains("-")){
                //If so, first lets remove all whitespace surrounding the dash.  This will ensure that
                //the upper and lower bounds of the span of verses are adjacent to the dash on both sides.
                block = block.replaceAll("\\s+-", "-").replaceAll("-\\s+", "-");;

                //Now we have to split by spaces, which is the SECOND way one can choose to separate verses.  These will either now
                //be single verses, or a string of the form 'int-int' for a span.
                String[]subblocks = block.split(" ");
                for (int k=0; k<subblocks.length; k++){
                    //So for each subblock we have to see if it is a span or a single verse.  Again, we check for "-" to determine.
                    String subblock = subblocks[k];
                    subblock.trim();

                    //If it contains a dash, we will get the index of the dash, and use that to try to parse the ints before and after the dash.
                    //If these ints parse with no problem, we will check them for validity, and if that checks out, we will iterate from the lower to the
                    //upper, adding them to the list.
                    if (subblock.contains("-")){
                        int indexOfDash = subblock.indexOf("-");
                        try{
                            int lower = Integer.valueOf(subblock.substring(0, indexOfDash));
                            int upper = Integer.valueOf(subblock.substring(indexOfDash+1, subblock.length()));

                            //If we get here, they parsed, so lets validate them.
                            if (
                                    (lower<upper) && //(Yes, we will require them to put them in the correct order... 11-5 is not valid, but 5-11 is.
                                            (lower>0) &&  //(Can't start at a negative verse number)
                                            (lower<bible.getVerseCount(bookNumber, chapter)-1) &&  //Has to start below the total number of verses in the chapter.
                                            (upper<bible.getVerseCount(bookNumber, chapter))  //Has to end at or below the total number of verses in the chapter.
                                    ){
                                //Now its validated, so we just need to add the range to the verse list.
                                for (int l=lower; l<=upper; l++){
                                    verses.add(l);
                                }
                                verseString = verseString + String.valueOf(lower) + "-" + String.valueOf(upper)+", ";
                            }
                        }catch (NumberFormatException e){
                            //Do nothing.
                        }

                    }else{ //If no "-" it must just be a single integer, so try to parse it, validate it, and add it as a verse to the list.
                        try{
                            int val = Integer.valueOf(subblock); //parse it...
                            if ((val>0) && (val<bible.getVerseCount(bookNumber, chapter))) //... validate it...
                            verses.add(val); //... add it.
                            verseString = verseString + String.valueOf(val)+", ";
                        }catch (NumberFormatException e){
                            //Do nothing.
                        }
                    }
                }

            }//End "if block contains "-" section
            else{
             //If it doesn't contain a "-", then we will assume that only spaces separate any possible digits, so we split by " ".
                String[] subblocks = block.split(" ");
                for (int m=0; m<subblocks.length; m++){
                    String subblock = subblocks[m];
                    subblock.trim();
                    try{
                        int val = Integer.valueOf(subblock); //parse it...
                        if ((val>0) && (val<bible.getVerseCount(bookNumber, chapter))) //... validate it...
                            verses.add(val); //... add it.
                            verseString = verseString + String.valueOf(val)+", ";
                    }catch (NumberFormatException e){
                        //Do nothing.
                    }
                }
            }
        }//end iteration through blocks.

        //finally, set the values to an int[] and return it.
        int[] finalVerses = new int[verses.size()];
        for (int n=0; n<finalVerses.length; n++){
            finalVerses[n] = verses.get(n);
        }
        return finalVerses;
    }

    private boolean uniqueNameStart(String testName){
        int count =0;
        for (String name:validNames){
            if (name.startsWith(testName)){
                count++;
            }
        }
        return (count ==1);
    }
    private WrappedScripture getWrappedScrip(String s, String sub){

        WrappedScripture ws = new WrappedScripture();
        ws.indexStart= s.indexOf(sub);
        ws.indexEnd = ws.indexStart + sub.length()-1;
        ws.replacedText = sub;
        Scripture scrip = Scripture.generate(bookNumber, chapter, verses);
        ws.scripture = scrip;
        String name = validNames[ws.scripture.getBook()];
        name = org.apache.commons.lang3.text.WordUtils.capitalizeFully(name);
        if (verseString.endsWith(", ")){
            verseString = verseString.substring(0, verseString.length()-2);
        }
        name = name+ " " + String.valueOf(chapter) + ":" + verseString;
        ws.newText = name;

        ws.scriptureParsedInfo = String.valueOf(bookNumber)+ " " + String.valueOf(chapter)+" ";
        for (int i=0; i<verses.length; i++){
            ws.scriptureParsedInfo = ws.scriptureParsedInfo + String.valueOf(verses[i])+" ";
        }

        return ws;
    }

    private int getChapterStartIndex (String s){
        String c;
        for (int i=1; i<s.length(); i++){
            c = String.valueOf(s.charAt(i));
            if (digits.contains(c)){
                return i;
            }
        }
        return -1;
    }
    private int getChapterEndIndex(String s, int start){
        for (int i=start; i<s.length(); i++){
            if (!(digits.contains(String.valueOf(s.charAt(i))))){
                return i;
            }
        }
        return -1;
    }
}
