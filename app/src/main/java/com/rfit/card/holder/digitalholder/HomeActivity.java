package com.rfit.card.holder.digitalholder;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.rfit.card.holder.digitalholder.adapter.ContactImageAdapter;
import com.rfit.card.holder.digitalholder.appfrw.BaseActivity;
import com.rfit.card.holder.digitalholder.appfrw.Convert;
import com.rfit.card.holder.digitalholder.appfrw.DataObject;
import com.rfit.card.holder.digitalholder.appfrw.NotifyObserver;
import com.rfit.card.holder.digitalholder.appfrw.ResponseObject;
import com.rfit.card.holder.digitalholder.entity.ContactEntity;
import com.rfit.card.holder.digitalholder.model.ContactDbModel;
import com.rfit.card.holder.digitalholder.utility.MaterialDialog;
import com.sftech.business.cardholder.R;

public class HomeActivity extends BaseActivity implements OnItemClickListener, AdapterView.OnItemLongClickListener {
    List<DataObject> contactList = new ArrayList<DataObject>();
    ContactImageAdapter mContactAdapter;
    ListView listView;

    private MaterialDialog dialog;

    public static final int CONTACT_ADD_UPDATE = 1;
    private String[] permissions;
    private int pCode = 12321;
    int mDelId = -1;
    public static final String KEY_CONTACT_OBJECT = "contactObject";


    @Override
    protected void createView(Bundle savedInstanceState) {
        checkPermissions();
        setContentView(R.layout.list);
        initWidget();
        setUpToolbar();
    }


    @Override
    protected void createAdapter() {
        mContactAdapter = new ContactImageAdapter(this);
        listView.setAdapter(mContactAdapter);

    }

    @Override
    protected void loadData() {
        mContactAdapter.loadData(new NotifyObserver() {
            @Override
            public void update(final ResponseObject response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.progressBar1).setVisibility(View.GONE);
                        mContactAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void doUpdateRequest(ResponseObject response) {

    }

    private void initWidget() {
        dialog = new MaterialDialog(this);
        dialog.setCanceledOnTouchOutside(true);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardContact();
            }
        });
    }


    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    private void deleteContactById(int id) {
        ContactEntity contact = ContactDbModel.getInstance(this).getContactById(id);
        if (contact != null) {
            ContactDbModel.getInstance(this).delete(contact.getKeyId());
        }
        mContactAdapter.removeItemById(id);
    }

    private void showDeleteDialog() {

        if (dialog != null) {
            dialog.setMessage("Are you sure to delete this information");
            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    deleteContactById(mDelId);
                }
            })
                    .setNegativeButton("No", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    })
                    .setCanceledOnTouchOutside(true)
                    .setOnDismissListener(
                            new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }
                            }).show();
        }
    }

    public void addCardContact() {
        Intent intent = new Intent(getApplicationContext(),
                FirstEntryPreviewActivity.class);
        startActivityForResult(intent, CONTACT_ADD_UPDATE);

    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            permissions = new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.CAMERA};
            for (String s : permissions)
                if (checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(permissions, pCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == pCode) {
            boolean flag = true;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                for (int i = 0, len = permissions.length; i < len; i++)
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        flag = false;
            if (!flag) {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONTACT_ADD_UPDATE:
                if (resultCode == RESULT_OK) {
                    if (mContactAdapter != null) {
                        mContactAdapter.loadData(new NotifyObserver() {
                            @Override
                            public void update(ResponseObject response) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mContactAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });

                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list:
                Intent intent = new Intent(HomeActivity.this,
                        DisplayImageActivity.class);
                TextView tv_pos = (TextView) view.findViewById(R.id.txtPos);
                int conId = Convert.toInt(tv_pos.getText().toString());
                intent.putExtra(KEY_CONTACT_OBJECT, conId);
                startActivityForResult(intent, CONTACT_ADD_UPDATE);
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list:
                TextView tv_pos = (TextView) view.findViewById(R.id.txtPos);
                String text = tv_pos.getText().toString();
                mDelId = Convert.toInt(text);
                showDeleteDialog();
                break;

            default:
                break;
        }
        return true;
    }
}
