package com.ameron32.apps.tapnotes.v2.data.parse;

import com.ameron32.apps.tapnotes.v2.data.frmk.Helper;
import com.ameron32.apps.tapnotes.v2.data.frmk.LocalHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.RemoteHelper;
import com.ameron32.apps.tapnotes.v2.data.frmk.UserHelper;
import com.ameron32.apps.tapnotes.v2.data.model.INote;
import com.ameron32.apps.tapnotes.v2.data.model.IObject;
import com.ameron32.apps.tapnotes.v2.data.model.IProgram;
import com.ameron32.apps.tapnotes.v2.data.model.ITalk;
import com.ameron32.apps.tapnotes.v2.data.model.IUser;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Note;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Program;
import com.ameron32.apps.tapnotes.v2.data.parse.model.Talk;
import com.ameron32.apps.tapnotes.v2.data.parse.model.User;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by klemeilleur on 4/18/16.
 */
public class ParseHelper {

  public final ParseLocalHelper cache;
  public final ParseRemoteHelper remote;
  public final ParseSyncEvent syncEvent;
  public final ParseUserHelper users;

  public ParseLocalHelper getCache() {
    return cache;
  }

  public ParseRemoteHelper getRemote() {
    return remote;
  }

  public ParseSyncEvent getSyncEvent() {
    return syncEvent;
  }

  public ParseUserHelper getUsers() {
    return users;
  }

  public ParseHelper() {
    cache = new ParseLocalHelper();
    remote = new ParseRemoteHelper();
    users = new ParseUserHelper();
    syncEvent = new ParseSyncEvent();
  }

  class ParseLocalHelper implements LocalHelper {

