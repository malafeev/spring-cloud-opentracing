package io.opentracing.example.client;


import brave.sampler.Sampler;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.schedulers.Schedulers;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

@EnableEurekaClient
@RestController
@SpringBootApplication
public class ServiceApp {

  private static final Logger logger = LoggerFactory.getLogger(ServiceApp.class);

  @Bean
  public Sampler sleuthTraceSampler() {
    return Sampler.ALWAYS_SAMPLE;
  }

  @GetMapping("/")
  public String index(@RequestHeader HttpHeaders headers) {
    return "Hello from Service 1";
  }

  @GetMapping("rx")
  public String rxJava() {
    Observable observable = Observable.range(1, 8)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .map(integer -> integer * 2)
        .filter(integer -> integer % 2 == 0);

    observable.subscribe(o -> logger.info("{}", o));

    return "rx";
  }

  @GetMapping("/headers")
  public ResponseEntity<?> home(@RequestHeader HttpHeaders headers) {
    Map<String, String> map = new HashMap<>();
    headers.toSingleValueMap().forEach(map::put);
    return ResponseEntity.ok(map);
  }

  @Bean
  public Reporter<Span> reporter() {
    return Reporter.CONSOLE;
  }

  @GetMapping("/traces")
  public ResponseEntity<?> traces() {
    return ResponseEntity.ok().build();
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp.class).run(args);
  }
}
