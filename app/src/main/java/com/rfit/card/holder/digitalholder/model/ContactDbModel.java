/**
 * @author Faruk Hossain
 * Setting BookmarkDbModel
 */
package com.rfit.card.holder.digitalholder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.rfit.card.holder.digitalholder.appfrw.DataObject;
import com.rfit.card.holder.digitalholder.appfrw.ResponseObject;
import com.rfit.card.holder.digitalholder.db.BaseDbModel;
import com.rfit.card.holder.digitalholder.db.BaseEntity;
import com.rfit.card.holder.digitalholder.db.UtilsCursor;
import com.rfit.card.holder.digitalholder.entity.ContactEntity;

import java.util.ArrayList;


//Help from : http://www.tutorialspoint.com/android/android_sqlite_database.htm
public class ContactDbModel extends BaseDbModel {

    static private ContactDbModel mContactDbModel;

    public static ContactDbModel getInstance(Context context) {
        if (mContactDbModel == null) {
            mContactDbModel = new ContactDbModel(context);
        }
        mContactDbModel.mContext = context;
        return mContactDbModel;
    }

    @Override
    public ResponseObject execute() {
        return null;
    }

    /**
     * Constructor function
     *
     * @param context
     */
    public ContactDbModel(Context context) {
        super(context);
    }

    /**
     * Update an entity into database
     */
    @Override
    public int update(BaseEntity entity) {
        ContactEntity item = (ContactEntity) entity;

        ContentValues values = new ContentValues();

        values.put(ContactEntity.Table.KEY_NAME, item.getKeyName());
        values.put(ContactEntity.Table.KEY_CONTACT, item.getKeyContact());
        values.put(ContactEntity.Table.KEY_ADDRESS, item.getKeyAddress());
        values.put(ContactEntity.Table.KEY_COMPANY, item.getKeyCompany());
        values.put(ContactEntity.Table.KEY_EMAIL, item.getKeyEmail());
        values.put(ContactEntity.Table.KEY_IMAGE, (byte[])item.getKeyImage());
        values.put(ContactEntity.Table.KEY_IMAGETWO, (byte[])item.getKeyImagetwo());

        String where = ContactEntity.Table.KEY_ID + "=?";
        String[] args = new String[]{item.getKeyId()};

        // TODO FIXME we have to code...
        int row = mDatabaseController.update(ContactEntity.Table.TABLE_CONTACTS, values, where, args);

        return row;
    }

    /**
     * Retrieve all information from database
     */
    public void query() {
        Cursor c = mDatabaseController.query(ContactEntity.Table.TABLE_CONTACTS, null, null, null, null, null, null);
        this.loadData(c);
    }

    public void rawQuery(String query) {
        Cursor c = mDatabaseController.rawQuery(query, null);
        this.loadData(c);
    }

    public void delete(String id) {
        mDatabaseController.delete(ContactEntity.Table.TABLE_CONTACTS, ContactEntity.Table.KEY_ID + "=?", new String[]{id + ""});
    }

    /**
     * Load all information from database
     */
    @Override
    protected void loadData(Cursor cursor) {
        // Remove the old data
        this.clear(false);
        // TODO Auto-generated method stub
        if (cursor == null) return;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            int i = 0;

            // Remove the old data
            this.clear(false);

            do {
                ContactEntity entity = new ContactEntity(0, "EntitySetting");
                //contact.setID(Integer.parseInt(cursor.getString(0)));
                //LIKE ANY ONE ADD ITEMS..
                entity.setKeyId(UtilsCursor.getStringFromCursor(ContactEntity.Table.KEY_ID, cursor));
                entity.setKeyName(UtilsCursor.getStringFromCursor(ContactEntity.Table.KEY_NAME, cursor));
                entity.setKeyContact(UtilsCursor.getStringFromCursor(ContactEntity.Table.KEY_CONTACT, cursor));
                entity.setKeyAddress(UtilsCursor.getStringFromCursor(ContactEntity.Table.KEY_ADDRESS, cursor));
                entity.setKeyCompany(UtilsCursor.getStringFromCursor(ContactEntity.Table.KEY_COMPANY, cursor));
                entity.setKeyEmai(UtilsCursor.getStringFromCursor(ContactEntity.Table.KEY_EMAIL, cursor));
                entity.settKeyImage(UtilsCursor.getBlobFromCursor(ContactEntity.Table.KEY_IMAGE, cursor));
                entity.settKeyImageTwo(UtilsCursor.getBlobFromCursor(ContactEntity.Table.KEY_IMAGETWO, cursor));
                entity.setPos(i);

                this.add(entity);
                i++;

            } while (cursor.moveToNext());
        }

        // Must close this cursor
        if (!cursor.isClosed()) {
            cursor.close();
        }
    }


    @Override
    public long insert(BaseEntity entity) {
        ContactEntity item = (ContactEntity) entity;

        ContentValues values = new ContentValues();
        //values.put(ContactEntity.Table.KEY_ID, item.getKeyId());
        values.put(ContactEntity.Table.KEY_NAME, item.getKeyName());
        values.put(ContactEntity.Table.KEY_CONTACT, item.getKeyContact());
        values.put(ContactEntity.Table.KEY_ADDRESS, item.getKeyAddress());
        values.put(ContactEntity.Table.KEY_COMPANY, item.getKeyCompany());
        values.put(ContactEntity.Table.KEY_EMAIL, item.getKeyEmail());
        values.put(ContactEntity.Table.KEY_IMAGE, (byte[]) item.getKeyImage());
        values.put(ContactEntity.Table.KEY_IMAGETWO, (byte[])item.getKeyImagetwo());

        long row = mDatabaseController.insert(ContactEntity.Table.TABLE_CONTACTS, null, values);

        return row;
    }

    public ArrayList<DataObject> getContactList() {

        String query = "SELECT * FROM " +
                ContactEntity.Table.TABLE_CONTACTS +
                " ORDER BY " +
                ContactEntity.Table.KEY_NAME;

        this.rawQuery(query);

        return this.mListItem;
    }

    public ContactEntity hasContact(String contactId) {

        String query = "SELECT * FROM " +
                ContactEntity.Table.TABLE_CONTACTS +
                " WHERE " +
                ContactEntity.Table.KEY_ID +
                "=" + "'" +
                contactId;

        this.rawQuery(query);

        for (int i = 0; i < this.getCount(); i++) {
            ContactEntity object = (ContactEntity) this.getItem(i);
            String id = object.getKeyId();
            int r1 = id.compareToIgnoreCase(contactId);
            if (r1 == 0) {
                return object;
            }
        }
        return null;
    }

    // Getting single contact
    public ContactEntity getContactById(int contactId) {
        String query = "SELECT * FROM " +
                ContactEntity.Table.TABLE_CONTACTS +
                " WHERE " +
                ContactEntity.Table.KEY_ID +
                "=" + "'" +
                contactId +
                "'";

        this.rawQuery(query);

        if (this.getCount() > 0) {
            ContactEntity object = (ContactEntity) this.getItem(0);
            return object;

        }

        return null;
    }
}

