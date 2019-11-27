//package ps.projects.foxy.beacontest;
//
//import android.app.Application;
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.altbeacon.beacon.BeaconManager;
//import org.altbeacon.beacon.BeaconParser;
//import org.altbeacon.beacon.Region;
//import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
//import org.altbeacon.beacon.startup.BootstrapNotifier;
//import org.altbeacon.beacon.startup.RegionBootstrap;
//
//public class TestApp extends Application implements BootstrapNotifier {
//    private static final String TAG = ".TestApp";
//    private RegionBootstrap regionBootstrap;
//    private BackgroundPowerSaver backgroundPowerSaver;
//    private BeaconManager mBeaconManager;
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
//        mBeaconManager.getBeaconParsers().clear();
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().
//                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
//        mBeaconManager.bind(this);
//
//        Region region = new Region("my-beacon-region", null, null, null);
//
//        regionBootstrap = new RegionBootstrap(this, region);
//
//        backgroundPowerSaver = new BackgroundPowerSaver(this);
//    }
//
//    @Override
//    public void didEnterRegion(Region arg0) {
//        Log.d(TAG, "Got a didEnterRegion call");
//        Toast.makeText(this,"The first beacon I see is about " ,Toast.LENGTH_LONG).show();
//
//        // This call to disable will make it so the activity below only gets launched the first time a beacon is seen (until the next time the app is launched)
//        // if you want the Activity to launch every single time beacons come into view, remove this call.
//        regionBootstrap.disable();
//        Intent intent = new Intent(this, MainActivity.class);
//        // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
//        // created when a user launches the activity manually and it gets launched from here.
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);
//    }
//
//    @Override
//    public void didExitRegion(Region region) {
//
//    }
//
//    @Override
//    public void didDetermineStateForRegion(int i, Region region) {
//        Toast.makeText(this,region.getUniqueId() ,Toast.LENGTH_LONG).show();
//
//    }
//}