    @Override
    public Observable<INote> createNote(final INote note) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (note instanceof Note) {
                  ((Note) note).save();
                  subscriber.onNext(note);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<INote> updateNote(final INote note) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (note instanceof Note) {
                  ((Note) note).save();
                  subscriber.onNext(note);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<INote> deleteNote(final INote note) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (note instanceof Note) {
                  ((Note) note).delete();
                  subscriber.onNext(note);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<List<IProgram>> getPrograms() {
      return Observable.defer(new Func0<Observable<List<IProgram>>>() {
        @Override
        public Observable<List<IProgram>> call() {
          return Observable.create(new Observable.OnSubscribe<List<IProgram>>() {
            @Override
            public void call(Subscriber<? super List<IProgram>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseUtil.toIProgram(ParseQuery.getQuery(Program.class)
                    .fromLocalDatastore()
                    .find()));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<List<ITalk>> getTalks(final IProgram program) {
      return Observable.defer(new Func0<Observable<List<ITalk>>>() {
        @Override
        public Observable<List<ITalk>> call() {
          return Observable.create(new Observable.OnSubscribe<List<ITalk>>() {
            @Override
            public void call(Subscriber<? super List<ITalk>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseUtil.toITalk(ParseQuery.getQuery(Talk.class)
                    .fromLocalDatastore()
                    .whereEqualTo(Constants.TALK_oPROGRAM_OBJECT_KEY, program.getId())
                    .find()));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<List<INote>> getNotes(final IProgram program, final ITalk talk, final DateTime date, final IUser user) {
      return Observable.defer(new Func0<Observable<List<INote>>>() {
        @Override
        public Observable<List<INote>> call() {
          return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (program instanceof Program &&
                    talk instanceof Talk &&
                    user instanceof User) {
                  final ParseQuery<Note> noteParseQuery = ParseQuery.getQuery(Note.class)
                      .fromLocalDatastore();

                  if (program != Helper.ALL_PROGRAMS) {
                    noteParseQuery
                        .whereEqualTo(Constants.NOTE_oPROGRAM_OBJECT_KEY, program.getId());
                  }

                  if (talk != Helper.ALL_TALKS) {
                    noteParseQuery
                        .whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY, talk.getId());
                  }

                  if (date != Helper.FOREVER) {
                    noteParseQuery
                        .whereGreaterThanOrEqualTo(Constants.NOTE_UPDATEDAT_DATE_KEY, date);
                  }

                  if (user != null) {
                    if (user.getId() == Helper.USER_ME.getId()) {
                      noteParseQuery
                          .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY, user.getId());
                    } else
                    if (user.getId() == Helper.USER_GENERIC.getId()) {
                      noteParseQuery
                          .whereDoesNotExist(Constants.NOTE_uOWNER_USER_KEY);
                    } else {
                      // unknown, deliver Helper.USER_ALL
                    }
                  } else {
                    // user == Helper.USER_ALL or null
                  }

                  final List<Note> notes = noteParseQuery.find();
                  subscriber.onNext(ParseUtil.toINote(notes));
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<IProgram> getProgram(final String programId) {
      return Observable.defer(new Func0<Observable<IProgram>>() {
        @Override
        public Observable<IProgram> call() {
          return Observable.create(new Observable.OnSubscribe<IProgram>() {
            @Override
            public void call(Subscriber<? super IProgram> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseQuery.getQuery(Program.class)
                    .fromLocalDatastore()
                    .get(programId));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<ITalk> getTalk(final String talkId) {
      return Observable.defer(new Func0<Observable<ITalk>>() {
        @Override
        public Observable<ITalk> call() {
          return Observable.create(new Observable.OnSubscribe<ITalk>() {
            @Override
            public void call(Subscriber<? super ITalk> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseQuery.getQuery(Talk.class)
                    .fromLocalDatastore()
                    .get(talkId));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<ITalk> getTalkAtSequence(String sequencePosition) {
      throw new Error("do not use getTalkAtSequence()");
    }

    @Override
    public Observable<INote> getNote(final String noteId) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseQuery.getQuery(Note.class)
                    .fromLocalDatastore()
                    .get(noteId));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<List<IProgram>> pinPrograms(final List<IProgram> programs) {
      return Observable.defer(new Func0<Observable<List<IProgram>>>() {
        @Override
        public Observable<List<IProgram>> call() {
          return Observable.create(new Observable.OnSubscribe<List<IProgram>>() {
            @Override
            public void call(Subscriber<? super List<IProgram>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                Program.pinAll(ParseUtil.fromIProgram(programs));
                subscriber.onNext(programs);
                return;
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<List<ITalk>> pinTalks(final List<ITalk> talks) {
      return Observable.defer(new Func0<Observable<List<ITalk>>>() {
        @Override
        public Observable<List<ITalk>> call() {
          return Observable.create(new Observable.OnSubscribe<List<ITalk>>() {
            @Override
            public void call(Subscriber<? super List<ITalk>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                Talk.pinAll(ParseUtil.fromITalk(talks));
                subscriber.onNext(talks);
                return;
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<List<INote>> pinNotes(final List<INote> notes) {
      return Observable.defer(new Func0<Observable<List<INote>>>() {
        @Override
        public Observable<List<INote>> call() {
          return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                Note.pinAll(ParseUtil.fromINote(notes));
                subscriber.onNext(notes);
                return;
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<IProgram> pinProgram(final IProgram program) {
      return Observable.defer(new Func0<Observable<IProgram>>() {
        @Override
        public Observable<IProgram> call() {
          return Observable.create(new Observable.OnSubscribe<IProgram>() {
            @Override
            public void call(Subscriber<? super IProgram> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (program instanceof Program) {
                  ((Program) program).pin(Constants.PROGRAM_PIN_NAME);
                  subscriber.onNext(program);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<ITalk> pinTalk(final ITalk talk) {
      return Observable.defer(new Func0<Observable<ITalk>>() {
        @Override
        public Observable<ITalk> call() {
          return Observable.create(new Observable.OnSubscribe<ITalk>() {
            @Override
            public void call(Subscriber<? super ITalk> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (talk instanceof Talk) {
                  ((Talk) talk).pin(Constants.TALK_PIN_NAME);
                  subscriber.onNext(talk);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<INote> pinNote(final INote note) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (note instanceof Note) {
                  ((Note) note).pin(Constants.NOTE_PIN_NAME);
                  subscriber.onNext(note);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<List<IObject>> getUnsyncedObjects() {
      throw new Error("method not ready");
    }
  }

  class ParseRemoteHelper implements RemoteHelper {

    @Override
    public Observable<INote> createNote(final INote note) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (note instanceof Note) {
                  ((Note) note).save();
                  subscriber.onNext(note);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<INote> updateNote(final INote note) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (note instanceof Note) {
                  ((Note) note).save();
                  subscriber.onNext(note);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<INote> deleteNote(final INote note) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                if (note instanceof Note) {
                  ((Note) note).delete();
                  subscriber.onNext(note);
                  return;
                }
              } catch (ParseException e) {
                e.printStackTrace();
              }
              subscriber.onNext(null);
            }
          });
        }
      });
    }

    @Override
    public Observable<List<IObject>> getObjects() {
      throw new Error("method not ready");
    }

    @Override
    public Observable<List<IProgram>> getPrograms() {
      return Observable.defer(new Func0<Observable<List<IProgram>>>() {
        @Override
        public Observable<List<IProgram>> call() {
          return Observable.create(new Observable.OnSubscribe<List<IProgram>>() {
            @Override
            public void call(Subscriber<? super List<IProgram>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(
                    ParseUtil.toIProgram(ParseQuery.getQuery(Program.class)
                        .orderByAscending(Constants.PROGRAM_CREATEDAT_DATE_KEY)
                        .find()));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<List<ITalk>> getTalks(final IProgram program) {
      return Observable.defer(new Func0<Observable<List<ITalk>>>() {
        @Override
        public Observable<List<ITalk>> call() {
          return Observable.create(new Observable.OnSubscribe<List<ITalk>>() {
            @Override
            public void call(Subscriber<? super List<ITalk>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(
                    ParseUtil.toITalk(ParseQuery.getQuery(Talk.class)
                        .whereEqualTo(Constants.NOTE_oPROGRAM_OBJECT_KEY, program)
                        .orderByAscending(Constants.TALK_SEQUENCE_STRING_KEY)
                        .find()));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<List<INote>> getNotes(final IProgram program) {
      return Observable.defer(new Func0<Observable<List<INote>>>() {
        @Override
        public Observable<List<INote>> call() {
          return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(
                    ParseUtil.toINote(ParseQuery.getQuery(Note.class)
                        .whereEqualTo(Constants.NOTE_oPROGRAM_OBJECT_KEY, program)
                        .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY, ParseUser.getCurrentUser())
                        .orderByAscending(Constants.NOTE_CREATEDAT_DATE_KEY)
                        .find()));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<List<INote>> getNotes(final ITalk talk) {
      return Observable.defer(new Func0<Observable<List<INote>>>() {
        @Override
        public Observable<List<INote>> call() {
          return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(
                    ParseUtil.toINote(ParseQuery.getQuery(Note.class)
                        .whereEqualTo(Constants.NOTE_oTALK_OBJECT_KEY, talk)
                        .whereEqualTo(Constants.NOTE_uOWNER_USER_KEY, ParseUser.getCurrentUser())
                        .orderByAscending(Constants.NOTE_CREATEDAT_DATE_KEY)
                        .find()));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<IObject> getObject(final String objectId) {
      throw new Error("method not ready");
//            return Observable.defer(new Func0<Observable<IObject>>() {
//                @Override
//                public Observable<IObject> call() {
//                    return Observable.create(new Observable.OnSubscribe<IObject>() {
//                        @Override
//                        public void call(Subscriber<? super IObject> subscriber) {
//                            if (subscriber.isUnsubscribed()) return;
//                            try {
//                                subscriber.onNext(ParseQuery.getQuery().get(objectId));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                                subscriber.onNext(null);
//                            }
//                        }
//                    });
//                }
//            });
    }

    @Override
    public Observable<IProgram> getProgram(final String programId) {
      return Observable.defer(new Func0<Observable<IProgram>>() {
        @Override
        public Observable<IProgram> call() {
          return Observable.create(new Observable.OnSubscribe<IProgram>() {
            @Override
            public void call(Subscriber<? super IProgram> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseQuery.getQuery(Program.class).get(programId));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<ITalk> getTalk(final String talkId) {
      return Observable.defer(new Func0<Observable<ITalk>>() {
        @Override
        public Observable<ITalk> call() {
          return Observable.create(new Observable.OnSubscribe<ITalk>() {
            @Override
            public void call(Subscriber<? super ITalk> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseQuery.getQuery(Talk.class).get(talkId));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<ITalk> getTalkAtSequence(String sequence) {
      throw new Error("do not use getTalkAtSequence()");
    }

    @Override
    public Observable<INote> getNote(final String noteId) {
      return Observable.defer(new Func0<Observable<INote>>() {
        @Override
        public Observable<INote> call() {
          return Observable.create(new Observable.OnSubscribe<INote>() {
            @Override
            public void call(Subscriber<? super INote> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                subscriber.onNext(ParseQuery.getQuery(Note.class).get(noteId));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<List<INote>> saveNotes(final List<INote> iNotes) {
      return Observable.defer(new Func0<Observable<List<INote>>>() {
        @Override
        public Observable<List<INote>> call() {
          return Observable.create(new Observable.OnSubscribe<List<INote>>() {
            @Override
            public void call(Subscriber<? super List<INote>> subscriber) {
              if (subscriber.isUnsubscribed()) return;
              try {
                Note.saveAll(ParseUtil.fromINote(iNotes));
                subscriber.onNext(iNotes);
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }
  }

  static class ParseUserHelper implements UserHelper {

    @Override
    public Observable<IUser> login(final String username, final String password) {
      return Observable.defer(new Func0<Observable<IUser>>() {
        @Override
        public Observable<IUser> call() {
          return Observable.create(new Observable.OnSubscribe<IUser>() {
            @Override
            public void call(Subscriber<? super IUser> subscriber) {
              try {
                subscriber.onNext((User) ParseUser.logIn(username, password));
              } catch (ParseException e) {
                e.printStackTrace();
                subscriber.onNext(null);
              }
            }
          });
        }
      });
    }

    @Override
    public Observable<IUser> logout() {
      return getClientUser()
          .concatMap(new Func1<IUser, Observable<? extends IUser>>() {
        @Override
        public Observable<? extends IUser> call(final IUser user) {
          return Observable.create(new Observable.OnSubscribe<IUser>() {
            @Override
            public void call(Subscriber<? super IUser> subscriber) {
              ParseUser.logOut();
              subscriber.onNext(user);
            }
          });
        }
      });
    }

    @Override
    public Observable<IUser> getClientUser() {
      return Observable.defer(new Func0<Observable<IUser>>() {
        @Override
        public Observable<IUser> call() {
          return Observable.create(new Observable.OnSubscribe<IUser>() {
            @Override
            public void call(Subscriber<? super IUser> subscriber) {
              subscriber.onNext((User) ParseUser.getCurrentUser());
            }
          });
        }
      });
    }
  }
}
