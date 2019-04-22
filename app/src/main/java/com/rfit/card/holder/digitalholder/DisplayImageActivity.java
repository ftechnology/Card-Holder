package com.rfit.card.holder.digitalholder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rfit.card.holder.digitalholder.appfrw.BaseActivity;
import com.rfit.card.holder.digitalholder.appfrw.Convert;
import com.rfit.card.holder.digitalholder.appfrw.ResponseObject;
import com.rfit.card.holder.digitalholder.entity.ContactEntity;
import com.rfit.card.holder.digitalholder.listener.CropCompleteListener;
import com.rfit.card.holder.digitalholder.model.ContactDbModel;
import com.rfit.card.holder.digitalholder.utility.MaterialDialog;
import com.rfit.card.holder.digitalholder.utility.Utill;
import com.rfit.card.holder.digitalholder.view.TouchImageView;
import com.sftech.business.cardholder.R;

import java.io.ByteArrayOutputStream;

public class DisplayImageActivity extends BaseActivity implements CropCompleteListener {
    ImageView imageDetail, mImageSecond;
    int imageId;
    EditText tvName, edtCompany, editContact, edtAddress, edtEmail;
    TextView visitingCard;
    Bitmap personImage, cardImage;

    private static final int PICK_PER_IMAGE_ID = 1;
    private static final int PICK_CARD_IMAGE_ID = 2;
    private MaterialDialog dialog;

    @Override
    protected void createView(Bundle savedInstanceState) {
        setContentView(R.layout.cam_new);

        setupToolbar();
        initWidget();

        Intent intnt = getIntent();
        int keyId = intnt.getIntExtra(HomeActivity.KEY_CONTACT_OBJECT, 0);

        ContactEntity contact = ContactDbModel.getInstance(mInstance).getContactById(keyId);
        if (contact != null) {
            editContact.setText(contact.getKeyContact());
            edtCompany.setText(contact.getKeyCompany());
            edtEmail.setText(contact.getKeyEmail());
            edtAddress.setText(contact.getKeyAddress());
            tvName.setText(contact.getKeyName());

            personImage = BitmapFactory.decodeByteArray((byte[]) contact.getKeyImage(), 0,
                    ((byte[]) contact.getKeyImage()).length);
            cardImage = BitmapFactory.decodeByteArray((byte[]) contact.getKeyImagetwo(), 0,
                    ((byte[]) contact.getKeyImagetwo()).length);
            imageId = Convert.toInt(contact.getKeyId());

            imageDetail.setImageBitmap(personImage);
            mImageSecond.setImageBitmap(cardImage);
        }
    }

    @Override
    protected void createAdapter() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void doUpdateRequest(ResponseObject response) {

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utill.forceHideIME(DisplayImageActivity.this);
                onBackPressed();
            }
        });
    }

    public void clickPersonImage(View v) {
        showImage(personImage);

    }

    public void clickCardImage(View v) {
        showImage(cardImage);
    }


    private void showDeleteDialog() {

        if (dialog != null) {
            dialog.setMessage("Are you sure to delete this information");
            dialog.setPositiveButton("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    ContactEntity contact = ContactDbModel.getInstance(mInstance).getContactById(imageId);
                    if (contact != null) {
                        ContactDbModel.getInstance(mInstance).delete(contact.getKeyId());
                    }
                    DisplayImageActivity.this.setResult(RESULT_OK);
                    finish();
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

    private void initWidget() {

        dialog = new MaterialDialog(this);
        dialog.setCanceledOnTouchOutside(true);

        tvName = (EditText) findViewById(R.id.edt_name);
        edtCompany = (EditText) findViewById(R.id.edt_company);
        imageDetail = (ImageView) findViewById(R.id.image_first);
        editContact = (EditText) findViewById(R.id.edt_contact);
        edtEmail = (EditText) findViewById(R.id.edt_email);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.txt_person)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.txt_company)).setVisibility(View.VISIBLE);

        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtAddress.setVisibility(View.VISIBLE);

        mImageSecond = (ImageView) findViewById(R.id.iv_visiting_card);
        mImageSecond.setVisibility(View.VISIBLE);
        visitingCard = (TextView) findViewById(R.id.txt_visitig_card);
        visitingCard.setVisibility(View.VISIBLE);
    }


    public void clickSave(View v) {

        String address = edtAddress.getText().toString();
        String com = edtCompany.getText().toString();
        String name = tvName.getText().toString();
        String phone = editContact.getText().toString();
        String email = edtEmail.getText().toString();

        if (name == null || "".equals(name)) {
            tvName.setError("Please enter person name");
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        personImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] personImStream = stream.toByteArray();
        stream.reset();
        cardImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] cardImStream = stream.toByteArray();

        ContactEntity entity = new ContactEntity(Convert.toString(imageId), name, com, phone, email, address, personImStream, cardImStream);
        ContactDbModel.getInstance(mInstance).update(entity);
//        db.updateContact(imageId, name, phone, address, com, email, personImStream,
//                cardImStream);
        // /after updating data go to main page
        this.setResult(RESULT_OK);
        finish();
    }

    public void clickDelete(View v) {
        showDeleteDialog();
    }


    public void clickBack(View v) {
        onBackPressed();
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

    public void showImage(Bitmap bitmap) {
        Dialog builder = new Dialog(DisplayImageActivity.this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // nothing;
            }
        });

        TouchImageView imageView = new TouchImageView(DisplayImageActivity.this);
        imageView.setImageBitmap(bitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    @Override
    public void onCompleteCrop(int requestCode, Bitmap bitmap) {
        if (requestCode == PICK_PER_IMAGE_ID) {
            personImage = bitmap;
            if (personImage != null) {
                imageDetail.setImageBitmap(personImage);
            }
        } else if (requestCode == PICK_CARD_IMAGE_ID) {
            cardImage = bitmap;
            if (cardImage != null) {
                ImageView cardImageView = (ImageView) findViewById(R.id.iv_visiting_card);
                cardImageView.setVisibility(View.VISIBLE);
                cardImageView.setImageBitmap(cardImage);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preview_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                clickSave(null);
                Utill.forceHideIME(DisplayImageActivity.this);
                return true;

            case R.id.delete:
                clickDelete(null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (personImage != null)
            personImage.recycle();
        if (cardImage != null)
            cardImage.recycle();
        cardImage = null;
        personImage = null;
    }
}