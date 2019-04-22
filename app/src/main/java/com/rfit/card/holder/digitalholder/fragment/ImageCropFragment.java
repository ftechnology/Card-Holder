package com.rfit.card.holder.digitalholder.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.CropImageView;
import com.rfit.card.holder.digitalholder.ImageCropActivity;
import com.sftech.business.cardholder.R;

public class ImageCropFragment extends Fragment {
  private static final String TAG = ImageCropFragment.class.getSimpleName();
  private static final String KEY_FRAME_RECT = "FrameRect";
  private static final String KEY_SOURCE_URI = "SourceUri";

  // Views ///////////////////////////////////////////////////////////////////////////////////////
  private CropImageView mCropView;
  private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
  private RectF mFrameRect = null;
  private Uri mSourceUri = null;

  Bitmap mBitmap = null;

  // Note: only the system can call this constructor by reflection.
  public ImageCropFragment() {
  }

  public static ImageCropFragment newInstance(Bitmap bitmap) {
    ImageCropFragment fragment = new ImageCropFragment();
    Bundle args = new Bundle();
    args.putParcelable("bitmap", bitmap);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_cropimage, null, false);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // bind Views
    bindViews(view);

    mBitmap = getArguments().getParcelable("bitmap");

    if (savedInstanceState != null) {
      // restore data
      mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT);
    }

    if (mBitmap == null) {
      return;
    }

    mCropView.setCropMode(CropImageView.CropMode.FREE);
    // load image
    mCropView.setImageBitmap(mBitmap);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    // save data
    outState.putParcelable(KEY_FRAME_RECT, mCropView.getActualCropRect());
    outState.putParcelable(KEY_SOURCE_URI, mCropView.getSourceUri());
  }


  // Bind views //////////////////////////////////////////////////////////////////////////////////

  private void bindViews(View view) {
    mCropView = (CropImageView) view.findViewById(R.id.cropImageView);
    view.findViewById(R.id.buttonDone).setOnClickListener(btnListener);

    view.findViewById(R.id.buttonRotateLeft).setOnClickListener(btnListener);
    view.findViewById(R.id.buttonRotateRight).setOnClickListener(btnListener);
  }

  public static Uri getUriFromDrawableResId(Context context, int drawableResId) {
    StringBuilder builder = new StringBuilder().append(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .append("://")
            .append(context.getResources().getResourcePackageName(drawableResId))
            .append("/")
            .append(context.getResources().getResourceTypeName(drawableResId))
            .append("/")
            .append(context.getResources().getResourceEntryName(drawableResId));
    return Uri.parse(builder.toString());
  }


  // Handle button event /////////////////////////////////////////////////////////////////////////

  private final View.OnClickListener btnListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      switch (v.getId()) {
        case R.id.buttonDone:
          Bitmap cropped = mCropView.getCroppedBitmap();
          ((ImageCropActivity) getActivity()).setCropImage(cropped);
          break;

        case R.id.buttonRotateLeft:
          mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
          break;
        case R.id.buttonRotateRight:
          mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
          break;
        }
    }
  };
}