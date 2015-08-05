package com.ameron32.apps.tapnotes.v2.ui.mc_notes;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ameron32.apps.tapnotes.v2.R;
import com.ameron32.apps.tapnotes.v2.model.IBible;
import com.ameron32.apps.tapnotes.v2.model.INote;
import com.ameron32.apps.tapnotes.v2.scripture.Bible;
import com.ameron32.apps.tapnotes.v2.ui.delegate.INotesDelegate;
import com.ameron32.apps.tapnotes.v2.ui.renderer.ScriptureSpanRenderer;
import com.jmpergar.awesometext.AwesomeTextHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;


/**
 * Created by Micah on 7/4/2015.
 */
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NoteViewHolder> implements INotesDelegate {

    private static final String TAG = "NotesRecyclerAdapter";
//    public static final int offsetToStartDrag = 100;
    private final Context mContext;
//    int lastX;
//    int lastY;
    Bible bible;
    private HashMap<INote, String> appendedScriptures;

    private INotesDelegateCallbacks mCallback;

    private NoteDataProvider mProvider;

    private ScriptureAppender appender;
    AwesomeTextHandler noteTextHandler;

    public void setSelected(int position){
        mProvider.setSelected(position);
    }

    public int getPositionOfSelectedItem(){
        return mProvider.getPositionOfSelectedItem();
    }


    @Override
    public void onBibleCreated(IBible bible) {
        if (bible instanceof Bible){
            Bible mBible = (Bible)bible;
            this.bible = mBible;
            appender = new ScriptureAppender(mBible, mContext);
        }
    }

    public NotesRecyclerAdapter(Context context) {
        this.mContext = context;
        mProvider = new NoteDataProvider();
        setHasStableIds(true);
        appendedScriptures = new HashMap<>(); // TODO MICAH or KRIS consider switch to ArrayMap for <1000 items

    }

    public void addINotesDelegateCallbacks(INotesDelegateCallbacks callback) {
        mCallback = callback;
    }

    public INotesDelegateCallbacks getmCallback(){return mCallback;}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.insert_notes_list_item, parent, false);
//        setOnClickListener(v);
//        setonTouch(v);
        NoteViewHolder holder = new NoteViewHolder(v);
//        setLongPressListener(v, holder.popup);
        return holder;
    }

    private static final String SCRIPTURE_PATTERN = "@\\<\\<!\\<[0-9| ]+\\<[\\w|\\,|:|\\-|\\s]+\\>!\\>\\>";


    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        INote note = mProvider.getItem(position);

        if (mProvider.getPositionOfSelectedItem() == position){
            holder.noteLayout.setSelected(true);
        }else{
            holder.noteLayout.setSelected(false);
        }

        //holder.notesTextView.setText(note.getNoteText());
        holder.noteLayout.setTag(R.string.holdertag, holder);
        holder.noteLayout.setTag(R.string.notetag, note);
        setOnClickListener(holder.noteLayout);
        setLongPressListener(holder.noteLayout, holder.popup);

        if (noteTextHandler == null)
            noteTextHandler = new AwesomeTextHandler();

        noteTextHandler
            .addViewSpanRenderer(SCRIPTURE_PATTERN, new ScriptureSpanRenderer())

            .setView(holder.notesTextView);
        noteTextHandler.setText(note.getNoteText());
        if (note.isBoldNote()){
            holder.notesTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            holder.notesTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        if (note.isImportantNote()){
            // TODO isImportantNote
        }else{
            // TODO IS NOT ImportantNote
        }
        String appendedText = appendedScriptures.get(note);
        if (appendedText != null) {
            appendedText = appendedText.trim();
            if (!appendedText.isEmpty() && appendedText.length() > 0) {
                holder.appendTextView.setVisibility(View.VISIBLE);
                holder.appendTextView.setText(Html.fromHtml(appendedText), isScriptureHidden, position);
            } else {
                holder.appendTextView.setVisibility(View.GONE);
            }
        }
    }

    private final SparseBooleanArray isScriptureHidden = new SparseBooleanArray();

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }


    //INotesDelegate methods:
    @Override
    public void synchronizeNotes(List<INote> allNotes) {
        LinkedList<INote> ll = new LinkedList<INote>(allNotes);
        mProvider.populateWithExistingNotes(ll);
        for (INote note : allNotes) {
            if (appender != null) {
                appendedScriptures.put(note, appender.appendScriptures(note));
            }
        }
        notifyDataSetChanged();

    }

    @Override
    public void addNotes(List<INote> notesToAdd) {
        for (INote note : notesToAdd) {
            mProvider.addNote(note);
            if (appender != null) {
                appendedScriptures.put(note, appender.appendScriptures(note));
            }
        }
        notifyDataSetChanged();


    }

    @Override
    public void removeNotes(List<INote> notesToRemove) {

        for (INote note : notesToRemove) {
            mProvider.removeItem(note);
            if (appender != null) {
                appendedScriptures.remove(note);
            }
            notifyDataSetChanged();
        }

    }


    @Override
    public void replaceNotes(List<INote> notesToReplace) {
        mProvider.replaceNotes(notesToReplace);
        if (appender!=null){
            for(INote note:notesToReplace){
                if (appendedScriptures.containsKey(note)){
                    appendedScriptures.remove(note);
                    appendedScriptures.put(note, appender.appendScriptures(note));
                }
            }
        }
        notifyDataSetChanged();
    }

    private void setOnClickListener(View v) {
        // TODO discuss and adjust click listener to resolve click issues WHILE retaining desired effect
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getParent() instanceof NotesRecycler) {
                    NotesRecycler nr = (NotesRecycler) v.getParent();
                    nr.itemClicked(null);
                }
//                mCallback.onUserClickEditNote(((INote) v.getTag(R.string.notetag)));
            }
        });
    }

    private void setLongPressListener(View v, View popup) {
        v.setOnLongClickListener(new View.OnLongClickListener() {


//            @InjectView(R.id.repos_button)
//            ImageView reposButton;
//            @InjectView(R.id.bold_button)
//            ImageView boldButton;
//            @InjectView(R.id.important_button)
//            ImageView importantButton;
//            @InjectView(R.id.edit_button)
//            ImageView editButton;
//            @InjectView(R.id.delete_button)
//            ImageView deleteButton;


            @Override
            public boolean onLongClick(View v) {

                if (v.getParent() instanceof NotesRecycler) {
                    NotesRecycler nr = ((NotesRecycler) v.getParent());
                    nr.itemClicked(v);
                    popup.setVisibility(View.VISIBLE);
                    return true;
                }

                return false;
            }
        });
    }

//    private void setonTouch(View v) {
//        v.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent ev) {
//                final int action = ev.getAction();
//                switch (action & MotionEvent.ACTION_MASK) {
//                    case MotionEvent.ACTION_DOWN: {
//                        int y = 0;
//                        if (v.getParent() instanceof NotesRecycler) {
//                            NotesRecycler nr = (NotesRecycler) v.getParent();
//                            y = nr.indexOfChild(v) * (int) (mContext.getResources().getDimension(R.dimen.note_row_height_min));
//                        }
//                        lastY = y - 200;
//                        break;
//                    }
//                }
//                return false;
//            }
//        });
//    }


}
