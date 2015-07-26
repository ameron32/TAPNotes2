package com.ameron32.apps.tapnotes.v2.util;

import android.util.Log;

import com.ameron32.apps.tapnotes.v2.model.EventType;
import com.ameron32.apps.tapnotes.v2.parse.Commands;
import com.ameron32.apps.tapnotes.v2.parse.Constants;
import com.ameron32.apps.tapnotes.v2.parse.Queries;
import com.ameron32.apps.tapnotes.v2.parse.object.Note;
import com.ameron32.apps.tapnotes.v2.parse.object.Program;
import com.ameron32.apps.tapnotes.v2.parse.object.Talk;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.eclipse.mat.parser.index.IndexManager;

import java.util.List;

/**
 * Created by klemeilleur on 7/7/2015.
 */
public class _MiscUtils {

  /**
   * ONE TIME USE APPLICATION OF PROGRAM OBJECT TO TALKS FOR 2015 CONVENTION
   */
  public static void put2015ConventionProgramObjectInto2015ConventionTalks() {

    ParseQuery.getQuery(Program.class)
        .getInBackground("BPCRNbT6Lf", new GetCallback<Program>() {

          @Override
          public void done(final Program program, ParseException e) {
            if (e != null) { return; }
            ParseQuery.getQuery(Talk.class)
                .findInBackground(new FindCallback<Talk>() {

                  @Override
                  public void done(List<Talk> list, ParseException e) {
                    if (e != null) { return; }
                    for (int i = 0; i < list.size(); i++) {
                      Talk t = list.get(i);
                      t.put("oProgram", program);
                      t.saveEventually();
                    }
                  }
                });
          }
        });
  }

  public static void _saveFakeNotes(List<Talk> talks, Program program) {

////  List<Note> notes = new ArrayList<>(talks.size());
////  List<Note> notes2 = new ArrayList<>();
//    for (int i = 0; i < talks.size(); i++) {
//      Talk talk = talks.get(i);
//      if (talk.getEventType() == EventType.TALK ||
//          talk.getEventType() == EventType.SYMPOSIUMTALK) {
//        Note last = null;
//        try {
//          last = Queries.Local.findLastClientOwnedNoteFor(talk);
//          Log.d("_MiscUtils", "last = " + last.toString());
//        } catch (ParseException e) {
//          e.printStackTrace();
//        }
//        Note note = Note.create("test3", program, talk, Commands.Local.getClientUser());
//        Commands.Local.saveEventuallyNote(note);
//        if (last != null) {
//          last.setNextNote(note);
//          Commands.Local.saveEventuallyNote(last);
////          notes2.add(last);
//        }
////        notes.add(note);
//      }
//    }
////  Commands.Live.saveNotesNow(notes);
////  Commands.Live.saveNotesNow(notes2);
  }

  public static void _generate1001Notes(Talk talk, Program program)
      throws ParseException {
    for (int i = 0; i < 1001; i++) {
      final Note note = Note.create("spam note " + (i+1), program, talk, Commands.Local.getClientUser());
      note.save();
    }
  }

