package org.covidwatch.android.presentation.home

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.annotation.WorkerThread
import org.covidwatch.android.CovidWatchTcnManager
import pub.devrel.easypermissions.EasyPermissions

class EnsureTcnIsStartedUseCase(
    private val context: Context,
    private val tcnManager: CovidWatchTcnManager
) {

    private val hasLocationPermission: Boolean
        get() {
            val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            return EasyPermissions.hasPermissions(context, *perms)
        }

    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }
    private val isBluetoothEnabled: Boolean get() = bluetoothAdapter?.isEnabled == true

    @WorkerThread
    fun execute(presenter: EnsureTcnIsStartedPresenter) {
        when {
            !hasLocationPermission -> {
                tcnManager.stopService()
                presenter.showLocationPermissionBanner()
            }
            !isBluetoothEnabled -> {
                tcnManager.stopService()
                presenter.showEnableBluetoothBanner()
            }
            else -> {
                tcnManager.startService()
                presenter.hideBanner()
            }
        }
    }
}