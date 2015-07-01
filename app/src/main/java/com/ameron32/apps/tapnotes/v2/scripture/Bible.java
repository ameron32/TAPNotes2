package com.ameron32.apps.tapnotes.v2.scripture;

public class Bible {

    public BibleBook[] books;

    public String getVerse (int bookNumber, int chapter, int verse){
        return books[bookNumber].chapters[chapter].verses[verse];
    }

}
