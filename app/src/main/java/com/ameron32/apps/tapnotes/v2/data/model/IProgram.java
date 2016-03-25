package com.ameron32.apps.tapnotes.v2.data.model;

/**
 * Created by klemeilleur on 6/29/2015.
 */
public interface IProgram extends IObject {

  ITalk[] getTalks();
  String getName();
}
