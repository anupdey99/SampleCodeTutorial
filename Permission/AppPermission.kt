

import android.Manifest


sealed class AppPermission(
    val permissionName: String,
    val requestCode: Int,
    val deniedMessageId: Int,
    val explanationMessageId: Int
) {
    companion object {
        val permissions: List<AppPermission> by lazy {
            listOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA)
        }
    }

    /**CAMERA PERMISSIONS**/
    object CAMERA : AppPermission(Manifest.permission.CAMERA, 1, R.string.permission_camera_denied, R.string.permission_camera_explanation)

    /**READ/WRITE TO STORAGE PERMISSIONS**/
    object READ_EXTERNAL_STORAGE : AppPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 23, R.string.permission_read_ext_storage_denied, R.string.permission_read_ext_storage_explanation)

    object WRITE_EXTERNAL_STORAGE : AppPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 24, R.string.permission_write_ext_storage_denied, R.string.permission_write_ext_storage_explanation)
}