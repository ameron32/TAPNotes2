package com.ameron32.apps.tapnotes.v2.data.backendless.model;

import com.ameron32.apps.tapnotes.v2.data.model.IObject;

import java.util.Date;

/**
 * Created by klemeilleur on 4/3/16.
 */
public class BObject implements IObject {

    private String objectId;

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
    }

    @Override
    public String getId() {
        return getObjectId();
    }

    private Date created;
    private Date updated;
    private Date userTimestamp;

    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated( Date updated )
    {
        this.updated = updated;
    }

    @Override
    public void setUserTimestamp(Date date) {
        this.userTimestamp = date;
    }
}
