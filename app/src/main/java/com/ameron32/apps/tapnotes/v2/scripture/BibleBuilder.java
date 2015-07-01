package com.ameron32.apps.tapnotes.v2.scripture;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibleBuilder {

    String defaultLangage;
    Resources r;
    Context c;

    //Kris:  To use BibleBuilder:
    //1.  Instantiate it.
    //2.  Don't want the device's default language?  Call setLanguage(String localeCode), passing in the correct localeCode for the language you want.
    //    See http://developer.android.com/reference/java/util/Locale.html for the list.
    //3.  Call getBible(Context c) to get the Bible.

    private int[] chapterQuantities = new int[]{
        50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150,
        31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4,
        28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5,
        1, 1, 1, 22};

    public Bible getBible(Context c) throws BibleResourceNotFoundException{

        defaultLangage = Locale.getDefault().getLanguage();
        this.c = c;
        r = c.getResources();
        return initializeNewBible();
    }

    private Bible initializeNewBible(){

        Bible bible = new Bible();
        bible.books = new BibleBook[66];
        for (int i=0; i<66; i++) {

            bible.books[i] = new BibleBook(i, chapterQuantities[i]);
            for (int j=0; j<chapterQuantities[i]; j++){
                Log.i("SF", "Book "+String.valueOf(i+1)+ " Chapter " + String.valueOf(j+1));
                bible.books[i].chapters[j] = new BibleChapter();
                bible.books[i].chapters[j].verses = loadChapterVerses(i, j);
            }
        }

        return bible;
    }

    public void setLanguage(String localeCode){
        if (isValidLocale(localeCode)){
            defaultLangage = localeCode;
        }
    }

    public String[] loadChapterVerses(int bookNumber, int chapter){

        String filename = getFileName(bookNumber, chapter);
        int fileID = r.getIdentifier(filename, "raw", c.getPackageName());
        InputStream is = r.openRawResource(fileID);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String[] versesText = cleanupChapter(readChapterFile(br), chapter);

        return versesText;
    }

    private StringBuilder readChapterFile(BufferedReader br) {

        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.toString();
        return sb;
    }

    private String[] cleanupChapter(StringBuilder sb, int chapterNumber){

        String verseDelimiter = "chapter"+String.valueOf(chapterNumber+1)+"_verse";
        int verseCount = countSubstring(verseDelimiter, sb.toString());

        String[] verses = new String[verseCount];

        for (int i=0; i<verseCount; i++){
            int start = sb.indexOf(verseDelimiter+String.valueOf(i+1));
            int end = sb.indexOf(verseDelimiter+String.valueOf(i+2));

            if (start!=-1){
                if (end!=-1){
                    verses[i]=sb.substring(start, end-1);
                }
                else{
                    verses[i]=sb.substring(start);
                }
                verses[i]=cleanString(verses[i]);
            }
        }

        for (int j=0; j<verses.length; j++){
            verses[j] = cleanString(verses[j]);
        }
        return verses;
    }

    private String cleanString(String s){

        if (s!=null) {
            ArrayList<TagPair> tags = new ArrayList<>();

            //Check for front tags
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '<') {
                    i = s.length();
                } else {
                    if (s.charAt(i) == '>') {
                        TagPair tp = new TagPair();
                        tp.start = 0;
                        tp.end = i;
                        tags.add(tp);
                        i = s.length();
                    }
                }
            }


            //Check for other tags;
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '<') {
                    int start = j;
                    for (int k = j; k < s.length(); k++) {
                        if (s.charAt(k) == '>') {
                            int end = k;
                            TagPair tp = new TagPair();
                            tp.start = start;
                            tp.end = end;
                            tags.add(tp);
                            k = s.length();
                        }
                    }
                }
            }

            //Check for end tags;
            for (int k = s.length() - 1; k >= 0; k--) {
                if (s.charAt(k) == '>') {
                    k = -1;
                } else {
                    if (s.charAt(k) == '<') {
                        TagPair tp = new TagPair();
                        tp.start = k;
                        tp.end = s.length() - 1;
                        tags.add(tp);
                    }
                }
            }

            s = removeBadTags(s, tags);

        }


            return s;

    }

    private String removeBadTags(String s, ArrayList<TagPair> tags){

        String[] allowedTags = r.getStringArray(R.array.allowed_tags);
        boolean[] remove = new boolean[tags.size()];
        for (int a=0; a<remove.length; a++){
            remove[a]=true;
        }

        for (int i=0; i<tags.size(); i++){
            TagPair tagP = tags.get(i);
            String tag = s.substring(tagP.start, tagP.end+1);
            for (int j=0; j<allowedTags.length; j++){
                if (tag.equals(allowedTags[j])){
                    remove[i]=false;
                }
            }
        }

        StringBuilder sb = new StringBuilder(s);
        for (int k=tags.size()-1; k>=0; k--){
            if (remove[k]){
                sb.replace(tags.get(k).start, tags.get(k).end+1, "");
            }
        }

        //Remove footnotes
        int start = -1;
        boolean footnote = false;

        for (int m=0; m<sb.length(); m++){
            if (sb.charAt(m)=='^'){
                start = m;
                footnote = true;
                m=sb.length();
            }
        }

        if (footnote){

            sb.replace(start, sb.toString().length()-1, "");
        }


        return sb.toString();
    }


    private int countSubstring(String subStr, String str){

        Pattern p = Pattern.compile(subStr);
        Matcher m = p.matcher(str);
        int count = 0;
        while (m.find()){
            count +=1;
        }
        return count;
    }

    private class TagPair{
        public int start;
        public int end;
    }

    private String getFileName(int bookNumber, int chapter){

        StringBuilder filename = new StringBuilder();

        if (defaultLangage.equals("es")){
            //Spanish Language
            if (chapter==0){
                filename
                        .append("b")
                        .append(convertBookNumberToAbbrevSpanish(bookNumber+1));

            }else {
                filename
                        .append("b")
                        .append(convertBookNumberToAbbrevSpanish(bookNumber+1))
                        .append(r.getString(R.string.default_verse_split))
                        .append(String.valueOf(chapter+1));
            }


        }else{
            //Default Language
            if (chapter==0){
                filename
                        .append(r.getString(R.string.default_verse_prefix))
                        .append(String.valueOf(bookNumber + 105));
            }else{
                filename
                        .append(r.getString(R.string.default_verse_prefix))
                        .append(String.valueOf(bookNumber + 105))
                        .append(r.getString(R.string.default_verse_split))
                        .append(String.valueOf(chapter+1));
            }
        }
        return filename.toString();
    }

    private String convertBookNumberToAbbrevSpanish(int bookNumber) {
        switch (bookNumber) {
            case 1: {
                return "ge";
            }
            case 2: {
                return "ex";
            }
            case 3: {
                return "le";
            }
            case 4: {
                return "nu";
            }
            case 5: {
                return "dt";
            }
            case 6: {
                return "jos";
            }
            case 7: {
                return "jue";
            }
            case 8: {
                return "rut";
            }
            case 9: {
                return "1sa";
            }
            case 10: {
                return "2sa";
            }
            case 11: {
                return "1re";
            }
            case 12: {
                return "2re";
            }
            case 13: {
                return "1cr";
            }
            case 14: {
                return "2cr";
            }
            case 15: {
                return "esd";
            }
            case 16: {
                return "ne";
            }
            case 17: {
                return "est";
            }
            case 18: {
                return "job";
            }
            case 19: {
                return "sl";
            }
            case 20: {
                return "pr";
            }
            case 21: {
                return "ec";
            }
            case 22: {
                return "can";
            }
            case 23: {
                return "isa";
            }
            case 24: {
                return "jer";
            }
            case 25: {
                return "lam";
            }
            case 26: {
                return "eze";
            }
            case 27: {
                return "da";

            }
            case 28: {
                return "os";

            }
            case 29: {
                return "joe";

            }
            case 30: {
                return "am";
            }
            case 31: {
                return "abd";

            }
            case 32: {

                return "jon";

            }
            case 33: {
                return "miq";

            }
            case 34: {
                return "nah";

            }
            case 35: {
                return "hab";

            }
            case 36: {
                return "sof";

            }
            case 37: {
                return "ag";

            }
            case 38: {
                return "zac";

            }
            case 39: {
                return "mal";

            }
            case 40: {
                return "mt";

            }
            case 41: {
                return "mr";

            }
            case 42: {
                return "lu";

            }
            case 43: {
                return "jn";

            }
            case 44: {
                return "hch";

            }
            case 45: {
                return "ro";

            }
            case 46: {
                return "1co";

            }
            case 47: {
                return "2co";

            }
            case 48: {
                return "gal";

            }
            case 49: {
                return "ef";

            }
            case 50: {
                return "flp";

            }
            case 51: {
                return "col";

            }
            case 52: {
                return "1te";

            }
            case 53: {
                return "2te";

            }
            case 54: {
                return "1ti";

            }
            case 55: {
                return "2ti";

            }
            case 56: {
                return "tit";

            }
            case 57: {
                return "flm";

            }
            case 58: {
                return "heb";

            }
            case 59: {
                return "snt";

            }
            case 60: {
                return "1pe";

            }
            case 61: {
                return "2pe";

            }
            case 62: {
                return "1jn";

            }
            case 63: {
                return "2jn";

            }
            case 64: {
                return "3jn";

            }
            case 65: {
                return "jud";

            }
            case 66: {
                return "rev";

            }
            default: {
                return "";
            }
        }


    }

    boolean isValidLocale(String value) {
        Locale []locales = Locale.getAvailableLocales();
        for (Locale l : locales) {
            if (value.equals(l.toString())) {
                return true;
            }
        }
        return false;
    }
}
