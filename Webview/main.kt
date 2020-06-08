fun main() {

    ChromeCustomTabBrowser.launch(this, AppConstant.POLICY_PRIVACY_URL) { fallbackUrl ->
        val WebView = WebView(this)
        WebView?.goToWebviewFragment(fallbackUrl, "প্রাইভেসি পলিসি")
        // or
        // findNavController().navigate(R.id.nav_action_dashboard_webView, bundleOf("url" to fallbackUrl))
    }

}