/**
 * 
 */
package com.app.chart.cache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * @author Sandeep Reddy Battula
 *
 */
@Slf4j
public class ChartCacheManager {

	public static final String MPS_CHART_CACHE = "MPSChartCache";
	private static ChartCacheManager _instance = new ChartCacheManager();
	private CacheManager cacheManager;
	private Cache mpsChartCache;

	/**
	 * 
	 */
	private ChartCacheManager() {
		// initialize the cache and put to singleton.
		createCacheInitializer();
	}

	public static ChartCacheManager getInstance() {
		return _instance;
	}

	// TODO to work on this cache management and put this to main class call..
	public void createCacheInitializer() {
		log.info("Cache Initailzed with cache name as MPSChartCache");
		cacheManager = CacheManager.create();
		mpsChartCache = new Cache(new CacheConfiguration(MPS_CHART_CACHE, 1000000000)
				.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU).eternal(true)
				.diskExpiryThreadIntervalSeconds(0)
				.persistence(new PersistenceConfiguration().strategy(Strategy.LOCALTEMPSWAP)));
		CacheConfiguration cacheConfiguration = mpsChartCache.getCacheConfiguration();
		// store less on heap and more on disk to reduce tension in heap as the app
		// occupies heap.
		cacheConfiguration.setMaxEntriesLocalHeap(999);
		cacheConfiguration.setMaxEntriesLocalDisk(999999999);

		// add the cache to cache manager
		cacheManager.addCache(mpsChartCache);

		// Now on Store all the items to this cache .To ensure maximum productivity and
		// less system files hit.

	}

	public void putAppUsedImagesToCache() {

	}

	/**
	 * @return the cacheManager
	 */
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * @return the mpsChartCache
	 */
	public Cache getMpsChartCache() {
		return mpsChartCache;
	}

	public void shutDownCacheManager() {
		cacheManager.shutdown();
	}
}
