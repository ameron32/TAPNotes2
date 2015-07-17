package com.ameron32.apps.tapnotes.v2.scripture;

import com.ameron32.apps.tapnotes.v2.model.IScripture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klemeilleur on 7/8/2015.
 */
public final class Scripture implements IScripture {

  public static List<Scripture> generateAll(Bible bible, String verseCodes) {
    final List<String> codes = Tools.splitMultiScripture(verseCodes);
    final List<Scripture> scriptures = new ArrayList<>(codes.size());
    for (int i = 0; i < codes.size(); i++) {
      scriptures.add(generate(bible, codes.get(i)));
    }
    return scriptures;
  }

  public static Scripture generate(Bible bible, String verseCode) {
    try {
      Tools.extractScripture(bible, verseCode);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Scripture generate(int book, int chapter, int[] verses) {
    return new Scripture(book, chapter, verses);
  }



  private final int book;
  private final int chapter;
  private final int[] verses;

  private Scripture(int book, int chapter, int[] verses) {
    this.book = book;
    this.chapter = chapter;
    this.verses = verses;
  }

  public int getBook() {
    return book;
  }

  public int getChapter() {
    return chapter;
  }

  public int[] getVerses() {
    return verses;
  }
}
