package com.ameron32.apps.tapnotes.v2.parse.frmk;

import com.parse.ParseACL;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by klemeilleur on 6/22/2015.
 */
public enum ParseType {

  STRING {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof String) {
        return true;
      }
      return false;
    }

    @Override
    public String getAs(Object value) {
      if (isObject(value)) {
        return (String) value;
      }
      return null;
    }
  },

  NUMBER {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof Number) {
        return true;
      }
      return false;
    }

    @Override
    public Number getAs(Object value) {
      if (isObject(value)) {
        return (Number) value;
      }
      return null;
    }
  },

  BOOLEAN {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof Boolean) {
        return true;
      }
      return false;
    }

    @Override
    public Boolean getAs(Object value) {
      if (isObject(value)) {
        return (Boolean) value;
      }
      return null;
    }
  },

  DATE {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof Date) {
        return true;
      }
      return false;
    }

    @Override
    public Date getAs(Object value) {
      if (isObject(value)) {
        return (Date) value;
      }
      return null;
    }

  },

  FILE {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof ParseFile) {
        return true;
      }
      return false;
    }

    @Override
    public ParseFile getAs(Object value) {
      if (isObject(value)) {
        return (ParseFile) value;
      }
      return null;
    }
  },

  GEOPOINT {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof ParseGeoPoint) {
        return true;
      }
      return false;
    }

    @Override
    public ParseGeoPoint getAs(Object value) {
      if (isObject(value)) {
        return (ParseGeoPoint) value;
      }
      return null;
    }
  },

  ARRAY {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof JSONArray) {
        return true;
      }
      return false;
    }

    @Override
    public JSONArray getAs(Object value) {
      if (isObject(value)) {
        return (JSONArray) value;
      }
      return null;
    }
  },

  MAP {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof Map) {
        return true;
      }
      return false;
    }

    @Override
    public Map getAs(Object value) {
      if (isObject(value)) {
        return (Map) value;
      }
      return null;
    }
  },

  LIST {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof List) {
        return true;
      }
      return false;
    }

    @Override
    public List getAs(Object value) {
      if (isObject(value)) {
        return (List) value;
      }
      return null;
    }

  },

  PARSEOBJECT {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof ParseObject) {
        return true;
      }
      return false;
    }

    @Override
    public ParseObject getAs(Object value) {
      if (isObject(value)) {
        return (ParseObject) value;
      }
      return null;
    }
  },

  PARSEUSER {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof ParseUser) {
        return true;
      }
      return false;
    }

    @Override
    public ParseUser getAs(Object value) {
      if (isObject(value)) {
        return (ParseUser) value;
      }
      return null;
    }
  },

  RELATION {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof ParseRelation) {
        return true;
      }
      return false;
    }

    @Override
    public ParseRelation getAs(Object value) {
      if (isObject(value)) {
        return (ParseRelation) value;
      }
      return null;
    }
  },

  JSONOBJECT {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof JSONObject) {
        return true;
      }
      return false;
    }

    @Override
    public JSONObject getAs(Object value) {
      if (isObject(value)) {
        return (JSONObject) value;
      }
      return null;
    }
  },

  BYTES {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof byte[]) {
        return true;
      }
      return false;
    }

    @Override
    public byte[] getAs(Object value) {
      if (isObject(value)) {
        return (byte[]) value;
      }
      return null;
    }
  },

  ACL {
    public boolean isObject(Object toCompare) {
      if (toCompare instanceof ParseACL) {
        return true;
      }
      return false;
    }

    @Override
    public ParseACL getAs(Object value) {
      if (isObject(value)) {
        return (ParseACL) value;
      }
      return null;
    }
  };

  public abstract boolean isObject(Object value);

  public abstract Object getAs(Object value);

  public static ParseType whichType(Object value) {
    for (int i = 0; i < ParseType.values().length; i++) {
      ParseType type = ParseType.values()[i];
      if (type.isObject(value)) {
        return type;
      }
    }
    return null;
  }
}
