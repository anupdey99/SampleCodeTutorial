###Helper.kt

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context?.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun RecyclerView.disableItemAnimator() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}

fun Activity.alert(title: CharSequence? = null, message: CharSequence? = null, showCancel: Boolean = false, positiveButtonText: String = "Yes", negativeButtonText: String = "Cancel", listener: ((type: Int) -> Unit)? = null): AlertDialog {

    val builder = AlertDialog.Builder(this/*, R.style.DeliveryAlertDialogTheme*/)
    builder.setTitle(title)
    // Display a message on alert dialog
    builder.setMessage(message)
    // Set a positive button and its click listener on alert dialog
    builder.setPositiveButton(positiveButtonText) { dialog, which ->
        dialog.dismiss()
        listener?.invoke(AlertDialog.BUTTON_POSITIVE)
    }
    // Display a negative button on alert dialog
    if (showCancel) {
        builder.setNegativeButton(negativeButtonText) { dialog, which ->
            dialog.dismiss()
            listener?.invoke(AlertDialog.BUTTON_NEGATIVE)
        }
    }

    val dialog = builder.create()
    val typeface = ResourcesCompat.getFont(this, R.font.slabo)
    val textView = dialog.findViewById<TextView>(android.R.id.message)
    textView?.typeface = typeface
    return dialog
}

fun Fragment.alert(title: CharSequence? = null, message: CharSequence? = null, showCancel: Boolean = false, positiveButtonText: String = "Yes", negativeButtonText: String = "Cancel", listener: ((type: Int) -> Unit)? = null): AlertDialog {

    val builder = AlertDialog.Builder(requireContext()/*, R.style.DeliveryAlertDialogTheme*/)
    builder.setTitle(title)
    // Display a message on alert dialog
    builder.setMessage(message)
    // Set a positive button and its click listener on alert dialog
    builder.setPositiveButton(positiveButtonText) { dialog, which ->
        dialog.dismiss()
        listener?.invoke(AlertDialog.BUTTON_POSITIVE)
    }
    // Display a negative button on alert dialog
    if (showCancel) {
        builder.setNegativeButton(negativeButtonText) { dialog, which ->
            dialog.dismiss()
            listener?.invoke(AlertDialog.BUTTON_NEGATIVE)
        }
    }

    val dialog = builder.create()
    val typeface = ResourcesCompat.getFont(requireContext(), R.font.slabo)
    val textView = dialog.findViewById<TextView>(android.R.id.message)
    textView?.typeface = typeface
    return dialog
}

fun Activity.progressDialog(message: String = getString(R.string.app_name)): ProgressDialog {

    val dialog = ProgressDialog(this)
    with(dialog) {
        setMessage(message)
    }
    return dialog
}

fun Fragment.progressDialog(message: String = getString(R.string.app_name)): ProgressDialog {

    val dialog = ProgressDialog(requireContext())
    with(dialog) {
        setMessage(message)
    }
    return dialog
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return (email.matches(emailPattern))
}

fun String.isValidUrl(): Boolean {
    //return Patterns.WEB_URL.matcher(this).matches()
    return URLUtil.isValidUrl(this)
}

