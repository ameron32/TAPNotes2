package com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer;

import android.content.Context;
import android.content.res.Resources;

import com.ameron32.apps.tapnotes.v2.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Micah on 7/16/2015.
 */
public class Sanitizer implements ISanitizer{

    String[] validNames;
    int[] chapterAmts;

    String book;
    int bookNumber;
    int chapter;
    int[] verses;

    String[] words;

    Pattern digitP = Pattern.compile("\\d+");
    Pattern digitV = Pattern.compile("[\\d+]|[-]");

    IVerseVerifier vv;
    ISanitizerCallbacks mCallback;

    public Sanitizer(Context context) {
        // TODO: MICAH CONSIDER MERGING bible_books & book_names
        // SEE strings_biblebooks.xml & strings_bible_book_reference.xml
        validNames = context.getResources().getStringArray(R.array.bible_books);
        chapterAmts = context.getResources().getIntArray(R.array.chapter_quantities);
    }

    public void setVerseVerifier(IVerseVerifier v){
        vv = v;
    }

    public void setCallback(ISanitizerCallbacks callback){mCallback = callback;};

    public Sanitizer(Resources r){
        validNames = r.getStringArray(R.array.bible_books);
        chapterAmts = r.getIntArray(R.array.chapter_quantities);
    }



    @Override
    public void testForScriptures(String s) {

        String[] subs = s.split("@");
        if (subs.length ==1) mCallback.onSanitizerResults(null);
       else{
            reallyCheck(s, subs);
        }
    }

    private WrappedScripture reallyCheck(String s, String[] subs){

        //We are only interested in the last one.
        String sub = subs[subs.length-1];
        words = sub.toUpperCase().split(" ");


        book = "";


            book = getBookName();

            if (book == ""){
                mCallback.onSanitizerResults(null);
            }else{
                 chapter = getChapter();
                if (chapter <0){
                    mCallback.onSanitizerResults(null);
                }else{
                    verses = getVerses();
                    if (verses == null)
                        mCallback.onSanitizerResults(null);
                    else{
                        //Actually found a scripture, return it.
                    }
                }


            }

        return null;
    }



    private String getBookName(){

        int wCount = words.length;
        String [] wordsWOName;

        String name = words[0];

        if (((name == "1") || (name == "2") || (name == "3")) && (wCount>1)){
            name = words[0] + " " + words[1];
        }

        if (!uniqueNameStart(name))
        return "";

        for (int i=0; i<validNames.length; i++){
            String vName = validNames[i];
            if (vName.startsWith(name)){
                bookNumber = i;
                wordsWOName = words.clone();
                for (int j=0; j<3; j++){
                    if (vName.contains(wordsWOName[j])){
                        wordsWOName[j]="";
                    }
                }
                words = wordsWOName;
                return vName;
            }
        }


        return "";
    }

    private int getChapter(){

        for (int i=0; i<words.length; i++){
            if (words[i]!=""){
                Matcher m = digitP.matcher(words[i]);
                try{
                    if (m.find()){
                        int val = Integer.valueOf(m.group());
                        if ((val>0)&&(val <=chapterAmts[bookNumber])){
                            words[i]="";
                            return val;
                        }
                    }
                }catch (NumberFormatException e){
                    return -1;
                }
            }
        }
        return -1;

    }

    private int[] getVerses(){
        int[] verses = new int[words.length];
        for (int i=0; i<words.length; i++){
            if (words[i]!=""){
                verses[i]= pullOutInt(words[i]);
            }
        }

        ArrayList<Integer> actualVerses = new ArrayList<>();
        for (int j=0; j<verses.length; j++){
            if (verses[j]>0){
                actualVerses.add(verses[j]);
            }
            else{
                if ((verses[j]==-2)&&(j>0) && (j<verses.length-2)){
                    int lower = verses[j-1];
                    int upper = verses[j+1];
                    if (lower<upper){
                        for (int k=lower; k<=upper; k++){
                            actualVerses.add(k);
                        }
                    }
                }
            }
        }

        for (int m=actualVerses.size()-1; m>=0; m--){
            if (!vv.verseValid(bookNumber, chapter, actualVerses.get(m))){
                actualVerses.remove(m); //TODO: Concurrent mod exception here?  We'll see. - MC
            }
        }

        return verses;
    }

    private boolean uniqueNameStart(String s){

        int count =0;

        for (String name:validNames){
            if (name.startsWith(s)){
                count++;
            }
        }

        return (count ==1);
    }

    private int pullOutInt(String s) {
        Matcher m = digitV.matcher(s);
        m.find();
        String x = m.group();
        if (x == "-") return -2;
        else
            try {
                int val = Integer.valueOf(m.group());
                return val;

            } catch (NumberFormatException e) {
                return -1;
            }
    }

   // SCRIPTURE_PATTERN = "(@)([0-9,A-Z])\\w+(\\s)(\\d)+([\\s,:])([\\d-,])+";


}
