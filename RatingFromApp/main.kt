fun main() {
    
    if (!areNotificationsEnabled()) {
        Snackbar.make(binding.parent, "Enable Notification", Snackbar.LENGTH_INDEFINITE).setAction("Settings") {
            openAppNotificationSettings(this)
        }.show()
    }
}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
        R.id.action_settings -> {
            openAppNotificationSettings(this)
            true
        }
        R.id.action_rating -> {
            launchMarket()
            true
        }
        R.id.action_feedback -> {
            feedback()
            true
        }
        R.id.action_share -> {
            shareApp()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}

private fun areNotificationsEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notificationChannels.forEach {channel ->
                if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                    return false
                }
            }
            true
        } else {
            false
        }
    } else {
        NotificationManagerCompat.from(this).areNotificationsEnabled()
    }
}

private fun openAppNotificationSettings(context: Context) {
    val intent = Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            }
            else -> {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + context.packageName)
            }
        }
    }
    context.startActivity(intent)
}

private fun launchMarket() {

    val uri = Uri.parse("market://details?id=$packageName")
    val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    }
    try {
        startActivity(myAppLinkToMarket)
    } catch (e: ActivityNotFoundException) {
        //Toast.makeText(this, "Unable to open Google Play", Toast.LENGTH_LONG).show()
        val intent = Intent(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        startActivity(intent)
    }
}

private fun shareApp() {

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        val msg = "Let me recommend you this application\nhttps://play.google.com/store/apps/details?id=$packageName"
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Notification Preview App")
        putExtra(Intent.EXTRA_TEXT, msg)
    }
    startActivity(Intent.createChooser(shareIntent, "Share with"))
}

private fun feedback() {

    val deviceInfo =
        "Device Info\nOS Version: ${System.getProperty("os.version")}(${Build.VERSION.INCREMENTAL})\nAPI: ${Build.VERSION.SDK_INT}\nDevice: ${Build.DEVICE}\nModel: ${Build.MODEL} (${Build.PRODUCT})\n\n"
    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "adlabs.apps@gmail.com", null))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Notification Preview - Feedback")
    emailIntent.putExtra(Intent.EXTRA_TEXT, deviceInfo)
    startActivity(Intent.createChooser(emailIntent, "Send email..."))
}