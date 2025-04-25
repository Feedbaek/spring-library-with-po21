package com.tmax.proobject.minskim2.config;

import com.tmax.proobject.minskim2.properties.Minskim2Properties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * minskim2 라이브러리를 사용하기 위한 AutoConfiguration

 * @since 2025.04.25
 * @version 1.0
 * @author 김민석G (minskim2)
 * */
@AutoConfiguration
@EnableConfigurationProperties(Minskim2Properties.class)
public class Minskim2AutoConfiguration {
}
