package nu.westlin.stockticker.server

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

@RestController
class StockController {

    @GetMapping("foo")
    fun foo() = "foo"

    @GetMapping(value = ["/stock/{symbol}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getStock(@PathVariable symbol: String): Flux<StockPrice> {
        return Flux.interval(Duration.ofMillis(123))
            .map { StockPrice(symbol, randomStockPrice(), LocalDateTime.now()) }
    }

    @GetMapping(value = ["/stock2/{symbol}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun getStock2(@PathVariable symbol: String): Flow<StockPrice> {
        return flow {
            while(true) {
                delay(123)
                emit(StockPrice(symbol, randomStockPrice(), LocalDateTime.now()))
            }
        }
    }

    private fun randomStockPrice(): Double = ThreadLocalRandom.current().nextDouble(100.0)
}

data class StockPrice(val symbol: String, val price: Double, val timeStamp: LocalDateTime)