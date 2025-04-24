package com.tmax.proobject.minskim2.config;

import com.tmax.proobject.minskim2.properties.Minskim2Properties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * minskim2 라이브러리를 사용하기 위한 AutoConfiguration
 * */
@AutoConfiguration
@Import(CacheConfiguration.class)
@EnableConfigurationProperties(Minskim2Properties.class)
public class Minskim2AutoConfiguration {
}
