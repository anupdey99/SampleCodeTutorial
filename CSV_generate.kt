
fun genetareCSV(shopList: List<Shop>) {

Timber.d("shopList size ${shopList?.size}")
    if (!shopList.isNullOrEmpty()){
        val builder = StringBuilder()
        builder.append(shopList.first().csvTitle())
        shopList.forEach { shopModel ->
            builder.append(shopModel.csvData())
        }

        val file = File(  "${Environment.getExternalStorageDirectory()}", "/${getString(R.string.app_name)}/backup")
        if (!file.exists()){
            file.mkdirs()
        }
        val file1 = File(file, "shop.csv")
        try {
            file1.printWriter().use {
                it.write(builder.toString())
            }
        }catch (e: IOException) {
            e.printStackTrace()
            Timber.e(e)
        }

        binding.parent.showSnackbar("Data Exported successfully")
    } else {
        binding.parent.showSnackbar("Nothing to export")
    }

}

fun readCSV() {


if (!isPermission()) {
        return@setOnClickListener
    }
    val url = "${Environment.getExternalStorageDirectory()}/${getString(R.string.app_name)}/backup/shop.csv"
    val file = File(url)
    if (file.exists()) {

        val lineList = mutableListOf<String>()
        file.useLines { lines ->
            Timber.d("Reading lines")
            lines.forEach { s ->
                lineList.add(s)
            }
        }
        Timber.d("$lineList")

        val modelList = mutableListOf<ShopModel>()
        lineList.forEachIndexed { index, s ->
            if (index != 0) {
                try {
                    val dataArray = s.split(",")
                    val shopModel = ShopModel(dataArray[0],dataArray[1],dataArray[2],dataArray[3],dataArray[4],dataArray[5])
                    modelList.add(shopModel)
                } catch (e: Exception) {
                    Timber.d(e)
                }
            }
        }
        Timber.d("$modelList")

        if (modelList.isNotEmpty()){
            viewModel.addShopList(modelList).observe(viewLifecycleOwner, Observer {
                if (it)
                    binding.parent.showSnackbar("Data imported successfully")
                else binding.parent.showSnackbar("Something wend wrong")
            })
        } else {
            binding.parent.showSnackbar("Nothing to import")
        }

    } else {
        binding.parent.showSnackbar("Nothing to import")
    }
}

fun shareCSV() {


    val path = InvoiceGenerator(requireContext()).generate(model)
    if(!path.isNullOrEmpty()) {
        Timber.d("path: $path")
        val file = File(path)
        if (file.exists()){
            Timber.d("path: Valid")
            val uri = FileProvider.getUriForFile(requireContext(), requireActivity().packageName + ".provider", file)

            val intent = Intent(Intent.ACTION_SEND)
            with(intent){
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                type = "application/pdf"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(model.customerEmail))
                putExtra(Intent.EXTRA_SUBJECT, "${model.shopName} Invoice-${model.invoiceNo}")
                putExtra(Intent.EXTRA_TEXT, "Best regards, Thank you")
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            startActivity(Intent.createChooser(intent, "Share with"))
        } else {
            binding.parent.showSnackbar("Invoice pdf file is removed")
        }
    } else {
        binding.parent.showSnackbar("Invoice pdf can not be generated")
    }

}