package com.xyjsoft.core.config;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

/**
 * @Auther: whw
 * @Date: 2019/8/7
 * @Description: redis配置-springboot2.x其中使用的是Lettuce- 例子： (1)第一种使用
 * @Resource private StringRedisTemplate stringRedisTemplate; (2)第二种自定义模板使用
 * @Resource private RedisTemplate<String, Object> redisTemplate; Lettuce 和
 *           Jedis 的都是连接Redis Server的客户端程序。 Jedis在实现上是直连redis
 *           server，多线程环境下非线程安全，除非使用连接池，为每个Jedis实例增加物理连接。
 *           Lettuce基于Netty的连接实例（StatefulRedisConnection），可以在多个线程间并发访问，且线程安全，满足多线程环境下的并发访问，
 *           同时它是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching // 开启缓存支持
public class RedisConfig extends CachingConfigurerSupport {
	@Resource
	private LettuceConnectionFactory lettuceConnectionFactory;

	// 缓存管理器
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))// key序列化方式
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))// value序列化方式
				.disableCachingNullValues().entryTtl(Duration.ofSeconds(30 * 60));// 缓存过期时间

		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(lettuceConnectionFactory).cacheDefaults(config).transactionAware()
				.withInitialCacheConfigurations(getRedisCacheConfigurationMap());

		return builder.build();
	}

	private RedisSerializer<String> keySerializer() {
		return new StringRedisSerializer();
	}

	private RedisSerializer<Object> valueSerializer() {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		jackson2JsonRedisSerializer.setObjectMapper(om);
		return jackson2JsonRedisSerializer;

		// 设置序列化 两种方式区别不大
	    // return new GenericJackson2JsonRedisSerializer();
	}

	private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
		/**
		 * @CacheConfig(cacheNames = "SsoCache") 
		 * public class SsoCache{
		 * 		@Cacheable(keyGenerator = "cacheKeyGenerator")
		 *      public String getTokenByGsid(String gsid)
		 *      
		 *      //二者选其一,可以使用value上的信息，来替换类上cacheNames的信息
		 *      @Cacheable(value = "BasicDataCache",keyGenerator = "cacheKeyGenerator")
		 *      public String getTokenByGsid(String gsid)
		 * }
		 * 
		 */
		// SsoCache和BasicDataCache进行过期时间配置
		/*redisCacheConfigurationMap.put("menuCache", this.getRedisCacheConfigurationWithTtl(24 * 60 * 60));
		redisCacheConfigurationMap.put("BasicDataCache", this.getRedisCacheConfigurationWithTtl(30 * 60));*/
		return redisCacheConfigurationMap;
	}

	private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
		redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofSeconds(seconds))
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))// key序列化方式
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))// value序列化方式;
		;
		return redisCacheConfiguration;
	}

	@Bean(name = "cacheKeyGenerator")
	public KeyGenerator cacheKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {

				StringBuffer sb = new StringBuffer();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	/**
	 * RedisTemplate配置
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		// 设置序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		om.enableDefaultTyping(DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		// 配置redisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		RedisSerializer<?> stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);// key序列化
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
		redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
