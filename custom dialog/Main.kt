
onCreate() {

    val imageUrl = "https://static.ajkerdeal.com/images/merchant/image_200520.jpg"
    val tag = PopupDialog.tag
    val dialog = PopupDialog.newInstance(imageUrl)
    dialog.show(supportFragmentManager, tag)

}