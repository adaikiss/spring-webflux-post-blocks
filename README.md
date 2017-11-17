# WebFlux Post Blocks

WebFlux post request with large body may block sometimes.

The server only received partial data.

The unit test always success.



## Run
```bash
mvn spring-boot:run
```

## Test
Visit `http://127.0.0.1:11111`, and post a request with large content(body size greater than 304).

This does not always fail, but with a high chance. If the first request success, restart the server and try again.

If the request with large body failed, the subsequent requests may fail too, even with small body.