package org.springframework.samples.petclinic.remote

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient


@Configuration
class RestClientFactory {
    @Bean(name = ["pet-hotel-client"])
    fun restClient(): RestClient {
        return RestClient.create("http://pet-hotel.com")
    }
}

