

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment


fun isLollipopOrBellow(): Boolean = (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP)


fun Fragment.isPermissionGranted(permission: AppPermission) = (activity?.let { PermissionChecker.checkSelfPermission(it, permission.permissionName) } == PackageManager.PERMISSION_GRANTED)

fun Fragment.isRationaleNeeded(permission: AppPermission) = activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, permission.permissionName) } ?: false

fun Fragment.requestPermission(permission: AppPermission) = requestPermissions(arrayOf(permission.permissionName), permission.requestCode)

inline fun Fragment.handlePermission(permission: AppPermission, onGranted: (AppPermission) -> Unit, onDenied: (AppPermission) -> Unit, onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Fragment.handlePermission(permission: AppPermission, onGranted: (AppPermission) -> Unit, onRationaleNeeded: (AppPermission) -> Unit) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> requestPermission(permission)
    }
}


fun Activity.isPermissionGranted(permission: AppPermission) = (PermissionChecker.checkSelfPermission(this, permission.permissionName) == PermissionChecker.PERMISSION_GRANTED)

fun Activity.isRationaleNeeded(permission: AppPermission) = ActivityCompat.shouldShowRequestPermissionRationale(this, permission.permissionName)

fun Activity.requestPermission(permission: AppPermission) = ActivityCompat.requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)

inline fun Activity.handlePermission(
    permission: AppPermission,
    onGranted: (AppPermission) -> Unit,
    onDenied: (AppPermission) -> Unit,
    onRationaleNeeded: (AppPermission) -> Unit
) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> onDenied(permission)
    }
}

inline fun Activity.handlePermission(
    permission: AppPermission,
    onGranted: (AppPermission) -> Unit,
    onRationaleNeeded: (AppPermission) -> Unit
) {
    when {
        isLollipopOrBellow() || isPermissionGranted(permission) -> onGranted(permission)
        isRationaleNeeded(permission) -> onRationaleNeeded(permission)
        else -> requestPermission(permission)
    }
}


fun onRequestPermissionsResultReceived(
    requestCode: Int, permissions: Array<out String>,
    grantResults: IntArray,
    onPermissionGranted: (AppPermission) -> Unit,
    onPermissionDenied: (AppPermission) -> Unit
) {
    AppPermission.permissions.find {
        it.requestCode == requestCode
    }?.let {
        val permissionGrantResult = mapPermissionsAndResults(permissions, grantResults)[it.permissionName]
        if (PackageManager.PERMISSION_GRANTED == permissionGrantResult) {
            onPermissionGranted(it)
        } else {
            onPermissionDenied(it)
        }
    }
}

private fun mapPermissionsAndResults(permissions: Array<out String>, grantResults: IntArray): Map<String, Int> =
    permissions.mapIndexedTo(mutableListOf<Pair<String, Int>>()) { index, permission -> permission to grantResults[index] }.toMap()
