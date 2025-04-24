package com.tmax.proobject.minskim2.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "proobject.minskim2.cache")
public class CacheProperties {

    private String type;
}
