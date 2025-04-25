package com.tmax.proobject.minskim2.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

/**
 * Redis 설정을 위한 Properties 클래스
 * application.yml 파일에서 proobject.minskim2.redis 로 시작하는 설정을 매핑합니다.
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "proobject.minskim2.redis")
public class RedisProperties {

    private String type;
    private List<String> nodes;
}
