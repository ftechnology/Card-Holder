package com.rfit.card.holder.digitalholder.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // private variables
    int _id;
    public String _name;
    public String _address;
    public String _company;
    public String _contact;
    public String _email;
    public byte[] _imagePerson;
    public byte[] _imageCard;

    protected Contact(Parcel in) {
        _id = in.readInt();
        _name = in.readString();
        _address = in.readString();
        _company = in.readString();
        _contact = in.readString();
        _email = in.readString();
        _imagePerson = in.createByteArray();
        _imageCard = in.createByteArray();
        pos = in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int pos;


    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_contact() {
        return _contact;
    }

    public void set_contact(String _contact) {
        this._contact = _contact;
    }

    public String get_company() {
        return _company;
    }

    public void set_company(String _company) {
        this._company = _company;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    // Empty constructor
    public Contact() {

    }

    // constructor
    public Contact(int keyId, String name, String contact, String address, String company, String email, byte[] image, byte[] imagetwo) {
        this._id = keyId;
        this._name = name;
        this._imagePerson = image;
        this._imageCard = imagetwo;
        this._address = address;
        this._company = company;
        this._contact = contact;
        this._email = email;


    }


    public byte[] get_imageTwo() {
        return _imageCard;
    }

    public void set_imageTwo(byte[] _imageTwo) {
        this._imageCard = _imageTwo;
    }

    // constructor
    public Contact(String name, String address, String contact, String company, String email, byte[] image, byte[] imagetwo) {
        this._name = name;
        this._imagePerson = image;
        this._imageCard = imagetwo;
        this._address = address;
        this._company = company;
        this._contact = contact;
        this._email = email;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int keyId) {
        this._id = keyId;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting phone number
    public byte[] getImage() {
        return this._imagePerson;
    }

    // setting phone number
    public void setImage(byte[] image) {
        this._imagePerson = image;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        // TODO Auto-generated method stub

        arg0.writeInt(_id);
        arg0.writeString(_name);
        arg0.writeString(_address);
        arg0.writeString(_company);
        arg0.writeString(_contact);
        arg0.writeString(_email);
        arg0.writeByteArray(_imagePerson);
        arg0.writeByteArray(_imageCard);
        arg0.writeInt(pos);
    }
}
