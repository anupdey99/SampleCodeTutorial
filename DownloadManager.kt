fun main() {

    dataAdapter.onDownloadClicked = { model, position ->
        
        val url = "${AppConstant.BASE_IMAGE}${model.filePath}"
        downloadId = download(url)
        if (downloadId > 0) binding.parent.snackbar("Downloading file", Snackbar.LENGTH_SHORT)
    }

}

private fun download(url: String): Long {

    return try {
        //val encodedUrl = URLEncoder.encode(url, "UTF-8")
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment

        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val fileDir = "${requireContext().getString(R.string.app_name_folder)}/$fileName"
        fullPathDownloadedFile = "$directoryPath/$fileDir"
        Timber.d("debugDownload URL $uri")
        Timber.d("debugDownload FilePATH $fileDir")
        Timber.d("debugDownload FullPATH $fullPathDownloadedFile")

        val request = DownloadManager.Request(uri)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileDir)
            .setTitle(fileName)
            .setDescription("Downloading")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)
        Timber.d("debugDownload ID $downloadId")
        return downloadId
    } catch (e: Exception) {
        e.printStackTrace()
        context?.toast("Something went wrong when downloading file, try again later")
        -1
    }
}

private val onDownloadComplete = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (downloadId == id) {
            val msg = "Download Completed. File location sdcard/Download/${requireContext().getString(R.string.app_name_folder)}"
            binding.parent.snackbar(msg, Snackbar.LENGTH_INDEFINITE, "Open") {
                openFile()
            }.show()
        }
    }
}
    
private fun openFile() {
    try {
        val file = File(fullPathDownloadedFile)
        val fileUri = FileProvider.getUriForFile(requireContext(), "com.a2i.apps.emcrp.fileprovider", file)
        Intent(Intent.ACTION_VIEW).apply {
            putExtra(Intent.EXTRA_STREAM, fileUri)
            setDataAndType(fileUri, getFileContentType(fullPathDownloadedFile))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }.also {
            startActivity(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        binding.parent.snackbar("Unable to open file", Snackbar.LENGTH_SHORT)
    }
}

fun getFileContentType(filePath: String): String? {
    val file = File(filePath)
    val map = MimeTypeMap.getSingleton()
    val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
    var type = map.getMimeTypeFromExtension(ext)
    if (type == null) type = "*/*"
    return type
}

override fun onStart() {
    super.onStart()
    requireContext().registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
}

override fun onStop() {
    requireContext().unregisterReceiver(onDownloadComplete)
    super.onStop()
}

