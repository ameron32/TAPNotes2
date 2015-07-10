package com.ameron32.apps.tapnotes.v2.scripture;

import com.ameron32.apps.tapnotes.v2.model.IScripture;
import com.ameron32.apps.tapnotes.v2.util.LocaleUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

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



  /**
   *  TODO--NEEDS FINAL EVERYWHERE POSSIBLE
   */
  public static class Tools {

    /**
     *
     * @param note
     *          String containing leading '@' followed by book chapter and verses,
     *          dilimited by spaces.
     *
     *          BOOK accepted as any-case abbreviation, full book name, or first
     *          few characters for lookup.
     *
     *          CHAPTER accepts 1 integer.
     *
     *          VERSES accepts any combination of integer, integer series, and
     *          integer list concatenated with commas. VERSES integer series begin
     *          with the start verse, end with the last verse, and concatenate by
     *          '-' hyphens.
     *
     *          FORMAT: CCC # #-#, Examples: JAS 1 1,2; REVELATION 21 3-5; matt 24
     *          3-11,14,45-47;
     *
     *
     * @return Scripture containing proper int BOOK, int CHAPTER, and
     *         int[] VERSES containing each and every verse int referred to in
     *         list and series'.
     * @throws Exception
     */
    private static Scripture extractScripture(Bible bible, String note)
        throws Exception {

      String bookString;
      int bookNumber;
      int chapter;
      int[] verses;

      final String[] bcv = note.split(" ");
      final String sVerses = bcv[bcv.length - 1];
      final String sChapter = bcv[bcv.length - 2];
      final StringBuilder sb = new StringBuilder();
      for (int i = 0; i < bcv.length - 2; i++) {
        sb.append(bcv[i]);
        sb.append(" ");
      }

      bookString = sb.toString().trim()
          .substring(1)
          .replace(".", "")
          .replace(":", " ")
          .toUpperCase(LocaleUtil.getMachineLocale());

      bookNumber = determineBook(bible, bookString);

      chapter = Integer.valueOf(sChapter);

      verses = convert(extractVerseBlocks(sVerses));

      final Scripture scripture = new Scripture(bookNumber, chapter, verses);
      return scripture;
    }

    private static List<Integer> extractVerseBlocks(final String codedVerses) {
      List<Integer> poolVerses = new ArrayList<>();
      if (codedVerses.contains(",")) { // multiblocks
        String[] verseBlocks = codedVerses.split(",");

        for (String block : verseBlocks) {
          poolVerses.addAll(extractVerses(block));
        }
      }
      else {
        poolVerses.addAll(extractVerses(codedVerses));
      }
      return poolVerses;
    }

    private static List<Integer> extractVerses(final String codedVerses) {
      String sStartVerse;
      String sEndVerse;

      if (codedVerses.contains("-")) { // verses
        String[] verses = codedVerses.split("-");
        sStartVerse = verses[0];
        sEndVerse = verses[1];
      }
      else { // 1verse
        String verse = codedVerses;
        sStartVerse = sEndVerse = verse;
      }

      int startVerse = Integer.valueOf(sStartVerse);
      int endVerse = Integer.valueOf(sEndVerse);
      List<Integer> poolVerses = new ArrayList<>();
      for (int i = startVerse; i < endVerse + 1; i++) {
        poolVerses.add(i);
      }

      return poolVerses;
    }

    private static int[] convert(final List<Integer> from) {

      final int[] c = new int[from.size()];
      for (int i = 0; i < from.size(); i++) {
        c[i] = from.get(i);
      }

      final Set<Integer> filter = new TreeSet<>();
      for (int i = 0; i < c.length; i++) {
        filter.add(c[i]);
      }

      final Iterator<Integer> it = filter.iterator();
      final int[] d = new int[filter.size()];
      int i = 0;
      while (it.hasNext()) {
        d[i] = it.next();
        i++;
      }

      return d;
    }

    /**
     * FORCED TO UPPERCASE
     */
    private static int determineBook(Bible bible, String userInput) {
      final BibleBookChooser bbc = new BibleBookChooser();
      return bbc.determineBook(bible, userInput);
    }

    public static List<String> splitMultiScripture(String verseCodes) {
      // TODO  coordinate all possible inputs to break into individual scriptures
      return Arrays.asList(new String[]{"empty"});
    }
  }
}
