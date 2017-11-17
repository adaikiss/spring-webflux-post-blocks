package com.whaleread.illustration.webflux.post.blocks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

/**
 * Created by hulingwei on 2017/11/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class IllustrationTest {
    @Autowired
    private RouterFunction routerFunction;
    private WebTestClient client;

    @Before
    public void before() {
        client = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    public void testPost() {
        this.client.post().uri("/hi")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody("Hello world!")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testPostWithHugeBody() {
        char[] body = new char[1000];
        for (int i = 0; i < body.length; i++) {
            body[i] = '0';
        }
        this.client.post().uri("/hi")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(new String(body))
                .exchange()
                .expectStatus().isOk();
    }
}
