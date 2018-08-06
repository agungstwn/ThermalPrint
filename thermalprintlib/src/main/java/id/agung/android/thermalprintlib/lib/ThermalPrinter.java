package id.agung.android.thermalprintlib.lib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.agung.android.thermalprintlib.R;
import id.agung.android.thermalprintlib.helper.ByteConvert;
import id.agung.android.thermalprintlib.helper.ImageHalper;
import id.agung.android.thermalprintlib.helper.P25ConnectionException;
import id.agung.android.thermalprintlib.helper.P25Connector;
import id.agung.android.thermalprintlib.helper.ThermalPrintFormater;
import id.agung.android.thermalprintlib.helper.Utils;

/**
 * Created by agung on 06/08/18.
 */

public class ThermalPrinter {
    private final String TAG =  getClass().getSimpleName();

    private BluetoothDevice         mBluetoothDevice;
    private P25Connector            mConnector;
    private ConnectedThread         mConnectedThread;
    private boolean                 stopWorking;
    private ThermalPrinterConnector mThermalPrinterConnector;
    private static ThermalPrinter   instance;
    private static String           printer;


    public static ThermalPrinter getDefaultInstance(String printerName){
        if (instance == null || !printerName.equals(printer)){
            instance = new ThermalPrinter();
        }
        printer = printerName;
        return instance;
    }

    public interface ThermalPrinterConnector{
        void onStartConnecting();

        void onConnected();

        void onErrorConnected(String error);
    }

    public void connect(BluetoothDevice mBluetoothDevice, final ThermalPrinterConnector mThermalPrinterConnector){
        this.mThermalPrinterConnector = mThermalPrinterConnector;
        this.mBluetoothDevice = mBluetoothDevice;
        if (mConnector == null){
            mConnector = new P25Connector(new P25Connector.P25ConnectionListener() {
                @Override
                public void onStartConnecting() {
                    Log.d(TAG, "onStartConnecting: ");
                    mThermalPrinterConnector.onStartConnecting();
                }

                @Override
                public void onConnectionCancelled() {
                    Log.d(TAG, "onConnectionCancelled: ");
                    mThermalPrinterConnector.onErrorConnected("Connection canceled. ");
                }

                @Override
                public void onConnectionSuccess() {
                    Log.d(TAG, "onConnectionSuccess: ");
                    if (mConnector == null){
                        mThermalPrinterConnector.onErrorConnected("onConnectionSuccess: mConnector " +
                                "is null, connect first");
                    }else if (mConnector.mBluetoothSocket != null){
                        mConnectedThread = new ConnectedThread(mConnector.mBluetoothSocket);
                        mConnectedThread.start();
                        mThermalPrinterConnector.onConnected();
                    }else {
                        Log.d(TAG, "onConnectionSuccess: Error");
                        mThermalPrinterConnector.onErrorConnected("onConnectionSuccess: Error");
                    }
                }

                @Override
                public void onConnectionFailed(String error) {
                    Log.d(TAG, "onConnectionFailed: ");
                    mThermalPrinterConnector.onErrorConnected("Connection failed because " + error);
                }

                @Override
                public void onDisconnected() {
                    Log.d(TAG, "onDisconnected: ");
                    mThermalPrinterConnector.onErrorConnected("Connection failed. ");
                }
            });

            if (mBluetoothDevice.getBondState() == 10){
                try {
                    createBond(mBluetoothDevice);
                }catch (Exception e){
                    Log.i("TAG", "connect: " + e.toString());
                    return;
                }
            }
            try{
                if (this.mConnector.isConnected()){
                    this.mConnector.disconnected();
                    this.stopWorking = true;
                    return;
                }
                this.mConnector.connect(mBluetoothDevice);
            }catch (P25ConnectionException e2){
                e2.printStackTrace();
            }
        }
    }

