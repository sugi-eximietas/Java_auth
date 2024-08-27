package com.eximietas.libs.authentication.mtls;


import java.security.cert.X509Certificate;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class MTLSAspect {

    private static final Logger logger = LoggerFactory.getLogger(MTLSAspect.class);

    @Around("@annotation(MTLS)")
    public Object checkMTLS(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        X509Certificate[] certs = (X509Certificate[]) request.getAttribute("jakarta.servlet.request.X509Certificate");

        if (certs == null || certs.length == 0) {
            logger.error("Client certificate required but not provided for path: {}", request.getRequestURI());
            if (response != null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Client certificate required");
            }
            return null;
        }

        // Log the details of the first certificate (client cert)
        X509Certificate clientCert = certs[0];
        logger.info("Client certificate found. Subject: {}", clientCert.getSubjectX500Principal().getName());
        logger.info("Certificate Issuer: {}", clientCert.getIssuerX500Principal().getName());
        logger.info("Certificate Serial Number: {}", clientCert.getSerialNumber());
        logger.info("Certificate Not Before: {}", clientCert.getNotBefore());
        logger.info("Certificate Not After: {}", clientCert.getNotAfter());

        return joinPoint.proceed();
    }
}