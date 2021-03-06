package com.auth.gw.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.websocket.WebsocketInbound;

import java.net.URI;

public class MyReactorNettyWebSocketClient extends ReactorNettyWebSocketClient {

    private static final Log logger = LogFactory.getLog(ReactorNettyWebSocketClient.class);


    private final HttpClient httpClient;

    /**
     * websocket最大大小为10MB
     */
    private static final int DEFAULT_FRAME_MAX_SIZE = 100 * 1024 * 8;


    /**
     * Default constructor.
     */
    public MyReactorNettyWebSocketClient() {
        this(HttpClient.create());
    }

    /**
     * Constructor that accepts an existing {@link HttpClient} builder.
     *
     * @since 5.1
     */
    public MyReactorNettyWebSocketClient(HttpClient httpClient) {
        Assert.notNull(httpClient, "HttpClient is required");
        this.httpClient = httpClient;
    }


    /**
     * Return the configured {@link HttpClient}.
     */
    public HttpClient getHttpClient() {
        return this.httpClient;
    }


    @Override
    public Mono<Void> execute(URI url, WebSocketHandler handler) {
        return execute(url, new HttpHeaders(), handler);
    }

    @Override
    public Mono<Void> execute(URI url, HttpHeaders requestHeaders, WebSocketHandler handler) {
        return getHttpClient()
                .headers(nettyHeaders -> setNettyHeaders(requestHeaders, nettyHeaders))
                .websocket(StringUtils.collectionToCommaDelimitedString(handler.getSubProtocols()))
                .uri(url.toString())
                .handle((inbound, outbound) -> {
                    HttpHeaders responseHeaders = toHttpHeaders(inbound);
                    String protocol = responseHeaders.getFirst("Sec-WebSocket-Protocol");
                    HandshakeInfo info = new HandshakeInfo(url, responseHeaders, Mono.empty(), protocol);
                    NettyDataBufferFactory factory = new NettyDataBufferFactory(outbound.alloc());
                    WebSocketSession session = new ReactorNettyWebSocketSession(inbound, outbound, info, factory, DEFAULT_FRAME_MAX_SIZE);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Started session '" + session.getId() + "' for " + url);
                    }
                    return handler.handle(session);
                })
                .doOnRequest(n -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Connecting to " + url);
                    }
                })
                .next();
    }

    private void setNettyHeaders(HttpHeaders httpHeaders, io.netty.handler.codec.http.HttpHeaders nettyHeaders) {
        httpHeaders.forEach(nettyHeaders::set);
    }

    private HttpHeaders toHttpHeaders(WebsocketInbound inbound) {
        HttpHeaders headers = new HttpHeaders();
        io.netty.handler.codec.http.HttpHeaders nettyHeaders = inbound.headers();
        nettyHeaders.forEach(entry -> {
            String name = entry.getKey();
            headers.put(name, nettyHeaders.getAll(name));
        });
        return headers;
    }
}