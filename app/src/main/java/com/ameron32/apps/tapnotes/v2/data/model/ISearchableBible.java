package com.ameron32.apps.tapnotes.v2.data.model;

/**
 * Created by klemeilleur on 7/10/2015.
 */
public interface ISearchableBible extends IBible {

  int findBibleBook(String interpretableString);
}
