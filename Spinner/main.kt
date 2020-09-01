fun main() {

    val packageList = acquisitionData.packageList
    val packageNameList = packageList.map { it.nameEn }

    // Array Adapter
    val packageSpinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, packageNameList).apply {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }
    binding.spinner1.adapter = packageSpinnerAdapter

    //MaterialSpinnerAdapter
    val adapter = MaterialSpinnerAdapter(view.context, R.layout.dropdown_menu_popup_item, packageNameList)
    binding.packageDropDown.setAdapter(adapter)
    

} 


