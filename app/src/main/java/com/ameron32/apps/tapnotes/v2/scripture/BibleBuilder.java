package com.ameron32.apps.tapnotes.v2.scripture;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibleBuilder {

  private String defaultLanguage;
  private Resources r;
  private Context c;

  //Kris:  To use BibleBuilder:
  //1.  Instantiate it.
  //2.  Don't want the device's default language?  Call setLanguage(String localeCode), passing in the correct localeCode for the language you want.
  //    See http://developer.android.com/reference/java/util/Locale.html for the list.
  //3.  Call getBible(Context c) to get the Bible.

  private int[] chapterQuantities;
  private String[] bookNames;
  private String[] bookAbbreviations;

  public Bible getBible(Context c) throws BibleResourceNotFoundException {

    defaultLanguage = Locale.getDefault().getLanguage();
    this.c = c;
    r = c.getResources();
    chapterQuantities = r.getIntArray(R.array.chapter_quantities);
    bookNames = r.getStringArray(R.array.bible_books);
    bookAbbreviations = r.getStringArray(R.array.book_abbr);
    return initializeNewBible();
  }

  private Bible initializeNewBible() {

    Bible bible = new Bible();
    bible.books = new BibleBook[66];
    bible.setChapterNames(bookNames);
    bible.setAbbrevs(bookAbbreviations);
    for (int i = 0; i < 66; i++) {

      bible.books[i] = new BibleBook(i, chapterQuantities[i]);
      for (int j = 0; j < chapterQuantities[i]; j++) {
        Log.i("SF", "Book " + String.valueOf(i + 1) + " Chapter " + String.valueOf(j + 1));
        bible.books[i].chapters[j] = new BibleChapter();
        bible.books[i].chapters[j].verses = loadChapterVerses(i, j);
      }
    }

    return bible;
  }

  public void setLanguage(String localeCode) {

    if (isValidLocale(localeCode)) {
      defaultLanguage = localeCode;
    }
  }

  private String filename;
  private int fileID;
  private InputStream is;
  private InputStreamReader isr;
  private BufferedReader br;
  private String[] versesText;

  public String[] loadChapterVerses(int bookNumber, int chapter) {

    filename = getFileName(bookNumber, chapter);
    fileID = r.getIdentifier(filename, "raw", c.getPackageName());
    is = r.openRawResource(fileID);
    isr = new InputStreamReader(is);
    br = new BufferedReader(isr);
    versesText = cleanupChapter(readChapterFile(br), chapter);

    return versesText;
  }

  private StringBuilder sb;
  private String line;

  private StringBuilder readChapterFile(BufferedReader br) {

    sb = new StringBuilder();
    //  line = null;

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

  private String verseDelimiter1;
  private int verseCount1;
  private String[] verses1;
  private int i1, j1, start1, end1;

  private String[] cleanupChapter(StringBuilder sb, int chapterNumber) {

    verseDelimiter1 = "chapter" + String.valueOf(chapterNumber + 1) + "_verse";
    verseCount1 = countSubstring(verseDelimiter1, sb.toString());

    verses1 = new String[verseCount1];

    for (i1 = 0; i1 < verseCount1; i1++) {
      start1 = sb.indexOf(verseDelimiter1 + String.valueOf(i1 + 1));
      end1 = sb.indexOf(verseDelimiter1 + String.valueOf(i1 + 2));

      if (start1 != -1) {
        if (end1 != -1) {
          verses1[i1] = sb.substring(start1, end1 - 1);
        } else {
          verses1[i1] = sb.substring(start1);
        }
        verses1[i1] = cleanString(verses1[i1]);
      }
    }

    for (j1 = 0; j1 < verses1.length; j1++) {
      verses1[j1] = cleanString(verses1[j1]);
    }
    return verses1;
  }

  private ArrayList<TagPair> tags2 = new ArrayList<>();
  private int i2, j2, k2a, k2b, start2, end2;
  private TagPair tp2;

  private String cleanString(String s) {

    if (s != null) {
      tags2.clear();

      //Check for front tags
      for (i2 = 0; i2 < s.length(); i2++) {
        if (s.charAt(i2) == '<') {
          i2 = s.length();
        } else {
          if (s.charAt(i2) == '>') {
            tp2 = new TagPair();
            tp2.start = 0;
            tp2.end = i2;
            tags2.add(tp2);
            i2 = s.length();
          }
        }
      }


      //Check for other tags;
      for (j2 = 0; j2 < s.length(); j2++) {
        if (s.charAt(j2) == '<') {
          start2 = j2;
          for (k2a = j2; k2a < s.length(); k2a++) {
            if (s.charAt(k2a) == '>') {
              end2 = k2a;
              tp2 = new TagPair();
              tp2.start = start2;
              tp2.end = end2;
              tags2.add(tp2);
              k2a = s.length();
            }
          }
        }
      }

      //Check for end tags;
      for (k2b = s.length() - 1; k2b >= 0; k2b--) {
        if (s.charAt(k2b) == '>') {
          k2b = -1;
        } else {
          if (s.charAt(k2b) == '<') {
            tp2 = new TagPair();
            tp2.start = k2b;
            tp2.end = s.length() - 1;
            tags2.add(tp2);
          }
        }
      }

      s = removeBadTags(s, tags2);

    }


    return s;

  }

  private String[] allowedTags3;
  private boolean[] remove3;
  private int a3, i3, j3, k3, m3, start3;
  private TagPair tagP3;
  private String tag3;
  private StringBuilder sb3;
  private boolean footnote3;

  private String removeBadTags(String s, ArrayList<TagPair> tags) {

    allowedTags3 = r.getStringArray(R.array.allowed_tags);
    remove3 = new boolean[tags.size()];
    for (a3 = 0; a3 < remove3.length; a3++) {
      remove3[a3] = true;
    }

    for (i3 = 0; i3 < tags.size(); i3++) {
      tagP3 = tags.get(i3);
      tag3 = s.substring(tagP3.start, tagP3.end + 1);
      for (j3 = 0; j3 < allowedTags3.length; j3++) {
        if (tag3.equals(allowedTags3[j3])) {
          remove3[i3] = false;
        }
      }
    }

    sb3 = new StringBuilder(s);
    for (k3 = tags.size() - 1; k3 >= 0; k3--) {
      if (remove3[k3]) {
        sb3.replace(tags.get(k3).start, tags.get(k3).end + 1, "");
      }
    }

    //Remove footnotes
    start3 = -1;
    footnote3 = false;

    for (m3 = 0; m3 < sb3.length(); m3++) {
      if (sb3.charAt(m3) == '^') {
        start3 = m3;
        footnote3 = true;
        m3 = sb3.length();
      }
    }

    if (footnote3) {

      sb3.replace(start3, sb3.toString().length() - 1, "");
    }


    return sb3.toString();
  }

  private Pattern p4;
  private Matcher m4;
  private int count4;

  private int countSubstring(String subStr, String str) {

    p4 = Pattern.compile(subStr);
    m4 = p4.matcher(str);
    count4 = 0;
    while (m4.find()) {
      count4 += 1;
    }
    return count4;
  }

  private StringBuilder filename5;

  private String getFileName(int bookNumber, int chapter) {

    filename5 = new StringBuilder();

    if (defaultLanguage.equals("es")) {
      //Spanish Language
      if (chapter == 0) {
        filename5
            .append("b")
            .append(convertBookNumberToAbbrevSpanish(bookNumber + 1));

      } else {
        filename5
            .append("b")
            .append(convertBookNumberToAbbrevSpanish(bookNumber + 1))
            .append(r.getString(R.string.default_verse_split))
            .append(String.valueOf(chapter + 1));
      }


    } else {
      //Default Language
      if (chapter == 0) {
        filename5
            .append(r.getString(R.string.default_verse_prefix))
            .append(String.valueOf(bookNumber + 105));
      } else {
        filename5
            .append(r.getString(R.string.default_verse_prefix))
            .append(String.valueOf(bookNumber + 105))
            .append(r.getString(R.string.default_verse_split))
            .append(String.valueOf(chapter + 1));
      }
    }
    return filename5.toString();
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

  private Locale[] locales6;

  boolean isValidLocale(String value) {
    locales6 = Locale.getAvailableLocales();
    for (Locale l : locales6) {
      if (value.equals(l.toString())) {
        return true;
      }
    }
    return false;
  }

  private class TagPair {
    public int start;
    public int end;
  }
}
