package com.ameron32.apps.tapnotes.v2.scripture;

import com.ameron32.apps.tapnotes.v2.data.model.IBible;

import java.io.Serializable;

public class Bible implements IBible, Serializable {

  // CHANGE THIS WHEN CHANGES MAKE THE BIBLE INCOMPATIBLE
  private static final long serialVersionUID = 0L;


  private static final int NUMBER_OF_BOOKS = 66;
  public BibleBook[] books;
  private String[] chapterNames;
  private String[] chapterAbbrevs;

  public String getVerse(int bookNumber, int chapter, int verse) {
    return books[bookNumber].chapters[chapter].verses[verse];
  }

  public String getVerses(int bookNumber, int chapter, int... verses) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < verses.length; i++) {
      int verse = verses[i];
      sb.append(getVerse(bookNumber, chapter, verse));
    }
    return sb.toString();
  }

  @Override
  public int findBibleBook(String interpretableString) {
    // TODO MICAH: findBibleBook
    // Compare perfect non-case specific match with Book Names
    // then compare perfect non-case specific match with Book Abbreviations
    // then compare startsWith non-case specific match with Book Names
    return 0;
  }

  @Override
  public int getBookCount() {
    return NUMBER_OF_BOOKS;
  }

  @Override
  public int getChapterCount(int book) {
    if ((book >= 0) && (book < 66))
      return books[book].chapters.length;
    return -1;
  }

  @Override
  public int getVerseCount(int book, int chapter) {
    if ((book >= 0) && (book < 66)) {
      if ((chapter >= 0) && (chapter < books[book].chapters.length)) {
        return books[book].chapters[chapter].verses.length;
      }
    }
    return -1;
  }

  @Override
  public void setChapterNames(String[] names) {
    chapterNames = names;
  }

  @Override
  public void setAbbrevs(String[] abbrevs) {
    chapterAbbrevs = abbrevs;
  }

  @Override
  public String getChapterName(int chapter) {
    if ((chapter >= 0) && (chapter < 66) && (chapterNames != null)) {
      return chapterNames[chapter];
    }
    return null;
  }

  @Override
  public String getChapterAbbrev(int chapter) {
    if ((chapter >= 0) && (chapter < 66) && (chapterAbbrevs != null)) {
      return chapterAbbrevs[chapter];
    }
    return null;
  }
}
