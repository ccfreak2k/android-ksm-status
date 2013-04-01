package org.untrust.ccfreak2k.android.ksms;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;
import org.untrust.ccfreak2k.android.ksms.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Ksm ksm;
	private Timer recalculateTimer;
	private final int recalculateTimerInterval = 5 * 1000;
	private int tmpval = 0; // temporary value used below...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ksm = new Ksm();
        
        updateDeviceInfo();
        recalculateTimerSchedule();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_reload:
    		recalculate();
    		return true;
    	case R.id.action_about:
    		Intent aboutIntent = new Intent(this, About.class);
    		startActivity(aboutIntent);
    		return true;
    	case R.id.action_share:
    		Intent shareIntent = new Intent();
    		shareIntent.setAction(Intent.ACTION_SEND);
    		shareIntent.putExtra(
    				Intent.EXTRA_TEXT, 
    				"Check out \"" 
    				+ getString(R.string.app_name) 
    				+ "\" - https://play.google.com/store/apps/details?id=id.co.ptskp.android.zs"
    			);
    		shareIntent.setType("text/plain");
    		startActivity(shareIntent);
    		return true;
    	case R.id.action_exit:
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    private void recalculateTimerSchedule() {
    	recalculateTimer = new Timer();
    	recalculateTimer.schedule(
    			new TimerTask() {
					
					@Override
					public void run() {
						recalculateTimerMethod();
					}
				}, 
    			0, recalculateTimerInterval);
    }
    
    private void recalculateTimerMethod() {
    	runOnUiThread(RecalculateTimerTick);
    }
    
    private Runnable RecalculateTimerTick = new Runnable() {
		
		@Override
		public void run() {
			recalculate();
		}
	};
    
    private void recalculate() {
    	ksm.clearCache();
    	
    	try {
    		NumberFormat nf = NumberFormat.getNumberInstance();

    	    TextView tvksmPagesToScan = (TextView) findViewById(R.id.ksm_pages_to_scan);
    	    tvksmPagesToScan.setText(
    	    	getResources().getString(R.string.ksm_pages_to_scan)
    	    	+ " "
    	    	+ nf.format(ksm.getPagesToScan())
    	    );

    	    TextView tvksmSleepMillisecs = (TextView) findViewById(R.id.ksm_sleep_millisecs);
    	    tvksmSleepMillisecs.setText(
    	    	getResources().getString(R.string.ksm_sleep_millisecs)
    	    	+ " "
    	    	+ nf.format(ksm.getSleepMillisecs())
    	    );

    	    TextView tvksmRun = (TextView) findViewById(R.id.ksm_run);
    	    tvksmRun.setText(
    	    	getResources().getString(R.string.ksm_run)
    	    	+ " "
    	    	+ nf.format(ksm.getRunState())
    	    );

//----------------------------------------------------

    	    tmpval = ksm.getPagesShared();
    	    TextView tvksmPagesShared = (TextView) findViewById(R.id.ksm_pages_shared);
    	    tvksmPagesShared.setText(
    	    	getResources().getString(R.string.ksm_pages_shared)
    	    	+ " "
    	    	+ nf.format(tmpval)
    	    	+ " ("
    	    	+ nf.format(ksm.getKbytesFromPages(tmpval))
    	    	+ "K)"
    	    );

    	    tmpval = ksm.getPagesSharing();
    	    TextView tvksmPagesSharing = (TextView) findViewById(R.id.ksm_pages_sharing);
    	    tvksmPagesSharing.setText(
    	    	getResources().getString(R.string.ksm_pages_sharing)
    	    	+ " "
    	    	+ nf.format(tmpval)
    	    	+ " ("
    	    	+ nf.format(ksm.getKbytesFromPages(tmpval))
    	    	+ "K)"
    	    );

    	    tmpval = ksm.getPagesUnshared();
    	    TextView tvksmPagesUnshared = (TextView) findViewById(R.id.ksm_pages_unshared);
    	    tvksmPagesUnshared.setText(
    	    	getResources().getString(R.string.ksm_pages_unshared)
    	    	+ " "
    	    	+ nf.format(tmpval)
    	    	+ " ("
    	    	+ nf.format(ksm.getKbytesFromPages(tmpval))
    	    	+ "K)"
    	    );

    	    tmpval = ksm.getPagesVolatile();
    	    TextView tvksmPagesVolatile = (TextView) findViewById(R.id.ksm_pages_volatile);
    	    tvksmPagesVolatile.setText(
    	    	getResources().getString(R.string.ksm_pages_volatile)
    	    	+ " "
    	    	+ nf.format(tmpval)
    	    	+ " ("
    	    	+ nf.format(ksm.getKbytesFromPages(tmpval))
    	    	+ "K)"
    	    );

    	    TextView tvksmFullScans = (TextView) findViewById(R.id.ksm_full_scans);
    	    tvksmFullScans.setText(
    	    	getResources().getString(R.string.ksm_full_scans)
    	    	+ " "
    	    	+ nf.format(ksm.getFullScans())
    	    );

    	    tmpval = ksm.getPagesSaved();
    	    TextView tvksmPagesSaved = (TextView) findViewById(R.id.ksm_pages_saved);
    	    tvksmPagesSaved.setText(
    	    	getResources().getString(R.string.ksm_pages_saved)
    	    	+ " "
    	    	+ nf.format(tmpval)
    	    	+ " ("
    	    	+ nf.format(ksm.getKbytesFromPages(tmpval))
    	    	+ "K)"
    	    );

//----------------------------------------------------

    	    final int sharedRatio = Math.round(ksm.getSharedRatio() * 100);
    	    ProgressBar pbksmPagesSharedRatio = (ProgressBar) findViewById(R.id.ksm_pages_shared_ratio_progress_bar);
    	    pbksmPagesSharedRatio.setProgress(sharedRatio);

    	    TextView tvksmPagesSharedRatio = (TextView) findViewById(R.id.ksm_pages_shared_ratio);
    	    tvksmPagesSharedRatio.setText(
    	    	getResources().getString(R.string.ksm_pages_shared_ratio)
    	    	+ " "
    	    	+ nf.format(ksm.getSharedRatio())
    	    );

//    		TextView tvksmDiskSize = (TextView) findViewById(R.id.ksm_disk_size);
//    		tvksmDiskSize.setText(
//        			getResources().getString(R.string.ksm_disk_size) 
//        			+ " "
//        			+ nf.format(ksm.getDiskSize())
//        			+ " bytes"
//    			);
//    		
//    		TextView tvksmCompressedDataSize = (TextView) findViewById(R.id.ksm_compressed_data_size);
//    		tvksmCompressedDataSize.setText(
//        			getResources().getString(R.string.ksm_compressed_data_size) 
//        			+ " "
//        			+ nf.format(ksm.getCompressedDataSize())
//        			+ " bytes"
//    			);
//    		
//    		TextView tvksmOriginalDataSize = (TextView) findViewById(R.id.ksm_original_data_size);
//    		tvksmOriginalDataSize.setText(
//        			getResources().getString(R.string.ksm_original_data_size) 
//        			+ " "
//        			+ nf.format(ksm.getOriginalDataSize())
//        			+ " bytes"
//    			);
//    		
//    		TextView tvksmMemUsedTotal = (TextView) findViewById(R.id.ksm_mem_used_total);
//    		tvksmMemUsedTotal.setText(
//        			getResources().getString(R.string.ksm_mem_used_total) 
//        			+ " "
//        			+ nf.format(ksm.getMemUsedTotal())
//        			+ " bytes"
//    			);
//    		
//    		final int compressionRatio = Math.round(ksm.getCompressionRatio() * 100);
//    		ProgressBar pbksmCompressionRatio = (ProgressBar) findViewById(R.id.ksm_compression_ratio_bar);
//    		pbksmCompressionRatio.setProgress(compressionRatio);
//    		
//    		TextView tvksmCompressionRatio = (TextView) findViewById(R.id.ksm_compression_ratio);
//    		tvksmCompressionRatio.setText(
//        			getResources().getString(R.string.ksm_compression_ratio) 
//        			+ " "
//        			+ Integer.toString(compressionRatio)
//        			+ "%"
//    			);
//    		
//    		final int usedRatio = Math.round(ksm.getUsedRatio() * 100);
//    		ProgressBar pbksmUsedRatio = (ProgressBar) findViewById(R.id.ksm_used_ratio_bar);
//    		pbksmUsedRatio.setProgress(usedRatio);
//    		
//    		TextView tvksmUsedRatio = (TextView) findViewById(R.id.ksm_used_ratio);
//    		tvksmUsedRatio.setText(
//        			getResources().getString(R.string.ksm_used_ratio) 
//        			+ " "
//        			+ Integer.toString(usedRatio)
//        			+ "%"
//    			);
    		
    		MemoryInfo mi = new MemoryInfo();
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            
            //long memoryTotal = mi.totalMem;
            long memoryAvail = mi.availMem;
            //long memoryUsed = memoryTotal - memoryAvail;

            TextView tvRamAvailable = (TextView) findViewById(R.id.ram_available);
            tvRamAvailable.setText(
            		getString(R.string.ram_available)
            		+ " " + nf.format(memoryAvail)
            		+ " bytes ("
            		+ nf.format((double)((double)memoryAvail/1048576))
            		+ "MB)"
            	);
            
    	} catch (Exception e) {
    		Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    		finish();
    	}
    	
    }
    
    private void updateDeviceInfo() {
    	TextView tvDeviceInfo = (TextView) findViewById(R.id.device_info);
        tvDeviceInfo.setText(
        		ksm.getDeviceName()
        		+ " - " + Build.DISPLAY
        	);
         
        TextView tvKernelVersion = (TextView) findViewById(R.id.kernel_version);
        tvKernelVersion.setText(
        		getString(R.string.text_kernel)
        		+ " " + ksm.getKernelVersion()
        	);
    }
    
}
