package com.ameron32.apps.tapnotes.v2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameron32.apps.tapnotes.v2.parse.adapter.AbsRecyclerQueryAdapter;
import com.ameron32.apps.tapnotes.v2.parse.frmk.ParseType;
import com.ameron32.apps.tapnotes.v2.parse.object._TestObject;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class _DummyTestAdapter extends AbsRecyclerQueryAdapter<_TestObject, _DummyTestAdapter.ViewHolder> {

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      loadObjects();
    }
  };

  public _DummyTestAdapter() {
    super(_TestObject.class, true);

  }

//  private OnItemClickListener mListener;
//
//  public void setItemClickListener(OnItemClickListener l) {
//    mListener = l;
//  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
    final View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.itemView.setOnClickListener(onClickListener);
    _TestObject to = getItem(position);
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < to.getColumnCount(); i++) {
      final String key = to.getKey(i);
      sb.append("key: " + key);
      sb.append("\n");
      final Object value = to.get(key);
      sb.append("value: " + value);
      sb.append("\n");
      final ParseType type = to.getType(key);
      if (type != null) {
        sb.append(displayAs(type, value));
      } else {
        sb.append("type was null");
      }
      sb.append("\n\n");
    }
    holder.textView.setText(sb.toString() + "**********");
//    holder.itemView.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        mListener.itemClicked(v, position);
//      }
//    });
  }

  private String displayAs(ParseType type, Object object) {
// TODO FIXME
    StringBuilder display = new StringBuilder();
    switch (type) {
      case ACL:
        final ParseACL acl = (ParseACL) object;
        boolean read = acl.getPublicReadAccess();
        boolean write = acl.getPublicWriteAccess();
        if (read && write) {
          display.append("ACL: Public[W]");
        } else if (read) {
          display.append("ACL: Public[R]");
        } else {
          display.append("ACL: Public[ ]");
        }
        break;
      case DATE:
        final Date date = (Date) object;
        break;
      case FILE:
        final ParseFile file = (ParseFile) object;
        break;
      case GEOPOINT:
        final ParseGeoPoint geoPoint = (ParseGeoPoint) object;
        break;
      case RELATION:
        final ParseRelation relation = (ParseRelation) object;
        break;
      case PARSEUSER:
        final ParseUser user = (ParseUser) object;
        break;
      case PARSEOBJECT:
        final ParseObject parseObject = (ParseObject) object;
        break;
      case ARRAY:
        final JSONArray array = (JSONArray) object;
        break;
      case MAP:
        final Map map = (Map) object;
        break;
      case LIST:
        final List list = (List) object;
        break;
      case STRING:
        final String string = (String) object;
        break;
      case NUMBER:
        final Number number = (Number) object;
        break;
      case BOOLEAN:
        final Boolean bool = (Boolean) object;
        break;
      case BYTES:
        final byte[] bytes = (byte[]) object;
        break;
      case JSONOBJECT:
        final JSONObject jsonObject = (JSONObject) object;
        break;
      default:

    }
    return display.toString();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @InjectView(android.R.id.text1)
    TextView textView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }
}