  public static void _saveSongNotes(String programId) {

    try {

      final Program program = Queries.Local.getProgram(programId);
//      Talk talk = getTalk(5);
//
//      Note.create("1. What love Jehovah showed,", program, talk, null).save();
//      Note.create("What blessings from him flowed,", program, talk, null).save();
//      Note.create("When for all mankind he gave his dear Son.", program, talk, null).save();
//      Note.create("Christ then became our bread,", program, talk, null).save();
//      Note.create("That we might all be fed,", program, talk, null).save();
//      Note.create("And life eternal in peace might be won.", program, talk, null).save();
//      Note.create("2. Christ taught us ev�ry day", program, talk, null).save();
//      Note.create("For God�s great name to pray,", program, talk, null).save();
//      Note.create("The name Jehovah to be sanctified.", program, talk, null).save();
//      Note.create("Pray that his Kingdom come", program, talk, null).save();
//      Note.create("And that his will be done.", program, talk, null).save();
//      Note.create("Pray that he daily our bread will provide.", program, talk, null).save();
//      Note.create("3. God�s truth Christ Jesus taught", program, talk, null).save();
//      Note.create("And tender comfort brought", program, talk, null).save();
//      Note.create("To those who followed as his faithful sheep.", program, talk, null).save();
//      Note.create("May seeds of Kingdom praise", program, talk, null).save();
//      Note.create("Be sown through all our days.", program, talk, null).save();
//      Note.create("Then satisfaction and joy we will reap.", program, talk, null).save();



    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private static Talk getTalk(int number) throws ParseException {
    return ParseQuery.getQuery(Talk.class).whereEqualTo(Constants.TALK_METADATA_STRING_KEY, "#" + number).getFirst();
  }



  /*
  LINKED LIST STORED FOR REVIEW ONLY
   */

  static class _CrunchifyLinkedListTest {

    public static void main(String[] args) {
      _CrunchifyLinkedList lList = new _CrunchifyLinkedList();

      // add elements to LinkedList
      lList.add("1");
      lList.add("2");
      lList.add("3");
      lList.add("4");
      lList.add("5");

        /*
         * Please note that primitive values can not be added into LinkedList
         * directly. They must be converted to their corresponding wrapper
         * class.
         */

      System.out.println("lList - print linkedlist: " + lList);
      System.out.println("lList.size() - print linkedlist size: " + lList.size());
      System.out.println("lList.get(3) - get 3rd element: " + lList.get(3));
      System.out.println("lList.remove(2) - remove 2nd element: " + lList.remove(2));
      System.out.println("lList.get(3) - get 3rd element: " + lList.get(3));
      System.out.println("lList.size() - print linkedlist size: " + lList.size());
      System.out.println("lList - print linkedlist: " + lList);
    }
  }

  static class _CrunchifyLinkedList {
    // reference to the head node.
    private Node head;
    private int listCount;

    // LinkedList constructor
    public _CrunchifyLinkedList() {
      // this is an empty list, so the reference to the head node
      // is set to a new node with no data
      head = new Node(null);
      listCount = 0;
    }

    public void add(Object data)
    // appends the specified element to the end of this list.
    {
      Node crunchifyTemp = new Node(data);
      Node crunchifyCurrent = head;
      // starting at the head node, crawl to the end of the list
      while (crunchifyCurrent.getNext() != null) {
        crunchifyCurrent = crunchifyCurrent.getNext();
      }
      // the last node's "next" reference set to our new node
      crunchifyCurrent.setNext(crunchifyTemp);
      listCount++;// increment the number of elements variable
    }

    public void add(Object data, int index)
    // inserts the specified element at the specified position in this list
    {
      Node crunchifyTemp = new Node(data);
      Node crunchifyCurrent = head;
      // crawl to the requested index or the last element in the list,
      // whichever comes first
      for (int i = 1; i < index && crunchifyCurrent.getNext() != null; i++) {
        crunchifyCurrent = crunchifyCurrent.getNext();
      }
      // set the new node's next-node reference to this node's next-node
      // reference
      crunchifyTemp.setNext(crunchifyCurrent.getNext());
      // now set this node's next-node reference to the new node
      crunchifyCurrent.setNext(crunchifyTemp);
      listCount++;// increment the number of elements variable
    }

    public Object get(int index)
    // returns the element at the specified position in this list.
    {
      // index must be 1 or higher
      if (index <= 0)
        return null;

      Node crunchifyCurrent = head.getNext();
      for (int i = 1; i < index; i++) {
        if (crunchifyCurrent.getNext() == null)
          return null;

        crunchifyCurrent = crunchifyCurrent.getNext();
      }
      return crunchifyCurrent.getData();
    }

    public boolean remove(int index)
    // removes the element at the specified position in this list.
    {
      // if the index is out of range, exit
      if (index < 1 || index > size())
        return false;

      Node crunchifyCurrent = head;
      for (int i = 1; i < index; i++) {
        if (crunchifyCurrent.getNext() == null)
          return false;

        crunchifyCurrent = crunchifyCurrent.getNext();
      }
      crunchifyCurrent.setNext(crunchifyCurrent.getNext().getNext());
      listCount--; // decrement the number of elements variable
      return true;
    }

    public int size()
    // returns the number of elements in this list.
    {
      return listCount;
    }

    public String toString() {
      Node crunchifyCurrent = head.getNext();
      String output = "";
      while (crunchifyCurrent != null) {
        output += "[" + crunchifyCurrent.getData().toString() + "]";
        crunchifyCurrent = crunchifyCurrent.getNext();
      }
      return output;
    }

    private class Node {
      // reference to the next node in the chain,
      // or null if there isn't one.
      Node next;
      // data carried by this node.
      // could be of any type you need.
      Object data;

      // Node constructor
      public Node(Object dataValue) {
        next = null;
        data = dataValue;
      }

      // another Node constructor if we want to
      // specify the node to point to.
      public Node(Object dataValue, Node nextValue) {
        next = nextValue;
        data = dataValue;
      }

      // these methods should be self-explanatory
      public Object getData() {
        return data;
      }

      public void setData(Object dataValue) {
        data = dataValue;
      }

      public Node getNext() {
        return next;
      }

      public void setNext(Node nextValue) {
        next = nextValue;
      }
    }
  }
}
