package com.ameron32.apps.tapnotes.v2.data.model;

/**
 * Created by klemeilleur on 4/15/2016.
 */
public interface IBible {

  int getBookCount();
  int getChapterCount(int book);
  int getVerseCount(int book, int chapter);

  void setChapterNames(String[] names);
  void setAbbrevs(String[] abbrevs);

  String getChapterName(int chapter);
  String getChapterAbbrev(int chapter);
}
