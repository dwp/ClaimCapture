package models.view

/**
 * Creates an object which has access to methods for saving to and getting
 * from the cache. The CachedChangeOfCircs trait implements the cacheKey method required
 * for getting from the cache.
 * See Language (controller) and LanguageSpec (tests) for example
 * usage.
 */
class CacheHandlingWithCircs extends EncryptedCacheHandling with CachedChangeOfCircs
