package org.springframework.samples.petclinic.remote

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.util.*


@Service
class HotelClient(
    @Qualifier("pet-hotel-client") private val client: RestClient,
    circuitBreakerRegistry: CircuitBreakerRegistry
) {
    private val circuitBreaker = circuitBreakerRegistry.circuitBreaker("pet-hotel-client")

    @Suppress("Code Coverage")
    fun requestRoomForPet(date: Date, petId: Int): RoomReservationResponse {
        return circuitBreaker.decorateCallable {
            client.post()
                .uri("/room-reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .body(RoomReservationRequest(date, petId))
                .retrieve()
                .body(RoomReservationResponse::class.java)
        }.call() ?: throw IllegalStateException("Missing body")
    }

    @Suppress("unused", "Code Coverage")
    fun requestRoomForPetWithoutCircuitBreaker(date: Date, petId: Int): RoomReservationResponse {
        return client.post()
            .uri("/room-reservation")
            .contentType(MediaType.APPLICATION_JSON)
            .body(RoomReservationRequest(date, petId))
            .retrieve()
            .body(RoomReservationResponse::class.java)
            ?: throw IllegalStateException("Missing body")
    }
}

data class RoomReservationRequest(val date: Date, val petId: Int)
data class RoomReservationResponse(val available: Boolean)
