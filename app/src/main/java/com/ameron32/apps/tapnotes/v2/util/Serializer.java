package com.ameron32.apps.tapnotes.v2.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by klemeilleur on 7/30/2015.
 */
public class Serializer<T extends Serializable> {

  final Class<T> type;

  public Serializer(final Class<T> type) {
    this.type = type;
  }

  public T load(final Context c, final String filename)
      throws IOException, ClassNotFoundException {
    final FileInputStream fis = c.openFileInput(filename);
    final ObjectInputStream ois = new ObjectInputStream(fis);
    final Object readObject = ois.readObject();
    if (type.isInstance(readObject)) {
      final T object = type.cast(readObject);
      return object;
    }
    return null;
  }

  public boolean save(final Context c, final String filename, final T object) throws IOException {
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    fos = c.openFileOutput(filename, Context.MODE_PRIVATE);
    oos = new ObjectOutputStream(fos);
    oos.writeObject(object);
    oos.close();
    return true;
  }
}
