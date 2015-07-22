package com.ameron32.apps.tapnotes.v2.model;

/**
 * Created by klemeilleur on 7/10/2015.
 */
public interface IBible {

  int findBibleBook(String interpretableString);

  int getBookCount();
  int getChapterCount(int book);
  int getVerseCount(int book, int chapter);

  void setChapterNames(String[] names);
  void setAbbrevs(String[] abbrevs);

  String getChapterName(int chapter);
  String getChapterAbbrev(int chapter);
}
