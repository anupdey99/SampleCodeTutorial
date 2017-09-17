
public class SelectImageActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    // Uri
    private Uri cameraImgUri;
    private Uri selectedProPicUri;
    // Request Codes
    private final int REQUEST_CODE_CAMERA  = 100;
    private final int REQUEST_CODE_GALLERY = 200;
    private final int PERMISSION_REQUEST_CODE = 123;
    private Activity thisActivity = SelectImageActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.addProfile_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        selectImage();
    }//

  //*********************** Image ****************

    private void selectImage() {
        final CharSequence[] items = {"Capture profile picture", "Select from Gallery","Delete profile picture", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Profile Picture!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                // take photo by camera
                if (items[item].equals("Capture profile picture")) {
                  // Camera
                    openCamera() 
                } else if (items[item].equals("Select from Gallery")) {
                  // Gallery
                     openGallery()
                } else if (items[item].equals("Delete profile picture")){
                    if (updateID > -1){
                        String gender = getGender();
                        if (gender.equalsIgnoreCase("Male")){
                            setImageResource(R.drawable.man);
                        }else {
                            setImageResource(R.drawable.woman);
                        }
                        selectedProPicUri = null;
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

  private void openCamera(){
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    cameraImgUri = Uri.fromFile(createTemporaryFile());
    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImgUri);
    startActivityForResult(intent, REQUEST_CODE_CAMERA); 
  }
 
  private void openGallery(){
    // Create Chooser 
    Intent intent = new Intent();
    intent.setType("*/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GALLERY);
    // or
    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
  }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){

            case REQUEST_CODE_CAMERA:{

                if (resultCode == Activity.RESULT_OK ){
                    UCrop.Options options = new UCrop.Options();
                    options.setToolbarColor(ActivityCompat.getColor(thisActivity,R.color.colorPrimary));
                    options.setStatusBarColor(ActivityCompat.getColor(thisActivity, R.color.colorPrimaryDark));
                    options.setActiveWidgetColor(ActivityCompat.getColor(thisActivity,R.color.colorAccent));
                    Uri destinationUri = getOutputFileUri();
                    if (destinationUri != null){
                        UCrop.of(cameraImgUri,destinationUri)
                                .withOptions(options)
                                .withAspectRatio(1,1)
                                .withMaxResultSize(500,500)
                                .start(thisActivity);
                    }
                    deleteTemporaryFile(cameraImgUri);
                }
                break;
            }
            case REQUEST_CODE_GALLERY:{

                if (resultCode == Activity.RESULT_OK){

                    Uri filePath = data.getData();
                    if (filePath != null) {

                        // Manifast activity entry, parmissins
                        UCrop.Options options = new UCrop.Options();
                        options.setToolbarColor(ActivityCompat.getColor(thisActivity,R.color.colorPrimary));
                        options.setStatusBarColor(ActivityCompat.getColor(thisActivity, R.color.colorPrimaryDark));
                        options.setActiveWidgetColor(ActivityCompat.getColor(thisActivity,R.color.colorAccent));
                        Uri destinationUri = getOutputFileUri();
                        if (destinationUri != null){
                            UCrop.of(filePath, destinationUri)
                                    .withOptions(options)
                                    .withAspectRatio(1, 1)
                                    .withMaxResultSize(500, 500)
                                    .start(thisActivity);
                        }
                    }
                }
                break;
            }
            case UCrop.REQUEST_CROP:{

                if (resultCode == Activity.RESULT_OK){

                    selectedProPicUri = UCrop.getOutput(data);
                    profilePictureIMG.setImageURI(selectedProPicUri);

                }
                break;
            }
            default: super.onActivityResult(requestCode, resultCode, data);
        }


    }//


    private  Uri getOutputFileUri() {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "iCare");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String path = mediaStorageDir.getPath() + File.separator + timeStamp ;
        new File(path);
        return Uri.parse(path);
    }

    private File createTemporaryFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "iCare/.temp/");
        //File tempDir= Environment.getExternalStorageDirectory();
        //tempDir = new File(tempDir.getAbsolutePath()+"/.temp/");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String tempImgPath = mediaStorageDir.getPath() + File.separator + "tmp_"+ timeStamp + ".jpg";
        return new File(tempImgPath);
    }

    private boolean deleteTemporaryFile( Uri uri ){
     return new File(uri.toString()).delete();
    }
  
  

    private void checkForPermission(){

        if(Build.VERSION.SDK_INT >= 23){

            if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, Manifest.permission.CAMERA)) {

                    ActivityCompat.requestPermissions(thisActivity,new  String[] {Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);

                } else {
                    ActivityCompat.requestPermissions(thisActivity, new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                }

            }else {
                selectImage();
            }
        }else {
            selectImage();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    selectImage();
                }else {
                    // Never ask permission handle
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, Manifest.permission.CAMERA)){
                        String message = "Please go to Settings to enable Camera permission. (Settings-apps-iCare-permissions)";
                        final AlertDialog permissionRationaleDialog = new AlertDialog.Builder(thisActivity).create();
                        permissionRationaleDialog.setTitle("Permission Required!");
                        permissionRationaleDialog.setMessage(message);
                        permissionRationaleDialog.setCancelable(false);
                        //icon
                        permissionRationaleDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                final Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + thisActivity.getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                thisActivity.startActivity(i);

                            }
                        });
                        permissionRationaleDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }); permissionRationaleDialog.show();
                    }
                }
                // Permission Denied
                //Snackbar.make(findViewById(R.id.addProfileActivity) , "Some Permissions are Denied", Snackbar.LENGTH_LONG).setAction("OK", null).show();
                break;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void datePicker() {

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDate = dateFormatter.format(newDate.getTime());
                Utility utility = new Utility(thisActivity);
                verifyBirthday = utility.verifyBirthday(birthDate);
                if (!verifyBirthday){
                    Snackbar.make(findViewById(R.id.addProfileActivity) , "Invalid Birth Date", Snackbar.LENGTH_LONG).show();
                    profileBirthDateET.setText(null);
                    birthDate = null;
                }else profileBirthDateET.setText(birthDate);
            }
        }, calender_year, calender_month, calender_day);
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();

    }//

   

    private String validateField(String input){

        if (input != null && !input.trim().isEmpty()){
            return input;
        }else return "-";

    }
    

  
    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null){
            imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
        }

    }

}//

