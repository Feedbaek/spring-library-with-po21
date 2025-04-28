package com.tmax.proobject.minskim2.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Cache 설정을 위한 Properties 클래스
 * application.yml 파일에서 proobject.minskim2.cache 로 시작하는 설정을 매핑합니다.
 *
 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 */
@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "proobject.minskim2.cache")
public class CacheProperties {

    private final String type;
}
