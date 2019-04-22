/**
 * @author Faruk hossain
 * ContactEntity
 */
package com.rfit.card.holder.digitalholder.entity;

import android.database.Cursor;

import com.rfit.card.holder.digitalholder.db.BaseEntity;

// TABLE ROW/ENTITY
public class ContactEntity extends BaseEntity {

    /**
     * Return the create table script
     *
     * @return
     */
    public static String getCreateTable() {

        String sqltblCreate = "CREATE TABLE " + Table.TABLE_CONTACTS + "(" + Table.KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + Table.KEY_NAME + " TEXT," + Table.KEY_CONTACT + " TEXT,"
                + Table.KEY_COMPANY + " TEXT," + Table.KEY_ADDRESS + " TEXT," + Table.KEY_EMAIL + " TEXT,"
                + Table.KEY_IMAGE + " BLOB," + Table.KEY_IMAGETWO + " BLOB" + ");";

        return sqltblCreate;
    }

    /**
     * Return the drop table script
     *
     * @return
     */
    public static String getDropTable() {

        String sqltblCreate = "DROP TABLE IF EXISTS " + Table.TABLE_CONTACTS;

        return sqltblCreate;
    }

    public ContactEntity(long id) {
        super(id);
    }

    // Add the following lines getCreateTable() in -> Tables.createTableScript()
    // to ensure that table is created.
    public static class Table {
        // All Static variables
        // Contacts table name
        public static final String TABLE_CONTACTS = "contacts";


        // Contacts Table Columns names
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_COMPANY = "company";
        public static final String KEY_CONTACT = "contact";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_ADDRESS = "address";
        public static final String KEY_IMAGE = "image_1";
        public static final String KEY_IMAGETWO = "image_2";
        public static final String KEY_POS = "KEY_POS";
    }

    //
    public ContactEntity(String contactId, String name, String company, String number, String email, String address, byte[] personImg, byte[] cardImg) {
        super(-1);
        this.setKeyId(contactId);
        this.setKeyName(name);
        this.setKeyCompany(company);
        this.setKeyContact(number);
        this.setKeyEmai(email);
        this.setKeyAddress(address);
        this.settKeyImage(personImg);
        this.settKeyImageTwo(cardImg);
    }


    public ContactEntity(String name, String company, String number, String email, String address, byte[] personImg, byte[] cardImg) {
        super(-1);
        this.setKeyName(name);
        this.setKeyCompany(company);
        this.setKeyContact(number);
        this.setKeyEmai(email);
        this.setKeyAddress(address);
        this.settKeyImage(personImg);
        this.settKeyImageTwo(cardImg);
    }

    public  String getKeyId() {
        return this.getString(Table.KEY_ID);
    }

    public void setKeyId(String value) {
        this.setValue(Table.KEY_ID, value);
    }

    public String getKeyName() {
        return this.getString(Table.KEY_NAME);
    }

    public void setKeyName(String value) {
        this.setValue(Table.KEY_NAME, value);
    }

    public String getKeyCompany() {
        return this.getString(Table.KEY_COMPANY);
    }

    public void setKeyCompany(String value) {
        this.setValue(Table.KEY_COMPANY, value);
    }

    public  String getKeyContact() {
        return this.getString(Table.KEY_CONTACT);
    }

    public void setKeyContact(String value) {
        this.setValue(Table.KEY_CONTACT, value);
    }

    public String getKeyEmail() {
        return this.getString(Table.KEY_EMAIL);
    }

    public void setKeyEmai(String value) {
        this.setValue(Table.KEY_EMAIL, value);
    }

    public String getKeyAddress() {
        return this.getString(Table.KEY_ADDRESS);
    }

    public void setKeyAddress(String value) {
        this.setValue(Table.KEY_ADDRESS, value);
    }

    public Object getKeyImage() {
        return this.getValue(Table.KEY_IMAGE);
    }

    public void settKeyImage(byte[] value) {
        this.setValue(Table.KEY_IMAGE, value);
    }

    public Object getKeyImagetwo() {
        return this.getValue(Table.KEY_IMAGETWO);
    }

    public void settKeyImageTwo(byte[] value) {
        this.setValue(Table.KEY_IMAGETWO, value);
    }

    public int getPos() {
        return this.getInt(Table.KEY_POS);
    }

    public void setPos(int value) {
        this.setValue(Table.KEY_POS, value);
    }

    public ContactEntity(long id, String value) {
        super(id);
    }

}



