package org.untrust.ccfreak2k.android.ksms;

import java.io.BufferedReader;
import java.io.FileReader;

import android.os.Build;

public class Ksm {
	
	//private final String ZRAMSTATFILE_DISKSIZE = "/sys/block/zram0/disksize";
	//private final String ZRAMSTATFILE_COMPRESSED_DATA_SIZE = "/sys/block/zram0/compr_data_size";
	//private final String ZRAMSTATFILE_ORIGINAL_DATA_SIZE = "/sys/block/zram0/orig_data_size";
	//private final String ZRAMSTATFILE_MEM_USED_TOTAL = "/sys/block/zram0/mem_used_total";

	private final String KSMSTATFILE_PAGES_TO_SCAN = "/sys/kernel/mm/ksm/pages_to_scan";
	private final String KSMSTATFILE_SLEEP_MILLISECS = "/sys/kernel/mm/ksm/sleep_millisecs";
	private final String KSMSTATFILE_RUN = "/sys/kernel/mm/ksm/run";

	private final String KSMSTATFILE_PAGES_SHARED = "/sys/kernel/mm/ksm/pages_shared";
	private final String KSMSTATFILE_PAGES_SHARING = "/sys/kernel/mm/ksm/pages_sharing";
	private final String KSMSTATFILE_PAGES_UNSHARED = "/sys/kernel/mm/ksm/pages_unshared";
	private final String KSMSTATFILE_PAGES_VOLATILE = "/sys/kernel/mm/ksm/pages_volatile";
	private final String KSMSTATFILE_FULL_SCANS = "/sys/kernel/mm/ksm/full_scans";

	//private int _diskSize = 0;
	//private int _compressedDataSize = 0;
	//private int _originalDataSize = 0;
	//private int _memUsedTotal = 0;

	private int _page_size = 0;
	private int _page_size_kbytes = 0;

	private int _pages_to_scan = 0;
	private int _sleep_millisecs = 0;
	private int _run = 0;

	private int _pages_shared = 0;
	private int _pages_sharing = 0;
	private int _pages_unshared = 0;
	private int _pages_volatile = 0;
	private int _full_scans = 0;

	private float _pages_shared_ratio = 0;
	
	/**
	 * Ksm constructor 
	 */
	public Ksm() {
		super();
		clearCache();
	}

	/**
	 * Read a file an return content of that file as a string
	 * 
	 * Copied from http://stackoverflow.com/a/4867192
	 * 
	 * @param filePath path of file
	 * @return file content as a string 
	 * @throws java.io.IOException
	 */
	private static String readFileAsString(String filePath) throws java.io.IOException {
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    String line, results = "";
	    while( ( line = reader.readLine() ) != null)
	    {
	        results += line;
	    }
	    reader.close();
	    return results;
	}
	
	public void clearCache() { 
		// FIXME: _page_size should be gotten from sysconf(_SC_PAGESIZE)
		// It's 4K on ARM but not everywhere.
		_page_size = 4096;
		_page_size_kbytes = _page_size/1024;

		_pages_to_scan = 0;
		_sleep_millisecs = 0;
		_run = 0;

		_pages_shared = 0;
		_pages_sharing = 0;
		_pages_unshared = 0;
		_pages_volatile = 0;
		_full_scans = 0;

		_pages_shared_ratio = 0;
	}

	/**
	 * Get pages to scan during each interval
	 * @return pages to scan
	 * @throws Exception
	 */
public int getPagesToScan() throws Exception {
	_pages_to_scan = _getPagesToScan();
	return _pages_to_scan;
}

private int _getPagesToScan() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_PAGES_TO_SCAN);
	return Integer.parseInt(sizeInString);
}

	/**
	 * Get interval between page scans
	 * @return
	 * @throws Exception
	 */
public int getSleepMillisecs() throws Exception {
	_sleep_millisecs = _getSleepMillisecs();
	return _sleep_millisecs;
}

private int _getSleepMillisecs() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_SLEEP_MILLISECS);
	return Integer.parseInt(sizeInString);
}

	/**
	 * Get current KSM run state
	 * @return
	 * @throws Exception
	 */
public int getRunState() throws Exception {
	_run = _getRunState();
	return _run;
}

private int _getRunState() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_RUN);
	return Integer.parseInt(sizeInString);
}

//---------------------------

	/**
	 * Gets the number of shared pages
	 * @return
	 * @throws Exception
	 */
public int getPagesShared() throws Exception {
	_pages_shared = _getPagesShared();
	return _pages_shared;
}

private int _getPagesShared() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_PAGES_SHARED);
	return Integer.parseInt(sizeInString);
}


	/**
	 * Gets the total number of pages being shared
	 * @return
	 * @throws Exception
	 */
