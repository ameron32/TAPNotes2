package com.ameron32.apps.tapnotes.v2.scripture;

public class Bible {

    public BibleBook[] books;

    public String getVerse (int bookNumber, int chapter, int verse){
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

}