    private void createBond(BluetoothDevice mBluetoothDevice) throws Exception {
        try {
            Class.forName("android.bluetooth.BluetoothDevice").getMethod("createBond", new Class[0]).invoke(mBluetoothDevice, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void sendData(byte[] bytes){
        try {
            this.mConnector.sendData(bytes);
        }catch (P25ConnectionException e){
            e.printStackTrace();
        }
    }

    public void printText(String text){
        if (text.trim() != ""){
            try {
                if (mConnector != null){
                    sendData(getBytePrintable(text));
                }
                else
                    Log.d(TAG, "mConnector null, connect first. ");
                }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void printWithImage(Object... data){
        List<Byte> mBytePrintTable = new ArrayList<>();
        for (Object obj : data){
            if (obj instanceof Bitmap){
                Bitmap bitmap = ImageHalper.resize((Bitmap) obj);
                byte[] bytes = Utils.decodeBitmap(bitmap);
                for (byte b : bytes){
                    mBytePrintTable.add(b);
                }
            }else {
                byte[] bytes = getBytePrintable(ThermalPrintFormater.format((String)obj));
                for (byte b : bytes){
                    mBytePrintTable.add(b);
                }
            }
        }

        byte[] mBytes = new byte[mBytePrintTable.size()];

        for (int i = 0; i < mBytePrintTable.size(); i++){
            mBytes[i] = mBytePrintTable.get(i);
        }

        if (mBytes.length != 0){
            try{
                sendData(mBytes);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void printWithFormatter(String text){
        if (text.trim() != ""){
            try {
                sendData(getBytePrintable(ThermalPrintFormater.format(text)));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private byte[] getBytePrintable(String text) {
        char[] chrArr = text.toCharArray();
        byte[] datas  = new byte[(chrArr.length + 1)];
        for (int i = 0; i < chrArr.length; i++) {
            char ch = chrArr[i];
            if (ch == '\u00c7') {
                datas[i] = Byte.MIN_VALUE;
            } else if (ch == '\u011e') {
                datas[i] = (byte) 1;
            } else if (ch == '\u0130') {
                datas[i] = (byte) -126;
            } else if (ch == '\u00d6') {
                datas[i] = (byte) -125;
            } else if (ch == '\u015e') {
                datas[i] = (byte) -124;
            } else if (ch == '\u00dc') {
                datas[i] = (byte) -123;
            } else if (ch == '\u00e7') {
                datas[i] = (byte) -122;
            } else if (ch == '\u011f') {
                datas[i] = (byte) -121;
            } else if (ch == '\u0131') {
                datas[i] = (byte) -120;
            } else if (ch == '\u00f6') {
                datas[i] = (byte) -119;
            } else if (ch == '\u015f') {
                datas[i] = (byte) -118;
            } else if (ch == '\u00fc') {
                datas[i] = (byte) -117;
            } else {
                datas[i] = (byte) ch;
            }
        }
        datas[chrArr.length] = (byte) 10;
        return datas;
    }

    private class ConnectedThread extends Thread{
        private final InputStream mInputStream;

        public ConnectedThread(BluetoothSocket mBluetoothSocket){
            InputStream inputStream = null;
            try{
                inputStream = mBluetoothSocket.getInputStream();
            }catch (IOException e){
            }
            this.mInputStream = inputStream;
        }

        public void run(){
            byte[] buffer = new byte[ByteConvert.DEFAULT_TABLE_LENGTH];
            while (!stopWorking){

            }
        }
    }

    public boolean isConnected(){
        if (mConnector == null){
            return false;
        }else {
            if (mConnector.isConnected()){
                return true;
            }else {
                return false;
            }
        }
    }

    public void print(Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background);
        try {
            byte[] stringByte   = getBytePrintable("Hallo");
            byte[] image        = Utils.decodeBitmap(bitmap);
            byte[] printable    = new byte[stringByte.length + image.length];
            int i = 0;
            for (byte b : stringByte){
                printable[i] = b;
                i++;
            }
            for (byte b : image){
                printable[i] = b;
                i++;
            }
            sendData(printable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
