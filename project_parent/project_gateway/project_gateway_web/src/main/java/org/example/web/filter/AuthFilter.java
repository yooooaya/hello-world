package org.example.web.filter;

import org.example.web.service.AuthService;
import org.example.web.util.UrlsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * 鉴权过滤器 验证token
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final String LOGIN_URL="http://localhost:8001/api/oauth/toLogin";
    public static final String Authorization = "Authorization";
    @Autowired
    private AuthService authService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1. 获取请求
        ServerHttpRequest request = exchange.getRequest();
        //2. 则获取响应
        ServerHttpResponse response = exchange.getResponse();
        //3. 如果是登录请求则放行
        String res=request.getURI().getPath();
        if (UrlsUtil.hasAuth(res)) {
            System.out.println(res);
            return chain.filter(exchange);
        }
        //判断cookie上是否存在jti
        String jti = authService.getJtiFromCookie(request);
        if (StringUtils.isEmpty(jti)){
            //拒绝访问,请求跳转

            return this.toLoginPage(LOGIN_URL+"?from="+res,exchange);
        }
        //判断redis中token是否存在
        String redisToken = authService.getTokenFromRedis(jti);
        if (StringUtils.isEmpty(redisToken)){
            //拒绝访问，请求跳转
            return this.toLoginPage(LOGIN_URL+"?from="+res,exchange);
        }
//        //4. 获取请求头
//        HttpHeaders headers = request.getHeaders();
//        //5. 请求头中获取令牌
//        String token = headers.getFirst(AUTHORIZE_TOKEN);
//
//        //6. 判断请求头中是否有令牌
//        if (StringUtils.isEmpty(token)) {
//            //7. 响应中放入返回的状态吗, 没有权限访问
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            //8. 返回
//            return response.setComplete();
//        }
//
//        //9. 如果请求头中有令牌则解析令牌
//        try {
//            JwtUtil.parseJWT(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //10. 解析jwt令牌出错, 说明令牌过期或者伪造等不合法情况出现
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            //11. 返回
//            return response.setComplete();
//        }
        //校验通过 , 请求头增强，放行
        request.mutate().header(Authorization,"Bearer "+redisToken);
        //12. 放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> toLoginPage(String loginUrl, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",loginUrl);
        return response.setComplete();
    }
}
