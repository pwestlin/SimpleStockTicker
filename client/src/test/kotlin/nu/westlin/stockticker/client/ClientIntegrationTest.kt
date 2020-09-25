package nu.westlin.stockticker.client

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import kotlin.coroutines.coroutineContext

internal class ClientIntegrationTest {

    val client: Client = Client(WebClient.create())

    @Test
    fun `get stock prices from the server with Flux`() {
        val prices: Flux<StockPrice> = client.pricesFor("SYMBOL")

        assertNotNull(prices)
        val fivePrices = prices.take(5)
        assertThat(fivePrices.count().block()).isEqualTo(5)
        assertThat(fivePrices.blockFirst()?.symbol).isEqualTo("SYMBOL")
    }

    @Test
    fun `get stock prices from the server with Flow`() = runBlocking<Unit> {
        val prices: Flow<StockPrice> = client.pricesFor2("SYMBOL")

        assertNotNull(prices)
        val fivePrices = prices.take(5)
        assertThat(fivePrices.count()).isEqualTo(5)
        assertThat(fivePrices.toList().first().symbol).isEqualTo("SYMBOL")
    }
}

