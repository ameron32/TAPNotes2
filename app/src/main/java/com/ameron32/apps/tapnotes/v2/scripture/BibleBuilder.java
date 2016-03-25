package com.ameron32.apps.tapnotes.v2.scripture;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.data.parse.Constants;
import com.ameron32.apps.tapnotes.v2.util.Serializer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibleBuilder {

  private static final String TAG = BibleBuilder.class.getSimpleName();

  private String defaultLanguage;
  private Resources resources;
  private Context context;

  //Kris:  To use BibleBuilder:
  //1.  Instantiate it.
  //2.  Don't want the device's default language?  Call setLanguage(String localeCode), passing in the correct localeCode for the language you want.
  //    See http://developer.android.com/reference/java/util/Locale.html for the list.
  //3.  Call getBible(Context c) to get the Bible.

  private int[] chapterQuantities;
  private String[] bookNames;
  private String[] bookAbbreviations;
  private final Serializer<Bible> serializer;

  public BibleBuilder(final Context context) {
    defaultLanguage = Locale.getDefault().getLanguage();
      // e.g. "bible-en.bible"
      programFilename = Constants.SERIALIZER_FILE_BIBLE_PREFIX + defaultLanguage +
          Constants.SERIALIZER_FILE_BIBLE_EXTENSION;
    this.context = context;
    this.resources = context.getResources();
    this.chapterQuantities = resources.getIntArray(R.array.chapter_quantities);
    this.bookNames = resources.getStringArray(R.array.bible_books);
    this.bookAbbreviations = resources.getStringArray(R.array.book_abbr);

    this.serializer = new Serializer<>(Bible.class);

    // from removeBadTags(), single initialization
    this.allowedTags3 = resources.getStringArray(R.array.allowed_tags);
  }

  private String getAppVersionLabel() {
    try {
      final PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return "-" + pInfo.versionName + "-" + pInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Bible getBible() throws BibleResourceNotFoundException {
    // attempt to recover a serialized copy of this bible in this language
    Bible bible;
    bible = loadSerializedBible();
    if (bible != null) {
      return bible;
    }

    // if no copy can be found, initialize a new copy
    bible = initializeNewBible();

    // serialize the new copy for future recovery
    storeSerializedBible(bible);

    // then return the new copy of the Bible
    return bible;
  }



  private final String programFilename;

  private Bible loadSerializedBible() {
    if (programFilename == null) {
      return null;
    }

    try {
      final Bible bible = serializer.load(context, programFilename);
      Log.d(TAG, "bible loaded as: " + programFilename);
      return bible;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean storeSerializedBible(final Bible bible) {
    if (programFilename == null) {
      return false;
    }

    try {
      boolean result = serializer.save(context, programFilename, bible);
      Log.d(TAG, "bible serialized as: " + programFilename);
      return result;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private Bible initializeNewBible() {

    final Bible bible = new Bible();
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

  public void setLanguage(final String localeCode) {

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

  public String[] loadChapterVerses(final int bookNumber, final int chapter) {

    filename = getFileName(bookNumber, chapter);
    fileID = resources.getIdentifier(filename, "raw", context.getPackageName());
    is = resources.openRawResource(fileID);
    isr = new InputStreamReader(is);
    br = new BufferedReader(isr);
    versesText = cleanupChapter(readChapterFile(br), chapter);

    return versesText;
  }

  private StringBuilder sb;
  private String line;

  private StringBuilder readChapterFile(final BufferedReader br) {

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

  private String[] cleanupChapter(final StringBuilder sb, final int chapterNumber) {

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

  private final ArrayList<TagPair> tags2 = new ArrayList<>(1024);
  private int i2, j2, k2a, k2b, start2, end2;
  private TagPair tp2;
  private int length2;
  private String string2;

  private String cleanString(final String str) {

    string2 = str;

    if (string2 != null) {
      tags2.clear();
      length2 = string2.length();

      //Check for front tags
      for (i2 = 0; i2 < length2; i2++) {
        if (string2.charAt(i2) == '<') {
          i2 = length2;
        } else {
          if (string2.charAt(i2) == '>') {
            tp2 = new TagPair();
            tp2.start = 0;
            tp2.end = i2;
            tags2.add(tp2);
            i2 = length2;
          }
        }
      }


      //Check for other tags;
      for (j2 = 0; j2 < length2; j2++) {
        if (string2.charAt(j2) == '<') {
          start2 = j2;
          for (k2a = j2; k2a < length2; k2a++) {
            if (string2.charAt(k2a) == '>') {
              end2 = k2a;
              tp2 = new TagPair();
              tp2.start = start2;
              tp2.end = end2;
              tags2.add(tp2);
              k2a = length2;
            }
          }
        }
      }

      //Check for end tags;
      for (k2b = length2 - 1; k2b >= 0; k2b--) {
        if (string2.charAt(k2b) == '>') {
          k2b = -1;
        } else {
          if (string2.charAt(k2b) == '<') {
            tp2 = new TagPair();
            tp2.start = k2b;
            tp2.end = length2 - 1;
            tags2.add(tp2);
          }
        }
      }

      string2 = removeBadTags(string2, tags2);
      string2 = alterSupToSupSmallA(string2);
      string2 = alterSupToSupSmallB(string2);

    }


    return string2;

  }

  private String[] allowedTags3;
  private boolean[] remove3;
  private int a3, i3, j3, k3, m3, start3;
  private TagPair tagP3;
  private String tag3;
  private StringBuilder sb3;
  private boolean footnote3;
  private int length3;
  private int tagsSize3;
  private int sb3length3;

  // FIXME CODE DISABLED, RELATED TO CODE IN removeBadTags()
//  private static final String startP3 = "<p id";
//  private static final String endP3 = "</p>";

  private String removeBadTags(final String s, final ArrayList<TagPair> tags) {

    // moved to INITIALIZATION, only 1 time needed
    // allowedTags3 = resources.getStringArray(R.array.allowed_tags);

    sb3 = new StringBuilder(s);


    tagsSize3 = tags.size();
    remove3 = new boolean[tagsSize3];
    length3 = remove3.length;
    for (a3 = 0; a3 < length3; a3++) {
      remove3[a3] = true;
    }

    for (i3 = 0; i3 < tagsSize3; i3++) {
      tagP3 = tags.get(i3);
      tag3 = s.substring(tagP3.start, tagP3.end + 1);

      // iterate through possible allowed tags
      for (j3 = 0; j3 < allowedTags3.length; j3++) {
        if (tag3.equals(allowedTags3[j3])) {
          remove3[i3] = false;
        }
      }

      // TODO check the usefulness of this code
      // if the tag is not yet allowed, do a check for <p>
      // FIXME CODE DISABLED BECAUSE IT INTRODUCED A ">" IN THE LAST VERSE OF EVERY CHAPTER
//      if (remove3[i3] == true) {
//        if (tag3.equals(endP3)) {
//          remove3[i3] = false;
//        }
//      }
//
//      if (remove3[i3] == true) {
//        if (tag3.startsWith(startP3)) {
//          remove3[i3] = false;
//        }
//      }
      // end useful code
    }

    for (k3 = tagsSize3 - 1; k3 >= 0; k3--) {
      if (remove3[k3]) {
        sb3.replace(tags.get(k3).start, tags.get(k3).end + 1, "");
      }
    }

    start3 = -1;
    footnote3 = false;
    sb3length3 = sb3.length();

    for (m3 = 0; m3 < sb3length3; m3++) {
      if (sb3.charAt(m3) == '^') {
        start3 = m3;
        footnote3 = true;
        m3 = sb3length3;
      }
    }

    if (footnote3) {

      sb3.replace(start3, sb3.toString().length() - 1, "");
    }

    return sb3.toString();
  }


  private static final String r01 = "<sup>";
  private static final String r02 = "</sup>";
  private static final String w01 = "<sup><small>";
  private static final String w02 = "</small></sup>";
  private String alterSupToSupSmallA(final String string) {
    return string.replaceAll(r01, w01);
  }
  private String alterSupToSupSmallB(final String string) {
    return string.replaceAll(r02, w02);
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
            .append(resources.getString(R.string.default_verse_split))
            .append(String.valueOf(chapter + 1));
      }


    } else {
      //Default Language
      if (chapter == 0) {
        filename5
            .append(resources.getString(R.string.default_verse_prefix))
            .append(String.valueOf(bookNumber + 105));
      } else {
        filename5
            .append(resources.getString(R.string.default_verse_prefix))
            .append(String.valueOf(bookNumber + 105))
            .append(resources.getString(R.string.default_verse_split))
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
