package utils

import com.google.common.cache.{Cache => GuavaCache, CacheBuilder}
import java.util.concurrent.Callable
import play.api.Configuration

/**
 * A Scala wrapper for a Google Guava cache. Exposes the basic underlying methods of a Guava cache and adds a
 * getOrElseUpdate(key, value) method that lazily evaluates the value parameter only if the key is not already present
 * in the cache.
 *
 * You may be asking, why not just use Scala's ConcurrentMap interface, which already has a getOrElseUpdate method?
 *
 * val cache = new ConcurrentHashMap().asScala
 * cache.getOrElseUpdate("foo", "bar") // BAD idea
 *
 * The answer is because this method is inherited not from ConcurrentMap but from MapLike, and is NOT a thread safe
 * (atomic) operation!
 *
 * @tparam K
 * @tparam V
 */
class MyCache[K <: AnyRef, V <: AnyRef](initialCapacity: Int = 16, concurrencyLevel: Int = 16) {

  /**
   * Overloaded constructor to create a cache from config values
   *
   * @param config
   * @return
   */
  def this(config: Configuration) = this(
    config.getInt("initialCapacity").getOrElse(16),
    config.getInt("concurrencyLevel").getOrElse(16)
  )

  private val cache: GuavaCache[K, V] = {
    CacheBuilder.newBuilder().build()
//      .newBuilder()
//      .initialCapacity(initialCapacity)
//      .concurrencyLevel(concurrencyLevel)
//      .build()
  }

  /**
   * Optionally get the value associated with the given key
   *
   * @param key
   * @return
   */
  def get(key: K): Option[V] = {
    Option(cache.getIfPresent(key))
  }

  /**
   * Get the value associated with the given key. If no value is already associated, then associate the given value
   * with the key and use it as the return value.
   *
   * Like Scala's ConcurrentMap, the value parameter will be lazily evaluated. However, unlike Scala's ConcurrentMap,
   * this method is a thread safe (atomic) operation.
   *
   * @param key
   * @param value
   * @return
   */
  def getOrElseUpdate(key: K, value: => V): V = {
    cache.get(key, new Callable[V] {
      def call(): V = value
    })
  }

  /**
   * Associate the given value with the given key
   *
   * @param key
   * @param value
   */
  def put(key: K, value: V) {
    cache.put(key, value)
  }

  /**
   * Remove the key and any associated value from the cache
   *
   * @param key
   */
  def remove(key: K) {
    cache.invalidate(key)
  }

  /**
   * Remove all keys and values from the cache
   */
  def clear() {
    cache.invalidateAll()
  }

  /**
   * Return how many items are in the cache
   *
   * @return
   */
  def size: Long = {
    cache.size()
  }

}

//object Cache {
//  val DEFAULT_INITIAL_CAPACITY = 16
//  val DEFAULT_CONCURRENCY_LEVEL = 16
//
//  val CONFIG_KEY_INITIAL_CAPACITY = "initialCapacity"
//  val CONFIG_KEY_CONCURRENCY_LEVEL = "concurrencyLevel"
//}