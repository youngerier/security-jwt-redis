package com.ziggle.authclient;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class SecurityJwtConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                SecurityJwtConfig.class.getName(),
                SecurityJwtTokenDecoder.class.getName()
        };
    }
}
