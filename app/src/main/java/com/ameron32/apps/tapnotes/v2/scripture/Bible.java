package com.ameron32.apps.tapnotes.v2.scripture;

import com.ameron32.apps.tapnotes.v2.model.IBible;

public class Bible implements IBible {

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

    @Override
    public int findBibleBook(String interpretableString) {
        // TODO MICAH: findBibleBook
        // Compare perfect non-case specific match with Book Names
        // then compare perfect non-case specific match with Book Abbreviations
        // then compare startsWith non-case specific match with Book Names
        return 0;
    }
}
