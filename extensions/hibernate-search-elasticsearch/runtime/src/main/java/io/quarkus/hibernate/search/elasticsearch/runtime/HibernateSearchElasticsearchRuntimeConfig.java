package io.quarkus.hibernate.search.elasticsearch.runtime;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.search.backend.elasticsearch.index.IndexLifecycleStrategyName;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.mapper.orm.automaticindexing.AutomaticIndexingSynchronizationStrategyName;
import org.hibernate.search.mapper.orm.search.loading.EntityLoadingCacheLookupStrategy;

import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "hibernate-search.elasticsearch", phase = ConfigPhase.RUN_TIME)
public class HibernateSearchElasticsearchRuntimeConfig {

    /**
     * Configuration of the default backend.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    ElasticsearchBackendRuntimeConfig defaultBackend;

    /**
     * Additional backends
     */
    @ConfigItem(name = "backends")
    @ConfigDocSection
    @ConfigDocMapKey("backend-name")
    Map<String, ElasticsearchBackendRuntimeConfig> additionalBackends;

    /**
     * Configuration for how entities are loaded by a search query.
     */
    @ConfigItem(name = "query.loading")
    SearchQueryLoadingConfig queryLoading;

    /**
     * Configuration for the automatic indexing.
     */
    @ConfigItem
    AutomaticIndexingConfig automaticIndexing;

    @ConfigGroup
    public static class ElasticsearchBackendRuntimeConfig {
        /**
         * The list of hosts of the Elasticsearch servers.
         */
        @ConfigItem(defaultValue = "http://localhost:9200")
        List<String> hosts;

        /**
         * The username used for authentication.
         */
        @ConfigItem
        Optional<String> username;

        /**
         * The password used for authentication.
         */
        @ConfigItem
        Optional<String> password;

        /**
         * The connection timeout.
         */
        @ConfigItem(defaultValue = "3S")
        Duration connectionTimeout;

        /**
         * The maximum number of connections to all the Elasticsearch servers.
         */
        @ConfigItem(defaultValue = "20")
        int maxConnections;

        /**
         * The maximum number of connections per Elasticsearch server.
         */
        @ConfigItem(defaultValue = "10")
        int maxConnectionsPerRoute;

        /**
         * Configuration for the automatic discovery of new Elasticsearch nodes.
         */
        @ConfigItem
        DiscoveryConfig discovery;

        /**
         * The default configuration for the Elasticsearch indexes.
         */
        @ConfigItem
        ElasticsearchIndexConfig indexDefaults;

        /**
         * Per-index specific configuration.
         */
        @ConfigItem
        @ConfigDocMapKey("index-name")
        Map<String, ElasticsearchIndexConfig> indexes;
    }

    @ConfigGroup
    public static class ElasticsearchIndexConfig {
        /**
         * Configuration for the lifecyle of the indexes.
         */
        @ConfigItem
        LifecycleConfig lifecycle;
    }

    @ConfigGroup
    public static class DiscoveryConfig {

        /**
         * Defines if automatic discovery is enabled.
         */
        @ConfigItem(defaultValue = "false")
        boolean enabled;

        /**
         * Refresh interval of the node list.
         */
        @ConfigItem(defaultValue = "10S")
        Duration refreshInterval;

        /**
         * The scheme that should be used for the new nodes discovered.
         */
        @ConfigItem(defaultValue = "http")
        String defaultScheme;
    }

    @ConfigGroup
    public static class AutomaticIndexingConfig {

        /**
         * Configuration for synchronization with the index when indexing automatically.
         */
        @ConfigItem
        AutomaticIndexingSynchronizationConfig synchronization;

        /**
         * Whether to check if dirty properties are relevant to indexing before actually reindexing an entity.
         * <p>
         * When enabled, re-indexing of an entity is skipped if the only changes are on properties that are not used when
         * indexing.
         */
        @ConfigItem(defaultValue = "true")
        boolean enableDirtyCheck;
    }

    @ConfigGroup
    public static class AutomaticIndexingSynchronizationConfig {

        /**
         * The synchronization strategy to use when indexing automatically.
         * <p>
         * Defines the status for which you wait before considering the operation completed by Hibernate Search.
         * <p>
         * Use {@code queued} or {@code committed} in production environments.
         * {@code searchable} is useful in integration tests.
         */
        @ConfigItem(defaultValue = "committed")
        AutomaticIndexingSynchronizationStrategyName strategy;
    }

    @ConfigGroup
    public static class SearchQueryLoadingConfig {

        /**
         * Configuration for cache lookup when loading entities during the execution of a search query.
         */
        @ConfigItem
        SearchQueryLoadingCacheLookupConfig cacheLookup;

        /**
         * The fetch size to use when loading entities during the execution of a search query.
         */
        @ConfigItem(defaultValue = "100")
        int fetchSize;
    }

    @ConfigGroup
    public static class SearchQueryLoadingCacheLookupConfig {

        /**
         * The strategy to use when loading entities during the execution of a search query.
         */
        @ConfigItem(defaultValue = "skip")
        EntityLoadingCacheLookupStrategy strategy;
    }

    // We can't set actual default values in this section,
    // otherwise "quarkus.hibernate-search.elasticsearch.index-defaults" will be ignored.
    @ConfigGroup
    public static class LifecycleConfig {

        /**
         * The strategy used for index lifecycle.
         */
        // We can't set an actual default value here: see comment on this class.
        @ConfigItem(defaultValueDocumentation = "create")
        Optional<IndexLifecycleStrategyName> strategy;

        /**
         * The minimal cluster status required.
         */
        // We can't set an actual default value here: see comment on this class.
        @ConfigItem(defaultValueDocumentation = "green")
        Optional<IndexStatus> requiredStatus;

        /**
         * How long we should wait for the status before failing the bootstrap.
         */
        // We can't set an actual default value here: see comment on this class.
        @ConfigItem(defaultValueDocumentation = "10S")
        Optional<Duration> requiredStatusWaitTimeout;
    }
}
