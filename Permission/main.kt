
//######### Kotlic Permession Handler extention

private fun checkPermission(): Boolean {

  handlePermission(AppPermission.WRITE_EXTERNAL_STORAGE,
      onGranted = {
          return true
      },
      onDenied = {
          requestPermission(AppPermission.WRITE_EXTERNAL_STORAGE)
          return false
      },
      onRationaleNeeded = {
          binding.parent.snackbarWithAction("Permission required") {
              requestPermission(it)
          }
          return false
      }
  )
  return false
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
  super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  onRequestPermissionsResultReceived(requestCode, permissions, grantResults,
      onPermissionGranted = {
          scanFolder()
      }, onPermissionDenied = {
          binding.parent.showSnackbar("Permission denied")
      })
}


//########### Kotlic fragment

val REQUEST_CODE_CAMERA=123 
val permissions=arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)

fun isCameraPermissions():Boolean{

  if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

    val cameraPermission=ContextCompat.checkSelfPermission(context!!,Manifest.permission.CAMERA)
    val storagePermission=ContextCompat.checkSelfPermission(context!!,Manifest.permission.WRITE_EXTERNAL_STORAGE)

    return if(cameraPermission!=PackageManager.PERMISSION_GRANTED||storagePermission!=PackageManager.PERMISSION_GRANTED){

    val cameraPermissionRationale=ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.CAMERA)
    val storagePermissionRationale=ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if(cameraPermissionRationale||storagePermissionRationale){
        requestPermissions(permissions,REQUEST_CODE_CAMERA)
      }else{
        requestPermissions(permissions,REQUEST_CODE_CAMERA)
      }
      false                                                                                                                                                                                                                                                                                                                                                                                                                                                
    } else {
      true
    }
  }else{
  return true
  }
}//

override fun onRequestPermissionsResult(requestCode:Int,permissions:Array<String>,grantResults:IntArray){
  super.onRequestPermissionsResult(requestCode,permissions,grantResults)
  when(requestCode){
    REQUEST_CODE_CAMERA->{
      if(grantResults.isNotEmpty()){
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
          // Task after permissions given
          goToSacn()
        }else{
          val cameraPermissionRationale=ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.CAMERA)
          val storagePermissionRationale=ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
          if(cameraPermissionRationale||storagePermissionRationale){
            ActivityCompat.requestPermissions(requireActivity(),permissions,REQUEST_CODE_CAMERA)
          }else{
            val message="Please go to Settings to enable Camera & Storage permission. (Settings-apps--permissions)"
            showDialog(message)
          }
        }
      }
    }
  }
}

fun showDialog(msg:String){

val permissionRationaleDialog=MaterialAlertDialogBuilder(context)permissionRationaleDialog.setTitle("Permission Required!")permissionRationaleDialog.setMessage(msg)permissionRationaleDialog.setCancelable(false)
// icon
permissionRationaleDialog.setPositiveButton("Settings"){dialog,which->val i=Intent()i.action=Settings.ACTION_APPLICATION_DETAILS_SETTINGS i.addCategory(Intent.CATEGORY_DEFAULT)i.data=Uri.parse("package:"+requireActivity().packageName)i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)startActivity(i)}permissionRationaleDialog.setNegativeButton("Deny"){dialog,which->dialog.cancel()
// finish();
// System.exit(0);
}permissionRationaleDialog.show()

}

Activity thisActivity=this;private final int REQUEST_CODE_STORAGE=123;

@Override protected void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);

FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.mainActivity_fab);fab.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View view){

getStoragePermissions();

}});

}

private void getStoragePermissions(){

if(Build.VERSION.SDK_INT>=23){

String[]permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};

if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

// Should we show an explanation?
if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},REQUEST_CODE_STORAGE);

}else{ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE_STORAGE);}

}else{
// Task after permission check
selectImage();}

}else{

// Task after permission check
selectImage();

}}//

@Override public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[]grantResults){

switch(requestCode){

case REQUEST_CODE_STORAGE:{if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

// Task after permissions given
selectImage();}else{if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE);}else{String message="Please go to Settings to enable Storage permission. (Settings-apps--permissions)";showDialog(message);}}break;}}}//

private void showDialog(String msg){

final AlertDialog permissionRationaleDialog=new AlertDialog.Builder(this).create();permissionRationaleDialog.setTitle("Permission Required!");permissionRationaleDialog.setMessage(msg);permissionRationaleDialog.setCancelable(false);
// icon
permissionRationaleDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Settings",new DialogInterface.OnClickListener(){@Override public void onClick(DialogInterface dialog,int which){

final Intent i=new Intent();i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);i.addCategory(Intent.CATEGORY_DEFAULT);i.setData(Uri.parse("package:"+getPackageName()));i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);startActivity(i);

}});permissionRationaleDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Deny",new DialogInterface.OnClickListener(){@Override public void onClick(DialogInterface dialog,int which){dialog.cancel();
// finish();
// System.exit(0);
}});permissionRationaleDialog.show();

}