public int getPagesSharing() throws Exception {
	_pages_sharing = _getPagesSharing();
	return _pages_sharing;
}

private int _getPagesSharing() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_PAGES_SHARING);
	return Integer.parseInt(sizeInString);
}

	/**
	 * Gets number of unique pages repeatedly checked for merging
	 * @return
	 * @throws Exception
	 */
public int getPagesUnshared() throws Exception {
	_pages_unshared = _getPagesUnshared();
	return _pages_unshared;
}

private int _getPagesUnshared() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_PAGES_UNSHARED);
	return Integer.parseInt(sizeInString);
}

	/**
	 * Gets number of pages too volatile to be placed in a tree
	 * @return
	 * @throws Exception
	 */
public int getPagesVolatile() throws Exception {
	_pages_volatile = _getPagesVolatile();
	return _pages_volatile;
}

private int _getPagesVolatile() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_PAGES_VOLATILE);
	return Integer.parseInt(sizeInString);
}

	/**
	 * Gets number of times all mergeable areas have been scanned
	 * @return
	 * @throws Exception
	 */
public int getFullScans() throws Exception {
	_full_scans = _getFullScans();
	return _full_scans;
}

private int _getFullScans() throws Exception {
	String sizeInString = readFileAsString(KSMSTATFILE_FULL_SCANS);
	return Integer.parseInt(sizeInString);
}

	/**
	 * Calculates the total number of pages of memory saved
	 * @return
	 * @throws Exception
	 */
public int getPagesSaved() throws Exception {
	return  _pages_sharing - _pages_shared;
}

	/**
	 * Gets the ratio of shared pages to sharing pages
	 * @return
	 * @throws Exception
	 */
public float getSharedRatio() throws Exception {
	_pages_shared_ratio = _getSharedRatio();
	return _pages_shared_ratio;
}

private float _getSharedRatio() throws Exception {
	return (float)_pages_shared/(float)_pages_sharing;
}

	/**
	 * Returns the number of kbytes in the given number of pages
	 * @param pages number of pages
	 * @return number of kbytes
	 * @throws Exception
	 */
public int getKbytesFromPages(int pages) throws Exception {
	return pages*_page_size_kbytes;
}

//	/**
//	 * Get ZRAM disk size
//	 * @return ZRAM disk size
//	 * @throws Exception
//	 */
//	public int getDiskSize() throws Exception {
//		if (_diskSize == 0) {
//			_diskSize = _getDiskSize();
//		}
//		return _diskSize;
//	}
//	
//	private int _getDiskSize() throws Exception {
//		String sizeInString = readFileAsString(ZRAMSTATFILE_DISKSIZE);
//		return Integer.parseInt(sizeInString);
//	}
//	
//	public int getCompressedDataSize() throws Exception {
//		if (_compressedDataSize == 0) {
//			_compressedDataSize = _getCompressedDataSize();
//		}
//		return _compressedDataSize;
//	}
//	
//	private int _getCompressedDataSize() throws Exception {
//		String sizeInString = readFileAsString(ZRAMSTATFILE_COMPRESSED_DATA_SIZE);
//		return Integer.parseInt(sizeInString);
//	}
//	
//	public int getOriginalDataSize() throws Exception {
//		if (_originalDataSize == 0) {
//			_originalDataSize = _getOriginalDataSize();
//		} 
//		return _originalDataSize;
//	}
//	
//	private int _getOriginalDataSize() throws Exception {
//		String sizeInString = readFileAsString(ZRAMSTATFILE_ORIGINAL_DATA_SIZE);
//		return Integer.parseInt(sizeInString);
//	}
//	
//	
//	public int getMemUsedTotal() throws Exception {
//		if (_memUsedTotal == 0) {
//			_memUsedTotal = _getMemUsedTotal();
//		}
//		return _memUsedTotal;
//	}
//	
//	private int _getMemUsedTotal() throws Exception {
//		String sizeInString = readFileAsString(ZRAMSTATFILE_MEM_USED_TOTAL);
//		return Integer.parseInt(sizeInString);
//	}
//	
//	public float getCompressionRatio() throws Exception {
//		return (float) getCompressedDataSize() / (float) getOriginalDataSize();
//	}
//	
//	public float getUsedRatio() throws Exception {
//		return (float) getOriginalDataSize() / (float) getDiskSize();
// 	}
	
	public String getKernelVersionFromSystemProperty() {
		return System.getProperty("os.version");
	}
	
	public String getKernelVersion() {
		return getKernelVersionFromSystemProperty();
	}
	
	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		} else {
			return manufacturer + " " + model;
		}
	}
}
