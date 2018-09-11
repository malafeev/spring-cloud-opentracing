package io.opentracing.example.client2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(value = "rest-service-1", fallback = StoreClientFallback.class)
public interface StoreClient {

  @RequestMapping(method = RequestMethod.GET, value = "/")
  String get();

}


