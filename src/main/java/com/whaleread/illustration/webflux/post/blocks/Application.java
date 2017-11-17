package com.whaleread.illustration.webflux.post.blocks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.AbstractUrlBasedView;
import org.springframework.web.reactive.result.view.UrlBasedViewResolver;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by dolphin on 2017/11/17
 */
@Slf4j
@SpringBootApplication
@ComponentScan
public class Application implements WebFluxConfigurer {
    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public RouterFunction<?> routers() {
        return route(path(""), request -> ServerResponse.ok().render("index"))
                .andRoute(path("/favicon.ico"), request -> ServerResponse.notFound().build())
                .andRoute(POST("/hi"), request -> {
                    String body = request.bodyToMono(String.class).block();
                    log.info("body: {}", body);
                    return ServerResponse.ok().body(BodyInserters.fromObject("Data received, length: " + body.length()));
                })
                .andRoute(GET("/hi"), request -> ServerResponse.ok().body(BodyInserters.fromObject("Hello " + request.queryParam("name").orElse("Anonymous") + "!")));
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        UrlBasedViewResolver htmlViewResolver = new UrlBasedViewResolver() {
            @Override
            protected AbstractUrlBasedView createView(String viewName) {
                HtmlView view = (HtmlView) super.createView(viewName);
                view.setResourceLoader(resourceLoader);
                return view;
            }
        };
        htmlViewResolver.setPrefix("classpath:/templates/");
        htmlViewResolver.setSuffix(".html");
        htmlViewResolver.setViewClass(HtmlView.class);
        registry.viewResolver(htmlViewResolver);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
