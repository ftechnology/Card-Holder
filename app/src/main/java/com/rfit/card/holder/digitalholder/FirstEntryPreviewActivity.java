package com.rfit.card.holder.digitalholder;

import java.io.ByteArrayOutputStream;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.rfit.card.holder.digitalholder.appfrw.BaseActivity;
import com.rfit.card.holder.digitalholder.appfrw.ResponseObject;
import com.rfit.card.holder.digitalholder.entity.ContactEntity;
import com.rfit.card.holder.digitalholder.listener.CropCompleteListener;
import com.rfit.card.holder.digitalholder.model.ContactDbModel;
import com.rfit.card.holder.digitalholder.utility.GPSTracker;
import com.rfit.card.holder.digitalholder.utility.Utill;
import com.sftech.business.cardholder.R;

public class FirstEntryPreviewActivity extends BaseActivity implements CropCompleteListener {

    private ImageView mImageView;
    private EditText editTextName, editTextCompany, editTextContact, editTextAddress, edtEmail;
    Bitmap personBitmap, cardBitmap;
    ProgressBar progressBar;
    GPSTracker gps;


    public static final int PICK_PER_IMAGE_ID = 1;
    public static final int PICK_CARD_IMAGE_ID = 2;

    String address;

    @Override
    protected void createView(Bundle savedInstanceState) {
        setContentView(R.layout.cam_new);
        setUpToolbar();
        initWidget();

        personBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_user);
        cardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_card);
    }

    @Override
    protected void createAdapter() {

    }


    protected void initWidget() {
        mImageView = (ImageView) findViewById(R.id.image_first);
        editTextName = (EditText) findViewById(R.id.edt_name);
        editTextCompany = (EditText) findViewById(R.id.edt_company);
        editTextContact = (EditText) findViewById(R.id.edt_contact);
        editTextAddress = (EditText) findViewById(R.id.edt_address);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
    }


    public void clickSave(View v) {
        saveData();
    }

    public void clickBack(View v) {
        onBackPressed();
    }

    private void saveData() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        personBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] personImStream = stream.toByteArray();
        stream.reset();
        cardBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] cardImStream = stream.toByteArray();
        String personName = editTextName.getText().toString();
        if (personName == null || "".equals(personName)) {
            editTextName.setError(getString(R.string.STR_ENTER_NAME));
            return;
        }
        String companyName = editTextCompany.getText().toString();
        String address = editTextAddress.getText().toString();
        String personEmail = edtEmail.getText().toString();
        String contact = editTextContact.getText().toString();

        if (personImStream == null || cardImStream == null) {
            return;
        }

        long id = ContactDbModel.getInstance(mInstance).insert(new ContactEntity(personName, companyName, contact, personEmail, address, personImStream, cardImStream));

//        db.addContact(new Contact(personName, address, contact, companyName,
//                personEmail, personImStream, cardImStream));
        this.setResult(RESULT_OK);
        finish();

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utill.forceHideIME(FirstEntryPreviewActivity.this);
                onBackPressed();
            }
        });

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void doUpdateRequest(ResponseObject response) {

    }

    public void onPickPerImage(View v) {
        ImageCropActivity.setCropListener(this);
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("requestCode", PICK_PER_IMAGE_ID);
        startActivity(intent);
    }

    public void onPickCardImage(View v) {
        ImageCropActivity.setCropListener(this);
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("requestCode", PICK_CARD_IMAGE_ID);
        startActivity(intent);
    }

    @Override
    public void onCompleteCrop(int requestCode, Bitmap bitmap) {
        if (requestCode == PICK_PER_IMAGE_ID) {
            personBitmap = bitmap;
            if (personBitmap != null) {
                mImageView.setImageBitmap(personBitmap);
            }
        } else if (requestCode == PICK_CARD_IMAGE_ID) {
            cardBitmap = bitmap;
            if (cardBitmap != null) {
                ImageView cardImageView = (ImageView) findViewById(R.id.iv_visiting_card);
                cardImageView.setVisibility(View.VISIBLE);
                cardImageView.setImageBitmap(cardBitmap);
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                clickSave(null);
                Utill.forceHideIME(FirstEntryPreviewActivity.this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (personBitmap != null)
            personBitmap.recycle();
        if (cardBitmap != null)
            cardBitmap.recycle();
        personBitmap = null;
        cardBitmap = null;
    }
}
