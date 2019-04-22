package com.rfit.card.holder.digitalholder.adapter;

import java.io.ByteArrayInputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rfit.card.holder.digitalholder.HomeActivity;
import com.rfit.card.holder.digitalholder.MapsActivity;
import com.rfit.card.holder.digitalholder.appfrw.Convert;
import com.rfit.card.holder.digitalholder.appfrw.DataObject;
import com.rfit.card.holder.digitalholder.appfrw.NotifyObserver;
import com.rfit.card.holder.digitalholder.appfrw.ResponseObject;
import com.rfit.card.holder.digitalholder.appfrw.ViewHolder;
import com.rfit.card.holder.digitalholder.entity.ContactEntity;
import com.rfit.card.holder.digitalholder.model.ContactDbModel;
import com.rfit.card.holder.digitalholder.utility.Utill;
import com.rfit.card.holder.digitalholder.view.QuickActionItem;
import com.rfit.card.holder.digitalholder.view.QuickActionPopup;
import com.rfit.card.holder.digitalholder.view.RoundedImageView;
import com.sftech.business.cardholder.R;

public class ContactImageAdapter extends BaseAdapter {
    Context context;
    private List<DataObject> mDataList;
    int itemPos;

    private static final int ID_SMS = 1;
    private static final int ID_CALL = 2;
    private static final int ID_MAIL = 3;
    private static final int ID_MAP = 4;
    QuickActionPopup quickActionPopup;


    public ContactImageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DataObject> data) {
        this.mDataList = data;
    }

    public void removeItemById(int id) {
        for (int i = 0; i < mDataList.size(); i++) {
            ContactEntity entity = (ContactEntity) mDataList.get(i);
            if (id == Convert.toInt(entity.getKeyId())) {
                mDataList.remove(i);
                break;
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_row, null, false);

            holder = new ViewHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.txtAddress = (TextView) row.findViewById(R.id.txtAddress);
            holder.txtContact = (TextView) row.findViewById(R.id.txtContact);
            holder.txtEmail = (TextView) row.findViewById(R.id.txtEmail);
            holder.imgIcon = (RoundedImageView) row.findViewById(R.id.imgIcon);
            holder.iv_option = (ImageView) row.findViewById(R.id.iv_option);
            holder.txt_pos = (TextView) row.findViewById(R.id.txtPos);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ContactEntity entity = (ContactEntity) mDataList.get(position);
        String name = entity.getKeyName();
        String info = entity.getKeyContact();
        String address = entity.getKeyAddress();
        String email = entity.getKeyEmail();
        String idPos = "" + entity.getKeyId();
        holder.txt_pos.setText(idPos);
        holder.txtTitle.setText(name);

        holder.iv_option.setTag(entity.getPos());

        if (info == null || "".equals(info)) {
            holder.txtContact.setText(context.getString(R.string.STR_NO_NUMBER));
        } else {
            holder.txtContact.setText(info);
        }

        if (address == null || "".equals(address)) {
            holder.txtAddress.setText("");
        } else {
            holder.txtAddress.setText(address);
        }
        if (email == null || "".equals(email)) {
            holder.txtEmail.setText(context.getString(R.string.STR_NO_EMAIL));
        } else {
            holder.txtEmail.setText(email);
        }

        // convert byte to bitmap take from contact class

        byte[] outImage = (byte[]) entity.getKeyImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.imgIcon.setImageBitmap(theImage);

        prepareQuickActionItem();

        holder.iv_option.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                itemPos = (int) v.getTag();
                quickActionPopup.show(v);

            }
        });

        return row;
    }


    public synchronized void loadData(final NotifyObserver observer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<DataObject> list = ContactDbModel.getInstance(context).getContactList();
                    setData(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (observer != null) {
                    ResponseObject object = new ResponseObject();
                    object.setResponseCode(HomeActivity.CONTACT_ADD_UPDATE);
                    observer.update(object);
                }
            }
        }).start();
    }

    private void prepareQuickActionItem() {
        QuickActionItem smsItem = new QuickActionItem(ID_SMS, context.getString(R.string.STR_ACTION_ITEM_1));
        QuickActionItem callItem = new QuickActionItem(ID_CALL, context.getString(R.string.STR_ACTION_ITEM_2));
        QuickActionItem mailItem = new QuickActionItem(ID_MAIL, context.getString(R.string.STR_ACTION_ITEM_3));
        QuickActionItem mapItem = new QuickActionItem(ID_MAP, context.getString(R.string.STR_ACTION_ITEM_4));

        quickActionPopup = new QuickActionPopup(context, QuickActionPopup.VERTICAL);

        // add action items into QuickActionPopup
        quickActionPopup.addActionItem(smsItem);
        quickActionPopup.addActionItem(callItem);
        quickActionPopup.addActionItem(mailItem);
        quickActionPopup.addActionItem(mapItem);

        quickActionPopup.setOnActionItemClickListener(new QuickActionPopup.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickActionPopup source, int pos,
                                    int actionId) {

                // filtering items by id
                if (actionId == ID_SMS) {
                    sendSMS();
                } else if (actionId == ID_CALL) {
                    phoneCall();
                } else if (actionId == ID_MAIL) {
                    email();
                } else if (actionId == ID_MAP) {
                    showMap();
                }
            }
        });
    }


    public void sendSMS() {
        ContactEntity contact = (ContactEntity) mDataList.get(itemPos);
        if (contact.getKeyContact().equals("")) {
            Toast.makeText(context, context.getString(R.string.STR_NO_NUMBER), Toast.LENGTH_LONG).show();
            return;
        }
        Utill.sendSMS(context, contact.getKeyContact());
    }

    public void phoneCall() {
        ContactEntity contact = (ContactEntity) mDataList.get(itemPos);
        if (contact.getKeyContact().equals("")) {
            Toast.makeText(context, context.getString(R.string.STR_NO_NUMBER), Toast.LENGTH_LONG).show();
            return;
        }
        Utill.phoneCall(context, contact.getKeyContact());
    }

    private void email() {
        ContactEntity contact = (ContactEntity) mDataList.get(itemPos);
        if (contact.getKeyEmail().equals("")) {
            Toast.makeText(context, context.getString(R.string.STR_NO_EMAIL), Toast.LENGTH_LONG).show();
            return;
        }
        Utill.email(context, contact.getKeyEmail());

    }

    private void showMap() {
        ContactEntity contact = (ContactEntity) mDataList.get(itemPos);
        Intent it = new Intent(context, MapsActivity.class);
        it.putExtra(MapsActivity.KEY_ADDRESS, contact.getKeyAddress());
        context.startActivity(it);

    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


}