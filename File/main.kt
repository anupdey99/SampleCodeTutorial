fun main() {


    if (paymentDetailsResponse != null) {
        val generator = ExcelGenerator(requireContext())
        val filePath = generator.writeExcel(paymentDetailsResponse!!)
        Timber.d("generateExcel $filePath")
        if (filePath.isNotEmpty()) {
            binding?.parent?.snackbar("আপনি সফলভাবে এক্সেল শীটে সেভ করেছেন\nSDCard/Download/${getString(R.string.app_name)}", Snackbar.LENGTH_INDEFINITE, "ওপেন") {
                openFile(filePath)
            }?.show()
        }
    } else {
        context?.toast("কোনো তথ্য নেই")
    }

} 

private fun writeExcel() {

    var filePath = ""
    val fileName = "${model.transactionNo}"
    val outputStream = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), context.getString(R.string.app_name))
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        filePath = file.absolutePath
        FileOutputStream(file)
    } else {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + context.getString(R.string.app_name))
        }
        resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
            filePath = FileUtils(context).getPath(uri)
            resolver.openOutputStream(uri)
        }
    }
    outputStream?.use { stream ->
        workBook.write(stream)
    }

    Timber.d("writeExcel $filePath")
    return filePath
}

private fun openFile(filePath: String) {
    try {
        val fileName = Uri.parse(filePath).lastPathSegment
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val fileDir = "${requireContext().getString(R.string.app_name)}/$fileName"
        val filePath = "$directoryPath/$fileDir"

        val file = File(filePath)
        if (file.exists()) {
            val fileUri = FileProvider.getUriForFile(requireContext(), getString(R.string.file_provider_authority), file)
            Intent(Intent.ACTION_VIEW).apply {
                putExtra(Intent.EXTRA_STREAM, fileUri)
                setDataAndType(fileUri, getFileContentType(filePath))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }.also {
                startActivity(it)
            }
        } else {
            binding?.parent?.snackbar("Unable to open file", Snackbar.LENGTH_SHORT)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        binding?.parent?.snackbar("Unable to open file", Snackbar.LENGTH_SHORT)
    }
}