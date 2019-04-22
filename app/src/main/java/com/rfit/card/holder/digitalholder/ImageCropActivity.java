package com.rfit.card.holder.digitalholder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.rfit.card.holder.digitalholder.fragment.ImageCropFragment;
import com.rfit.card.holder.digitalholder.listener.CropCompleteListener;
import com.rfit.card.holder.digitalholder.utility.ImagePicker;
import com.sftech.business.cardholder.R;

/**
 * Created by farukhossain on 12/6/17.
 */

public class ImageCropActivity extends AppCompatActivity{
    public static final int PICK_IMAGE_ID = 100;
    private int REQUEST_CODE = 0;

    private static CropCompleteListener mCropListener = null;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crop_acitivty);
        REQUEST_CODE = getIntent().getIntExtra("requestCode", 0);

        onPickPerImage();
    }

    public static void setCropListener (CropCompleteListener listener){
        mCropListener = listener;
    }

    private void onPickPerImage() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_ID) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, ImageCropFragment.newInstance(bitmap)).commit();
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    public void setCropImage(Bitmap bitmap) {
        if (isFinishing()) return;
        if(bitmap != null && mCropListener != null){
            mCropListener.onCompleteCrop(REQUEST_CODE,bitmap);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCropListener = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
