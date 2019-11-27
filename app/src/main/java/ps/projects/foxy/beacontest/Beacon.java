package ps.projects.foxy.beacontest;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class Beacon extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;
    BluetoothLeAdvertiser bluetoothAdvertiser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        String str = "Weclome";
        byte[] advertisingBytes = str.getBytes();


         bluetoothManager = (BluetoothManager) this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
         bluetoothAdapter = bluetoothManager.getAdapter();
         bluetoothAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();

        AdvertiseData.Builder dataBuilder=new AdvertiseData.Builder();
        dataBuilder.addManufacturerData((int)0, advertisingBytes);



        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);


        bluetoothAdvertiser.startAdvertising(settingsBuilder.build(),dataBuilder.build(),advertiseCallback);
    }

   private AdvertiseCallback advertiseCallback= new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Toast.makeText(Beacon.this,"started advertising successfully.",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Toast.makeText(Beacon.this,"did not start advertising successfully",Toast.LENGTH_LONG).show();
        }


    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothAdvertiser.stopAdvertising(advertiseCallback);
    }
}
