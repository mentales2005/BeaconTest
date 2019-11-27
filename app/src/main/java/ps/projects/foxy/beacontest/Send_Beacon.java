package ps.projects.foxy.beacontest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Send_Beacon extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;
    private static final String TAG = "MainActivity";
    private BeaconTransmitter mBeaconTransmitter;
    private Beacon beacon;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send__beacon);

        Button btn = findViewById(R.id.button);

        /*
         bluetoothManager = (BluetoothManager) this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
         bluetoothAdapter = bluetoothManager.getAdapter();
        BluetoothLeAdvertiser bluetoothAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();

        AdvertisementData.Builder dataBuilder = new AdvertisementData.Builder();
        dataBuilder.setManufacturerData((int) 0, advertisingBytes);

        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        settingsBuilder.setType(AdvertiseSettings.ADVERTISE_TYPE_NON_CONNECTABLE);

        bluetoothAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), advertiseCallback);
*/

        try {
            byte[] urlBytes = UrlBeaconUrlCompressor.compress("http://www.davidgyoungtech.com");
            Identifier encodedUrlIdentifier = Identifier.fromBytes(urlBytes, 0, urlBytes.length, false);
            ArrayList<Identifier> identifiers = new ArrayList<Identifier>();
            identifiers.add(encodedUrlIdentifier);
            beacon = new Beacon.Builder()
                    .setIdentifiers(identifiers)
                    .setManufacturer(0x0118)
                    .setTxPower(-59)
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT);
            BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon);
        } catch (MalformedURLException e) {
            Log.d(TAG, "That URL cannot be parsed");
        }

//        if (checkPrerequisites()) {
//
//            // Sets up to transmit as an AltBeacon-style beacon.  If you wish to transmit as a different
//            // type of beacon, simply provide a different parser expression.  To find other parser expressions,
//            // for other beacon types, do a Google search for "setBeaconLayout" including the quotes
//
//            mBeaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
//            // Transmit a beacon with Identifiers 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 1 2
//            Beacon beacon = new Beacon.Builder()
//                    .setId1("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6")
//                    .setId2("1")
//                    .setId3("2")
//                    .setManufacturer(0x004c) // Choose a number of 0x00ff or less as some devices cannot detect beacons with a manufacturer code > 0x00ff
//                    .setTxPower(-59)
//                    .setDataFields(Arrays.asList(new Long[]{0l}))
//                    .build();
//
//            mBeaconTransmitter.startAdvertising(beacon);
//        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeaconTransmitter.stopAdvertising();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeaconTransmitter.stopAdvertising();
    }


    private boolean checkPrerequisites() {

        if (android.os.Build.VERSION.SDK_INT < 18) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not supported by this device's operating system");
            builder.setMessage("You will not be able to transmit as a Beacon");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;
        }
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not supported by this device");
            builder.setMessage("You will not be able to transmit as a Beacon");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;
        }
        if (!((BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth not enabled");
            builder.setMessage("Please enable Bluetooth and restart this app.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;

        }

        try {
            // Check to see if the getBluetoothLeAdvertiser is available.  If not, this will throw an exception indicating we are not running Android L
            ((BluetoothManager) this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().getBluetoothLeAdvertiser();
        } catch (Exception e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE advertising unavailable");
            builder.setMessage("Sorry, the operating system on this device does not support Bluetooth LE advertising.  As of July 2014, only the Android L preview OS supports this feature in user-installed apps.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }

            });
            builder.show();
            return false;

        }

        return true;
    }

}
/*
        //
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                /*
                Beacon beacon = new Beacon.Builder()
                        .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                        .setId2("1")
                        .setId3("2")
                        .setManufacturer(0x004c) // Radius Networks.  Change this for other beacon layouts
                        .setTxPower(-59)
                        .setDataFields(Arrays.asList(new Long[]{0l})) // Remove this for beacon layouts without d: fields
                        .build();
// Change the layout below for other beacon types
                BeaconParser beaconParser = new BeaconParser()
                        .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
                BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
                beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

                    @Override
                    public void onStartFailure(int errorCode) {
                        Toast.makeText(Send_Beacon.this,"Advertisement start failed with code: ",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                        Toast.makeText(Send_Beacon.this,"Advertisement start succeeded.",Toast.LENGTH_LONG).show();
                    }
                });




            }
        });
*/



