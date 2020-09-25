package nu.westlin.stockticker.client

import kotlinx.coroutines.flow.Flow
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux
import java.time.LocalDateTime

class Client(private val webClient: WebClient) {
    fun pricesFor(symbol: String): Flux<StockPrice> {
        return webClient.get()
            .uri("http://localhost:8080/stock/{symbol}", symbol)
            .retrieve()
            .bodyToFlux()
        
    }

    fun pricesFor2(symbol: String): Flow<StockPrice> {
        return webClient.get()
            .uri("http://localhost:8080/stock/{symbol}", symbol)
            .retrieve()
            .bodyToFlow()

    }
}

data class StockPrice(val symbol: String, val price: Double, val timeStamp: LocalDateTime)