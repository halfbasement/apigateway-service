package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {


    public LoggingFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        //Custoem Pre Filter

   /*     return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage= {}",config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Global Filter Start :  request id ->{}",request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{


                if(config.isPostLogger()){
                    log.info("Global Filter End :  response Code ->{}",response.getStatusCode());
                };
            }));

        };*/

        /**
         * 람다 x 위 코드와 동일
         */
        GatewayFilter filter = new OrderedGatewayFilter((exchange,chain)->{
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage= {}",config.getBaseMessage());

            if(config.isPreLogger()){
                log.info("Logging PRE Filter  :  request id ->{}",request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{


                if(config.isPostLogger()){
                    log.info("Logging Post Filter  :  response Code ->{}",response.getStatusCode());
                };
            }));
        }, Ordered.LOWEST_PRECEDENCE); // 두번쨰 Orderd 로 필터 우선순위 변경가능 ( 하이스트로 하면 글로벌보다 먼저 실행 )

        return filter;
    }

    @Data
    public static class Config{
        //put the configuration properties

        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
        private String sayHello;

    }

}
