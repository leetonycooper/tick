package com.panxora.gravity

import com.fasterxml.jackson.databind.ObjectMapper
import com.panxora.gravity.tick.websocket.Price
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @LocalServerPort
    private val port = 0

    @Test
    @Disabled
    fun `can subscribe to a exchange market ticker`(){

        val stompClient = WebSocketStompClient(SockJsClient(createTransportClient()))
        val mappingJackson2MessageConverter = MappingJackson2MessageConverter()

        mappingJackson2MessageConverter.objectMapper = objectMapper
        stompClient.messageConverter = mappingJackson2MessageConverter

        stompClient.connect("ws://localhost:$port/v1", object : StompSessionHandlerAdapter() {
            override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                val stompFrameHandler = StompFrameHandlerImpl()
                session.subscribe("/topic/bitfinex/BTCUSD", stompFrameHandler)
                session.send("/app/ticker/bitfinex/BTCUSD", "{}")
            }

            override fun handleException(
                    session: StompSession,
                    command: StompCommand?,
                    headers: StompHeaders,
                    payload: ByteArray,
                    exception: Throwable) {
                exception.printStackTrace()
            }

        })[1, TimeUnit.SECONDS]

        Thread.sleep(100000)
    }

    private fun createTransportClient(): List<Transport> {
        val transports: MutableList<Transport> = ArrayList(1)
        transports.add(WebSocketTransport(StandardWebSocketClient()))
        return transports
    }

    class StompFrameHandlerImpl : StompFrameHandler{
        override fun getPayloadType(p0: StompHeaders): Type {
            return Price::class.java
        }

        override fun handleFrame(p0: StompHeaders, p1: Any?) {
            p1 as Price
            LOG.trace("WBS tick received - $p1")
        }
    }

    companion object{
        val LOG: Logger = getLogger(WebSocketTest::class.java)
    }
}

