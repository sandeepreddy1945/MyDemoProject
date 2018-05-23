/**
 * 
 */
package com.app.chart.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class ChartCacheManager {

	// TODO to work on this cache management.
	public static void createCacheInitializer() {
		CacheManager cacheManager = CacheManager.create();
		String[] cacheNames = CacheManager.getInstance().getCacheNames();

		Cache cache = cacheManager.getCache("");
	}
}
