package com.github.dmgiangi.brewerhub.services;

import com.github.dmgiangi.brewerhub.models.entity.ErrorResponse;
import com.google.gson.Gson;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Component
public class IpFilter implements Filter {
        final static Logger logger = LogManager.getLogger(IpFilter.class);

        private static final Bandwidth limit = Bandwidth.simple(50, Duration.ofMinutes(1));

        private Map<String, Bucket> buckets;

        @Override
        public void init(FilterConfig filterConfig) {
                buckets = new HashMap<>();
        }

        @Override
        public void destroy() { Filter.super.destroy(); }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
                String ip = request.getRemoteAddr();

                buckets.putIfAbsent(ip,
                        Bucket.builder()
                                .addLimit(limit)
                                .build());
                Bucket bucket = buckets.get(ip);

                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setHeader("x-ratelimit-limit:", "60");

                if (bucket.tryConsume(1)) {
                        httpResponse.setHeader(
                                "x-ratelimit-limit-remaining:",
                                bucket.getAvailableTokens() + "");
                        chain.doFilter(request, httpResponse);
                } else {
                        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                        httpResponse.setHeader("x-ratelimit-limit-remaining:", "0");
                        httpResponse.getWriter().write(
                                new Gson().toJson(
                                        new ErrorResponse("Too many request baby! slow down!" , "429")));
                        httpResponse.getWriter().flush();
                }
        }
}