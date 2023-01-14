package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class ZuulLoggingFilter extends ZuulFilter {

    /**
     * 필터의 기능을 정의하는 메서드
     * @return Object
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        log.info("**************** printing logs: ");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("**************** printing logs: " + request.getRequestURI());
        return null;
    }

    /**
     * 사전 필터인지 사후 필터인지를 나타냄 pre(사전)/(사후)
     * @return String
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 여러 개의 필터가 있을 시 순서를 지정
     * @return int
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 필터 사용 여부
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }
}
