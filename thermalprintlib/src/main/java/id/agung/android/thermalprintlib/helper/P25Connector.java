package id.agung.android.thermalprintlib.helper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

/**
 * Created by agung on 29/07/18.
 */

//P25 Printer Connection class

public class P25Connector {
    public BluetoothSocket mBluetoothSocket;
    private OutputStream mOutputStream;

    private Boolean mIsconnecting = false;

    private static final String TAG = "P25";
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private P25ConnectionListener mListener;

    private ConnectTask mConnectTask;

    private class ConnectTask extends AsyncTask<URL, Integer, Long> {
        BluetoothDevice device;
        String error;

        public ConnectTask(BluetoothDevice device) {
            this.error = "";
            this.device = device;
        }

        protected void onCancelled() {
            P25Connector.this.mIsconnecting = false;

            P25Connector.this.mListener.onConnectionCancelled();
        }

        protected void onPreExecute() {
            P25Connector.this.mListener.onStartConnecting();

            P25Connector.this.mIsconnecting = true;
        }

        @Override
        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                P25Connector.this.mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
                P25Connector.this.mBluetoothSocket.connect();
                P25Connector.this.mOutputStream = P25Connector.this.mBluetoothSocket.getOutputStream();
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
                error = e.getMessage();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress){

        }

        protected void onPostExecute(Long result){
            P25Connector.this.mIsconnecting = false;

            if (P25Connector.this.mBluetoothSocket != null && result.longValue() == 1){
                P25Connector.this.mListener.onConnectionSuccess();
            }else {
                P25Connector.this.mListener.onConnectionFailed("Connection failed" + error);
            }
        }
    }

    public interface P25ConnectionListener {
        public abstract void onStartConnecting();

        public abstract void onConnectionCancelled();

        public abstract void onConnectionSuccess();

        public abstract void onConnectionFailed(String error);

        public abstract void onDisconnected();
    }

    public P25Connector(P25ConnectionListener listener) {
        this.mIsconnecting = false;
        this.mListener = listener;
    }

    public boolean isConnecting(){
        return mIsconnecting;
    }

    public boolean isConnected(){
        return this.mBluetoothSocket !=null;
    }

    public void connect(BluetoothDevice device) throws P25ConnectionException{
        if (this.mIsconnecting && this.mConnectTask != null){
            throw new P25ConnectionException("Connection in progress");
        } else if (this.mBluetoothSocket != null){
            throw new P25ConnectionException("Socket already connected");
        }else {
            ConnectTask connectTask = new ConnectTask(device);
            this.mConnectTask = connectTask;
            connectTask.execute(new URL[0]);
        }
    }

    public void disconnected() throws P25ConnectionException{
        if (this.mBluetoothSocket == null){
            throw new P25ConnectionException("Socket is not connected");
        }
        try {
            this.mBluetoothSocket.close();
            this.mBluetoothSocket = null;
            this.mListener.onDisconnected();
        }catch (IOException e){
            throw new P25ConnectionException(e.getMessage());
        }
    }

    public void cancel() throws P25ConnectionException{
        if (!this.mIsconnecting || this.mConnectTask == null){
            throw new P25ConnectionException("No connection is in progress");
        }
        this.mConnectTask.cancel(true);
    }

    public void sendData(byte[] msg) throws P25ConnectionException{
        if (mBluetoothSocket == null){
            throw new P25ConnectionException("Socket is not connected, try to call connect() first");
        }
        try {
            mOutputStream.write(msg);
            mOutputStream.flush();
        }catch (IOException e){
            throw new P25ConnectionException(e.getMessage());
        }
    }
}
