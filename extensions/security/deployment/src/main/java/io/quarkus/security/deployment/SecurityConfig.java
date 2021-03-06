package io.quarkus.security.deployment;

import java.util.List;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

/**
 *
 */
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public final class SecurityConfig {

    /**
     * List of security providers to enable for reflection
     */
    @ConfigItem
    public List<String> securityProviders;
}
