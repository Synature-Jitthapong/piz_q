package com.syn.queuedisplay.custom;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ManageLogoActivity extends Activity implements OnClickListener{
	public static final int REQ_MANAGE_LOGO = 1;
	public static final int SELECT_PHOTO = 2;
	
	private ImageView mImgLogo;
	private Uri mSelectedImageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_logo);
		mImgLogo = (ImageView) findViewById(R.id.imgLogo);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch(item.getItemId()){
			case android.R.id.home:
				intent = new Intent(ManageLogoActivity.this, SettingActivity.class);
				intent.setData(mSelectedImageUri);
				setResult(RESULT_OK, intent);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			mSelectedImageUri = data.getData();
            mImgLogo.setImageURI(mSelectedImageUri);
		}
	}
	
	public String getImageTitle(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
        	//cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        	return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE));
        }
        return null;
    }
	
	private void selectPhoto(){
		 Intent intent = new Intent();
         intent.setType("image/*");
         intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PHOTO);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnLoadPhoto:
			selectPhoto();
			break;
		}
	}

}
