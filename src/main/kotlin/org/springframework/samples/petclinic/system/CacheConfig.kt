/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system


import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import javax.cache.configuration.Configuration
import javax.cache.configuration.MutableConfiguration

/**
 * Cache could be disabled in unit test.
 *
 * @author Antoine Rey
 * @author Stephane Nicoll
 */
@org.springframework.context.annotation.Configuration
@EnableCaching
@Profile("production")
class CacheConfig {

    @Bean
    fun cacheManagerCustomizer(): JCacheManagerCustomizer {
        return JCacheManagerCustomizer {
            it.createCache("vets", createCacheConfiguration())
        }
    }

    private fun createCacheConfiguration(): Configuration<Any, Any> =
            // Create a cache using infinite heap. A real application will want to use an
            // implementation dependent configuration that will better fit your needs
            MutableConfiguration<Any, Any>().setStatisticsEnabled(true)
}
