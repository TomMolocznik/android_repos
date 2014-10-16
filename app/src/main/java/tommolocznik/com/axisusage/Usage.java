package tommolocznik.com.axisusage;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tommolocznik on 10/8/14.
 */
public class Usage {

    private static final String JSON_ID = "id";
    private static final String JSON_ITEM = "item";
    private static final String JSON_LOT = "lot";
    private static final String JSON_DATE = "date";
    private static final String JSON_ADDED = "add";
    private static final String JSON_PHOTO = "photo";

    private UUID mId;
    private String mItem;
    private String mLot;
    private Date mDate;
    private boolean mAdded;
    private Photo mPhoto;

    public Usage() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Usage(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mItem = json.getString(JSON_ITEM);
        mLot = json.getString(JSON_LOT);
        mAdded = json.getBoolean(JSON_ADDED);
        mDate = new Date(json.getLong(JSON_DATE));
        if (json.has(JSON_PHOTO))
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_ITEM, mItem);
        json.put(JSON_LOT,mLot);
        json.put(JSON_ADDED, mAdded);
        json.put(JSON_DATE, mDate.getTime());
        if (mPhoto != null)
        json.put(JSON_PHOTO, mPhoto.toJSON());
        return json;
    }

    @Override
    public String toString() {
        return mItem;
    }

    public String getItem() {
        return mItem;
    }

    public void setItem(String item) {
        mItem = item;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isAdded() {
        return mAdded;
    }

    public void setAdded(boolean added) {
        mAdded = added;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getLot() { return mLot;}

    public void setLot(String lot) { mLot = lot;}

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }


}
