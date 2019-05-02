/**   
 * 
 * ObjectPool: 用于存放对象的一个共享池，commons-pool2用双端阻塞队列LinkedBlockingDeque保存空闲对象, 用ConcurrentHashMap保存所有的池对象。
 *  GenericObjectPool继承BaseGenericObjectPool实现ObjectPool，通常用此实现来作为默认的共享池。
 *  GenericKeyedObjectPool 类似于GenericObjectPool，每个key对应一个GenericObjectPool。它用于区别不同类型的对象。
 *  
 * 
 * PooledObjectFactory: 对象池工厂，用于产生一个新的池对象。
 * 
 *  PooledObject<T> makeObject() throws Exception;
 *  - 生成新对象
 *  a. 初始化时ObjectPool时，调用生成新对象，以达到池中的最小对象数
 *  b. 在evict完过期对象后，池中的对象数小于最小对象数时，调用生成新对象
 *  c. ObjectPool.borrow()时，如果池中没有空闲对象，而且对象总数小于最大对象数，调用生成新对象
 *  
 *  void destroyObject(PooledObject<T> p) throws Exception;
 *  - 销毁一个池对象，实现这个方法需要考虑到处理异常情况。
 *  
 *  boolean validateObject(PooledObject<T> p);
 *  - 用于校验一个池对象是否可用，比如在borrow或者return时，调用该方法检测对象是否可用。需要注意的是校验方法只会作用于激活的对象实例上。
 *  - 通常的做法是在对象空闲的时候进行校验，而不是在使用的时候进行校验，因为这样会影响性能。
 *  
 *  void activateObject(PooledObject<T> p) throws Exception;
 *  - 激活一个对象，在生成新对象或者从对象池中borrow被钝化过的对象时调用该方法。
 *  
 *  void passivateObject(PooledObject<T> p) throws Exception;
 *  - 钝化一个对象。在向对象池归还一个对象是会调用这个方法。
 *  
 *  
 * PooledObject: 对象池里面存放的对象
 *  DefaultPooledObject 实现了PooledObject，默认的池对象包装器用于跟踪其他信息，例如状态。
 * 
 *   
 * BaseObjectPoolConfig: 参数配置抽象类，用于自定义对象池参数。 
 * GenericObjectPoolConfig extends BaseObjectPoolConfig 对象池的通用配置实现。
 * - boolean lifo           对象池存储空闲对象是使用的LinkedBlockingDeque，如果为true，表示使用FIFO获取对象。 
 * - boolean fairness       common-pool2双向阻塞队列使用的是Lock锁。这个参数表示是否使用lock的公平锁。默认值是false。 
 * 
 * - long maxWaitMillis     当没有空闲对象时，获取一个对象的最大等待时间。小于0，则永不超时，一直等待，直到有空闲对象到来。
 *                        - 如果大于0，则等待maxWaitMillis长时间，如果没有空闲对象，将抛出NoSuchElementException异常。默认值是-1。
 * - boolean blockWhenExhausted 当对象池没有空闲对象时，获取对象的请求是否阻塞。true阻塞。默认值是true
 *      
 * - long minEvictableIdleTimeMillis    空闲检测时，当空闲的时间大于这个值，执行移除这个对象操作。默认值是1000L * 60L * 30L(30分钟)。
 * - long softMinEvictableIdleTimeMillis空闲检测时，当空闲的时间大于这个值，并且当前空闲对象的数量大于最小空闲数量(minIdle)时，执行移除操作。
 *                                    - 区别是，它会保留最小的空闲对象数量。而上面的不会，是强制性移除的。默认值是-1（Long的最大值）。
 * - long evictorShutdownTimeoutMillis  当创建驱逐线(evictor)程时，如果发现已有一个evictor正在运行则会停止该evictor，
 *                                      evictorShutdownTimeoutMillis表示当前线程需等待多长时间让ScheduledThreadPoolExecutor停止该evictor线程。
 *                                      
 * - long timeBetweenEvictionRunsMillis 空闲对象检测线程的执行周期，即多长时候执行一次空闲对象检测。单位是毫秒数。如果小于等于0，则不执行检测线程。默认值是-1
 * - int numTestsPerEvictionRun         检测空闲对象线程每次检测的空闲对象的数量。默认值是3
 * 
 * - boolean testOnCreate           在创建对象时检测对象是否有效，默认值是false。做了这个配置会降低性能。 
 * - boolean testOnBorrow           在从对象池获取对象时是否检测对象有效，默认值是false。做了这个配置会降低性能。 
 * - boolean testOnReturn           在向对象池中归还对象时是否检测对象有效，默认值是false。做了这个配置会降低性能。 
 * - boolean testWhileIdle          在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性。默认值是false。建议配置为true，不影响性能，并且保证安全性。 
 * 
 * - int maxTotal   对象池中管理的最多对象个数。默认值是8。 
 * - int maxIdle    对象池中最大的空闲对象个数。默认值是8。 
 * - int minIdle    对象池中最小的空闲对象个数。默认值是0。
 * 
 * 
 * - 要实现一个连接池首先需要3个基本的类，PooledObjec池中对象，PooledObjectFactory对象工厂，ObjectPool对象池。
 * - 由于ObjectPool缓存的是一个对象的包装类型即PooledObject，所以在PooledObjectFactory获得对象的时候需将实际对象进行包装。
 * 
 * 
 * 
 * @author chenzq  
 * @date 2019年5月2日 下午12:42:03
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
package com.asiainfo.dbcp;
