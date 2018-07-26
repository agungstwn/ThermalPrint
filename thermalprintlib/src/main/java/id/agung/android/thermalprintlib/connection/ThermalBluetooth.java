package id.agung.android.thermalprintlib.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public List<BluetoothDevice> getPairedDevice(){
        List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
        if (isBluetoothEnabled()){
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0){
                for (BluetoothDevice device : pairedDevices){
                    mBluetoothDeviceList.add(device);
                }
                return mBluetoothDeviceList;
            }else {
                Log.i(TAG, "No Paired Device ");
            }
        }else {
            Log.i(TAG, "Bluetooth is Disabled");
        }
        return mBluetoothDeviceList;
    }
}
