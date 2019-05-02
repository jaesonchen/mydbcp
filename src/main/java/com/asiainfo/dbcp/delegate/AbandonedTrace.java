package com.asiainfo.dbcp.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: Trace, 用于缓存连接打开的statement、resultset，以便在连接失效时关闭已打开的statement、resultset
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午9:04:48
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class AbandonedTrace {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// 保存Connection的所有trace信息
	private final List<AbandonedTrace> traceList = new ArrayList<AbandonedTrace>();
    private final Lock lock = new ReentrantLock();
    private long lastUsed = 0;
    
    protected long getLastUsed() {
		return lastUsed;
	}
	
	protected void setLastUsed() {
	    setLastUsed(System.currentTimeMillis());
	}
	
	protected void setLastUsed(long lastUsed) {
		this.lastUsed = lastUsed;
		logger.debug("setLastUsed={} ......", this.lastUsed);
	}
	
	/**
	 * @Description: 返回所有的trace记录
	 * @author chenzq
	 * @date 2019年5月1日 下午9:10:40
	 * @return
	 */
    protected List<AbandonedTrace> getTrace() {
        lock.lock();
        try {
            return new ArrayList<AbandonedTrace>(traceList);
        } finally {
            lock.unlock();
        }
    }
	
    /**
     * @Description: 增加一个trace记录
     * @author chenzq
     * @date 2019年5月1日 下午9:12:41
     * @param trace
     */
    protected void addTrace(AbandonedTrace trace) {
    	logger.debug("addTrace={} ......", trace);
        lock.lock();
    	try {
            this.traceList.add(trace);
            setLastUsed();
        } finally {
            lock.unlock();
        }
    }
	
    /**
     * @Description: 删除一个trace
     * @author chenzq
     * @date 2019年5月1日 下午9:13:00
     * @param trace
     */
    protected void removeTrace(AbandonedTrace trace) {
    	logger.debug("removeTrace={} ......", trace);
    	lock.lock();
        try {
            this.traceList.remove(trace);
        } finally {
            lock.unlock();
        }
    }
	
    /**
     * @Description: 清空所有trace记录
     * @author chenzq
     * @date 2019年5月1日 下午9:13:13
     */
	protected void clearTrace() {
		logger.debug("clearTrace ......");
		lock.lock();
        try {
            this.traceList.clear();
        } finally {
            lock.unlock();
        }
    }
}
