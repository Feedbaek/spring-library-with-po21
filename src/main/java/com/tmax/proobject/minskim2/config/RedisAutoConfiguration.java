package com.tmax.proobject.minskim2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.proobject.minskim2.properties.RedisProperties;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * Redis 설정을 자동 구성하기 위한 클래스
 * Redis cluster, Redis standalone 구성으로 LettuceConnectionFactory 설정을 자동으로 구성한다.
 * Redisson 사용을 위한 RedissonClient 빈도 등록한다.
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

    private final RedisProperties redisProperties;

    /**
     * Redis Cluster 연결을 위해 LettuceConnectionFactory 자동 구성
     * */
    @Bean
    @ConditionalOnMissingBean(LettuceConnectionFactory.class)
    @ConditionalOnClass(LettuceConnectionFactory.class)
    @ConditionalOnProperty(prefix = "proobject.minskim2.redis", name = "type", havingValue = "cluster")
    public LettuceConnectionFactory lettuceClusterConnectionFactory() {
        log.info("Creating LettuceConnectionFactory for Redis Cluster");
        // 1) ClusterTopologyRefreshOptions 설정
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enableAdaptiveRefreshTrigger()
                .enablePeriodicRefresh(Duration.ofSeconds(30)) // 30초 주기로 클러스터 토폴로지 갱신
                .build();

        // 2) Socket Options 설정
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        // 3) 클라이언트 옵션 설정
        ClusterClientOptions clientOptions = ClusterClientOptions.builder()
                .autoReconnect(true)  // 자동 재연결
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .socketOptions(socketOptions)
                .build();

        // 4) LettuceClientConfiguration 설정
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2))       // 커맨드 타임아웃
                .shutdownTimeout(Duration.ofMillis(100))     // shutdown 타임아웃
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .clientOptions(clientOptions)
                .build();

        // 5) Redis Cluster Configuration 설정
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getNodes());

        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(LettuceConnectionFactory.class)
    @ConditionalOnClass(LettuceConnectionFactory.class)
    @ConditionalOnProperty(prefix = "proobject.minskim2.redis", name = "type", havingValue = "standalone")
    public LettuceConnectionFactory lettuceStandaloneConnectionFactory() {
        log.info("Creating LettuceConnectionFactory for Redis Standalone");
        String[] nodeInfo = redisProperties.getNodes().get(0).split(":");
        String host = nodeInfo[0];
        int port = Integer.parseInt(nodeInfo[1]);

        // 1) 단일 서버 정보 설정
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration();
        serverConfig.setHostName(host);      // Redis 호스트
        serverConfig.setPort(port);                 // Redis 포트

        // 2) 클라이언트(lettuce) 옵션 설정 (pool, timeout 등)
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2))       // 커맨드 타임아웃
                .shutdownTimeout(Duration.ofMillis(100))     // shutdown 타임아웃
                .build();

        // 3) LettuceConnectionFactory 생성
        return new LettuceConnectionFactory(serverConfig, clientConfig);
    }

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    @ConditionalOnClass(Redisson.class)
    public RedissonClient redissonClient() {
        log.info("Creating RedissonClient");
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisProperties.getNodes().get(0));

        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    @ConditionalOnMissingBean(RedisSerializer.class)
    @ConditionalOnClass(RedisConnectionFactory.class)
    public RedisSerializer<Object> redisSerializer(ObjectMapper objectMapper) {
        log.info("Creating RedisSerializer");
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
