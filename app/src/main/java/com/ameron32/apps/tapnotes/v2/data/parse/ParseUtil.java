package com.ameron32.apps.tapnotes.v2.data.parse;

import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klemeilleur on 3/25/2016.
 */
public class ParseUtil {

    static List<INote> toINote(List<Note> notes) {
        List<INote> iNotes = new ArrayList<>();
        iNotes.addAll(notes);
        return iNotes;
    }

    static List<Note> fromINote(List<INote> iNotes) {
        List<Note> notes = new ArrayList<>();
        for (INote iNote : iNotes) {
            if (iNote instanceof Note) {
                notes.add((Note) iNote);
            } else {
                throw new IllegalArgumentException("Cannot convert "+INote.class.getSimpleName()
                        +" to "+Note.class.getSimpleName()+". One or more "+INote.class.getSimpleName()
                        +" is not of type "+Note.class.getSimpleName()+".");
            }
        }
        return notes;
    }

    static List<ITalk> toITalk(List<Talk> talks) {
        List<ITalk> iTalks = new ArrayList<>();
        iTalks.addAll(talks);
        return iTalks;
    }

    static List<Talk> fromITalk(List<ITalk> iTalks) {
        List<Talk> talks = new ArrayList<>();
        for (ITalk iTalk : iTalks) {
            if (iTalk instanceof Note) {
                talks.add((Talk) iTalk);
            } else {
                throw new IllegalArgumentException("Cannot convert "+INote.class.getSimpleName()
                        +" to "+Note.class.getSimpleName()+". One or more "+INote.class.getSimpleName()
                        +" is not of type "+Note.class.getSimpleName()+".");
            }
        }
        return talks;
    }
}
