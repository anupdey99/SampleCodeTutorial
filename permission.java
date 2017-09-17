import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.provider.Settings;

Activity thisActivity = this;
private final int REQUEST_CODE_STORAGE = 123;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mainActivity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getStoragePermissions();

            }
        });

}

 private void getStoragePermissions() {
 
  if(Build.VERSION.SDK_INT >= 23){

      String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED) {

          // Should we show an explanation?
          if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

              ActivityCompat.requestPermissions(this,new  String[] {Manifest.permission.SEND_SMS}, REQUEST_CODE_STORAGE);

          } else {
              ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_STORAGE);
          }

      }else {
          // Task after permission check
          selectImage();
      }

  }else {

      // Task after permission check
      selectImage();

  }
}//

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

    switch (requestCode){

        case REQUEST_CODE_STORAGE:{
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Task after permissions given
                selectImage();
            } else {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    ActivityCompat.requestPermissions(this,new  String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE);
                }else {
                    String message = "Please go to Settings to enable Storage permission. (Settings-apps--permissions)";
                    showDialog(message);
                }
            }
            break;
        }
    }
}//

private void showDialog(String msg){

final AlertDialog permissionRationaleDialog = new AlertDialog.Builder(this).create();
permissionRationaleDialog.setTitle("Permission Required!");
permissionRationaleDialog.setMessage(msg);
permissionRationaleDialog.setCancelable(false);
//icon
permissionRationaleDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Settings", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(i);

    }
});
permissionRationaleDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Deny", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        //finish();
        //System.exit(0);
    }
}); 
permissionRationaleDialog.show();

}

