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
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;

import org.eclipse.mat.parser.index.IndexManager;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

  public static void _saveProgramScriptures(Program program) {

  }

  public static void _saveSongNotes(String programId) {

    try {

      final Program program = Queries.Local.getProgram(programId);
      Talk talk;

//      talk = getTalk(120);
//      Note.create("1. If we have listened to Christ, will we show it?", program, talk, null).save();
//      Note.create("His teaching shines as it shows us the way.", program, talk, null).save();
//      Note.create("It makes us happy to hear and to know it,", program, talk, null).save();
//      Note.create("But we'll be blessed if we know and obey.", program, talk, null).save();
//      Note.create("(CHORUS)", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed", program, talk, null).save();
//      Note.create("When you hear God's will expressed.", program, talk, null).save();
//      Note.create("If you'd be happy and enter his rest,", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed.", program, talk, null).save();
//      Note.create("2. Our way of life, like a house, gives protection", program, talk, null).save();
//      Note.create("When it is built on the rock, not on sand.", program, talk, null).save();
//      Note.create("If we apply Jesus' loving direction,", program, talk, null).save();
//      Note.create("We'll build a life which on bedrock will stand.", program, talk, null).save();
//      Note.create("(CHORUS)", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed", program, talk, null).save();
//      Note.create("When you hear God's will expressed.", program, talk, null).save();
//      Note.create("If you'd be happy and enter his rest,", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed.", program, talk, null).save();
//      Note.create("3. Just as a tree rooted deep by the waters", program, talk, null).save();
//      Note.create("Gives of its fruit when each season arrives,", program, talk, null).save();
//      Note.create("If we obey as God's own sons and daughters,", program, talk, null).save();
//      Note.create("We'll all be blessed and enjoy endless lives.", program, talk, null).save();
//      Note.create("(CHORUS)", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed", program, talk, null).save();
//      Note.create("When you hear God's will expressed.", program, talk, null).save();
//      Note.create("If you'd be happy and enter his rest,", program, talk, null).save();
//      Note.create("Listen, obey, and be blessed.", program, talk, null).save();

//      ParseACL roleAdminACL = new ParseACL();
//      roleAdminACL.setPublicReadAccess(true);
//      roleAdminACL.setPublicWriteAccess(true);
//      ParseRole roleAdmin = new ParseRole("administrator", roleAdminACL);
//      roleAdmin.save();
//
//      final List<ParseRole> parseRoles = ParseRole.getQuery().whereEqualTo("name", "administrator").find();
//      ParseRole roleAdmin = parseRoles.get(0);
//      roleAdmin.getUsers().add(Commands.Local.getClientUser());
//      roleAdmin.save();

//      _loop(roleAdmin);

    } catch (ParseException e) {
      e.printStackTrace();
//    } catch (InterruptedException e) {
//      e.printStackTrace();
    }
  }

  private static void _loop(ParseRole roleAdmin) throws ParseException, InterruptedException {
    final List<Note> notes = ParseQuery.getQuery(Note.class)
        .whereDoesNotExist(Constants.NOTE_uOWNER_USER_KEY)
        .whereLessThan(Constants.NOTE_UPDATEDAT_DATE_KEY, DateTime.parse("20150727", DateTimeFormat.forPattern("yyyyMMdd")).toDate())
        .find();
    ParseACL acl = new ParseACL();
    acl.setPublicReadAccess(true);
    acl.setPublicWriteAccess(false);
    acl.setRoleWriteAccess(roleAdmin, true);
    int i = 0;
    String text;
    for (Note note : notes) {
      text = note.getString(Constants.NOTE_TEXT_STRING_KEY);
      Log.d("_updateNote", note.getId() + ": " + ((text.length() > 14) ? text.substring(0, 14) : text));
      note.setACL(acl);
      note.save();
      if (i % 100 == 0) {
        Thread.sleep(1000);
      } else if (i % 30 == 0) {
        Thread.sleep(100);
      }

      i++;
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
