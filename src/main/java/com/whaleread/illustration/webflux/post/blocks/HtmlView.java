package com.whaleread.illustration.webflux.post.blocks;

import lombok.Setter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.result.view.AbstractUrlBasedView;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

/**
 * Created by dolphin on 2017/11/17
 */
public class HtmlView extends AbstractUrlBasedView {
    @Setter
    private ResourceLoader resourceLoader;

    @Override
    public boolean checkResourceExists(Locale locale) throws Exception {
        //noinspection ConstantConditions
        return resourceLoader.getResource(getUrl()).exists();
    }

    @Override
    protected Mono<Void> renderInternal(Map<String, Object> renderAttributes, @Nullable MediaType contentType, ServerWebExchange exchange) {
        InputStream in = null;
        try {
            //noinspection ConstantConditions
            in = resourceLoader.getResource(getUrl()).getInputStream();
            Flux<DataBuffer> content = DataBufferUtils.read(resourceLoader.getResource(getUrl()), new DefaultDataBufferFactory(), 1024);
            return exchange.getResponse().writeWith(content);
        } catch (IOException e) {
            return Mono.error(new IllegalStateException("Could not load html template for URL[" + getUrl() + "]", e));
        } catch (Exception e) {
            return Mono.error(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
