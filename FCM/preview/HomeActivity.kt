

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)

    onNewIntent(intent)
}

override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    if (intent != null) {
        val model: FCMNotification? = intent.getParcelableExtra("data")
        if (model != null) {
            val fragment = NotificationPreviewFragment.newInstance(model)
            val tag = NotificationPreviewFragment.fragmentTag

            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.mainActivityContainer, fragment, tag)
            ft.addToBackStack(tag)
            ft.commit()

            intent.removeExtra("data")
        }
    }
}