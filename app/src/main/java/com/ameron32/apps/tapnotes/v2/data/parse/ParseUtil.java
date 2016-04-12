package com.ameron32.apps.tapnotes.v2.data.parse;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klemeilleur on 3/25/2016.
 */
public class ParseUtil {

    public static List<INote> toINote(List<Note> notes) {
        List<INote> iNotes = new ArrayList<>();
        iNotes.addAll(notes);
        return iNotes;
    }

    public static List<Note> fromINote(List<INote> iNotes) {
        List<Note> notes = new ArrayList<>();
        if (iNotes != null) {
            for (INote iNote : iNotes) {
                if (iNote instanceof Note) {
                    notes.add((Note) iNote);
                } else {
                    throw new IllegalArgumentException("Cannot convert " + INote.class.getSimpleName()
                            + " to " + Note.class.getSimpleName() + ". One or more " + INote.class.getSimpleName()
                            + " is not of type " + Note.class.getSimpleName() + ".");
                }
            }
        }
        return notes;
    }

    public static List<ITalk> toITalk(List<Talk> talks) {
        List<ITalk> iTalks = new ArrayList<>();
        iTalks.addAll(talks);
        return iTalks;
    }

    public static List<Talk> fromITalk(List<ITalk> iTalks) {
        List<Talk> talks = new ArrayList<>();
        if (iTalks != null) {
            for (ITalk iTalk : iTalks) {
                if (iTalk instanceof Note) {
                    talks.add((Talk) iTalk);
                } else {
                    throw new IllegalArgumentException("Cannot convert " + INote.class.getSimpleName()
                            + " to " + Note.class.getSimpleName() + ". One or more " + INote.class.getSimpleName()
                            + " is not of type " + Note.class.getSimpleName() + ".");
                }
            }
        }
        return talks;
    }

    public static List<IProgram> toIProgram(List<Program> programs) {
        List<IProgram> iPrograms = new ArrayList<>();
        iPrograms.addAll(programs);
        return iPrograms;
    }

    public static List<Program> fromIProgram(List<IProgram> iPrograms) {
        List<Program> talks = new ArrayList<>();
        if (iPrograms != null) {
            for (IProgram iProgram : iPrograms) {
                if (iProgram instanceof Program) {
                    talks.add((Program) iProgram);
                } else {
                    throw new IllegalArgumentException("Cannot convert " + IProgram.class.getSimpleName()
                            + " to " + Program.class.getSimpleName() + ". One or more " + IProgram.class.getSimpleName()
                            + " is not of type " + Program.class.getSimpleName() + ".");
                }
            }
        }
        return talks;
    }
}
