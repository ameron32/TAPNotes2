package com.ameron32.apps.tapnotes.v2.scripture;

/**
 * Created by Micah on 6/28/2015.
 */
public class BibleBook{
    public int bookNumber;
    public BibleChapter[] chapters;

    public BibleBook(int bookNumber, int chapterCount){
        this.bookNumber = bookNumber;
        chapters = new BibleChapter[chapterCount];
    }
}

