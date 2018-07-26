package id.agung.android.thermalprintlib.connection;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

/**
 * Created by agung on 26/07/18.
 */

public class ThermalBluetooth {
    private final String TAG = getClass().getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;

    public ThermalBluetooth(BluetoothAdapter bluetoothAdapter) {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothEnabled(){
        if (mBluetoothAdapter.isEnabled()){
            return true;
        }else {
            Log.i(TAG, "isBluetoothEnabled: ");
            return false;
        }
    }


}
