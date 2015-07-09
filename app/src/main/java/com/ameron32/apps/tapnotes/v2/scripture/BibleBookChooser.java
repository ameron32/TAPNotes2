package com.ameron32.apps.tapnotes.v2.scripture;

import java.util.List;

/**
 * Created by klemeilleur on 7/9/2015.
 */
public class BibleBookChooser {

  public int determineBook(final String userInputBook) {
    List<Book> books = Books.getBooks();
    for (Book book : books) {
      int bestGuess = book.getBestGuess(userInput);
      return bestGuess;
    }
    return BOOK_NOT_FOUND;
  }
}
