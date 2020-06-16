class HomeActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var snackBar: Snackbar? = null
    private lateinit var connectivityReceiver : ConnectivityReceiver

    override fun onCreate(){
        connectivityReceiver = ConnectivityReceiver()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onStop() {
        ConnectivityReceiver.connectivityReceiverListener = null
        unregisterReceiver(connectivityReceiver)
        super.onStop()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(parent, "ইন্টারনেট কানেকশন সমস্যা হচ্ছে", Snackbar.LENGTH_INDEFINITE)
            snackBar?.show()
        } else {
            snackBar?.dismiss()
        }
    }
}