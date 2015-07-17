package com.ameron32.apps.tapnotes.v2.scripture;

import com.ameron32.apps.tapnotes.v2.ui.mc_sanitizer.IVerseVerifier;

/**
 * Created by Micah on 6/24/2015.
 */
public class ScriptureFinder implements IVerseVerifier {

  public String[] getVerses(Bible b, int bookNumber, int chapter, int[]verses) throws ScriptureNotFoundException{

    validateRequest(b, bookNumber, chapter, verses);

    String[] verseText = new String[verses.length];
    for (int i=0; i<verses.length; i++){
      verseText[i] = b.books[bookNumber].chapters[chapter].verses[verses[i]];
    }

    return verseText;
  }

  public String[] getChapter(Bible b, int bookNumber, int chapter) throws ScriptureNotFoundException{

    validateRequest(b, bookNumber, chapter, null);

    int totalVerses = b.books[bookNumber].chapters.length;
    int[] verses = new int[totalVerses];
    for (int i=0; i<totalVerses; i++){
      verses[i]=i;
    }
    return getVerses(b, bookNumber, chapter, verses);
  }

  private void validateRequest(Bible b, int bookNumber, int chapter, int[]verses) throws ScriptureNotFoundException{

    if ((bookNumber<0)||(bookNumber>65)){
      throw new ScriptureNotFoundException(ScriptureNotFoundException.BOOK_NOT_FOUND);
    }

    int totalChapters = b.books[bookNumber].chapters.length;
    if ((chapter<0)||(chapter>totalChapters-1)){
      throw new ScriptureNotFoundException(ScriptureNotFoundException.CHAPTER_NOT_FOUND);
    }

    if (verses!=null){
      int totalVerses = b.books[bookNumber].chapters[chapter].verses.length;
      for (int a=0; a<verses.length; a++){
        if ((verses[a]<0)||(verses[a]>totalVerses)){
          throw new ScriptureNotFoundException(ScriptureNotFoundException.VERSE_NOT_FOUND);
        }
      }
    }

  }

  @Override
  public boolean verseValid(int book, int chapter, int verse) {
    return false;
  }

  public class ScriptureNotFoundException extends Exception{

    private int type;
    public final static int BOOK_NOT_FOUND = 0;
    public final static int CHAPTER_NOT_FOUND = 1;
    public final static int VERSE_NOT_FOUND = 2;

    public ScriptureNotFoundException(int code){
      type = code;
    }

    public int getType(){
      return type;
    }

  }

}
