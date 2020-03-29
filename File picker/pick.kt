private fun pickBackupFile() {

        val intent = Intent()
        val mimeTypes = arrayOf("application/json")
        intent.action = Intent.ACTION_GET_CONTENT
        with(intent) {
            //addCategory(Intent.CATEGORY_OPENABLE)
            flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            type = "*/*"
            //putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        }
        try {
            startActivityForResult(intent, 420)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }


override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 420 && resultCode == Activity.RESULT_OK && data?.data != null) {
        val path = RealPathUtil.getRealPath(requireContext(), data.data!!)
            Timber.d("Data path: $path")
            val file = File(path)
            if (file.exists()) {
            var jsonString = ""
                file.useLines { lines ->
                    Timber.d("Reading lines")
                    lines.forEach {
                        jsonString = it
                    }
                }
                Timber.d("$jsonString")
            }
    }
}