fun parseStringDate(dateString: String?): String? {
    if (dateString == null || dateString .isEmpty()) {
        return null
    }
    val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val format1 = "yyyy-MM-dd"
    val inputFormatter = SimpleDateFormat(format, Locale.ENGLISH)
    val outputFormat = SimpleDateFormat(format1, Locale.ENGLISH)
    try {
        val date: Date? = inputFormatter.parse(dateString)
        date?.let {
            return outputFormat.format(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun convertFileToBase64(file: File, withHeader: Boolean = false): String {

    val base64String = FileInputStream(file).use { inputStream ->
        ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                inputStream.copyTo(base64FilterStream)
                base64FilterStream.close() // This line is required, see comments
                outputStream.toString()
            }
        }
    }

    return if (withHeader) "data:${getMimeType(file)};base64,$base64String" else base64String
}

fun getMimeType(file: File): String {
    var mimeType: String? = ""
    val extension: String = file.extension
    if (MimeTypeMap.getSingleton().hasExtension(extension)) {
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return mimeType ?: ""
}

fun Fragment.setToolbarTitle(title: String) {
    (activity as DashboardActivity).supportActionBar?.title = title
}

########### Doenload manager ##############

if (checkForPermission()) {
                val downloadFileRef = download("${CommonData.galleryBaseUrl}${model.url}", model.url ?: "file" ,model.name ?: "")
                if (downloadFileRef != 0L) {
                    FancyToast.makeText(requireContext(), "Starting download...", Toast.LENGTH_SHORT, FancyToast.INFO, false).show()
                } else {
                    FancyToast.makeText(requireContext(), "File is not available for download", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                }
            }

private fun  download(url: String, fileName: String, name: String = ""): Long {
        var downloadReference = 0L
        val uri = Uri.parse(url)
        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        try {
            val request = DownloadManager.Request(uri)
            request.setTitle(fileName)
            request.setDescription("Downloading $name")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+"/FeemaaLawyer",fileName)
            request.allowScanningByMediaScanner()
            downloadReference = downloadManager.enqueue(request)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            FancyToast.makeText(requireContext(), "Download link is broken or not available for download", Toast.LENGTH_SHORT, FancyToast.WARNING, false).show()
        }
        return downloadReference
    }
    
    
    private fun checkForPermission(): Boolean {

        if (Build.VERSION.SDK_INT >= 23) {

            //val cameraSelf = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            val storageSelf = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

            if (storageSelf) {
                // Should we show an explanation?
                //val cameraRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)
                val storageRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (storageRationale) {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

fun getGreetingMessage():String{
    val c = Calendar.getInstance()
    val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

    return when (timeOfDay) {
           in 0..11 -> "Good Morning"
           in 12..15 -> "Good Afternoon"
           in 16..20 -> "Good Evening"
           in 21..23 -> "Good Night"
             else -> {
              "Hello"
          }
      }
    }

fun Fragment.setToolbarTitle(title: String) {
    (activity as DashboardActivity).supportActionBar?.title = title
}

fun Context.toast(msg: String?) {
    if (msg != null)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Response<*>.parseErrJsonResponse(): T? {

    val gson = Gson()
    val response = errorBody()?.string()
    if(response != null)
        try {
            return gson.fromJson(response, T::class.java)
        } catch(e: Exception) {
            e.printStackTrace()
        }
    return null
}


override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            val view = currentFocus
            if (view is TextInputEditText) {
                if (!isPointInsideView(event.rawX, event.rawY, view)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean{
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]

        // point is inside view bounds
        return ((x > viewX && x < (viewX + view.width)) && (y > viewY && y < (viewY + view.height)))
    }

fun Fragment.setToolbarTitle(title: String) {
    (activity as DashboardActivity).supportActionBar?.title = title
}

fun Context.toast(msg: String?) {
    if (msg != null)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Response<*>.parseErrJsonResponse(): T? {

    val gson = Gson()
    val response = errorBody()?.string()
    if(response != null)
        try {
            return gson.fromJson(response, T::class.java)
        } catch(e: Exception) {
            e.printStackTrace()
        }
    return null
}

fun View.showSnackbar(msg: String?, duration: Int = Snackbar.LENGTH_SHORT) {
    if (!msg.isNullOrEmpty()) {
        Snackbar.make(this, msg, duration).show()
    }
}

fun View.snackbarWithAction(message: String, actionText: Int = R.string.request_permission, action: () -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).setAction(actionText) { action() }.show()
}

fun View.snackbar(message: String, length: Int = Snackbar.LENGTH_INDEFINITE, actionName: String, onClick: ((view: View) -> Unit)? = null){
    Snackbar.make(this, message, length).also { snackbar ->
        snackbar.setAction(actionName) {
            onClick?.invoke(it)
            snackbar.dismiss()
        }
    }.show()
}

val <T> T.exhaustive: T
    get() = this

override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){
            val view = currentFocus
            if (view is TextInputEditText) {
                if (!isPointInsideView(event.rawX, event.rawY, view)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean{
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]

        // point is inside view bounds
        return ((x > viewX && x < (viewX + view.width)) && (y > viewY && y < (viewY + view.height)))
    }

    fun isPackageInstalled(packageManager: PackageManager, packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: Exception) {
        false
    }
}

fun Number.spToPx(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics).toInt()