package com.example.sdkotel.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@RestController
public class Rolldice {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return("testttttt");
    }

    //spring slf4j Logger to console not OTEL
    private static final Logger logger = LoggerFactory.getLogger(Rolldice.class);
    //Tracer instance
    private final Tracer tracer;
    
    //Otel entrypoint 
    Rolldice(OpenTelemetry openTelemetry){
        this.tracer = openTelemetry.getTracer(Rolldice.class.getName(),"0.1.0");
    }
    
    @WithSpan
    @GetMapping("/request")
    public String example(@SpanAttribute @RequestParam("name") String name) {
        return new String(name);
    }
    

    @GetMapping("/rolldice")
    public String index(@RequestParam("player") Optional<String> player) {
        int result = this.getRandomNumber(1, 6);

        // span needs to be initialized using startspan
        Span span = tracer.spanBuilder("roll-dice").startSpan();

        // Instrumentation scope to set span lifecycle as current 
        try(Scope scope = span.makeCurrent()){
            if(!player.isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"missing player name",null);
            }
            if (player.isPresent()) {
                logger.info("{} is rolling the dice: {}", player.get(), result);
                span.addEvent("dice is rolled");
            } else {
                logger.info("Anonymous player is rolling the dice: {}", result);
            }
            return Integer.toString(result);
        }
        catch(Throwable t){
            span.setStatus(StatusCode.ERROR, "bad-span-error");
            span.recordException(t);
            throw t;
        }
        finally{
            // span ends here, for a single lifecycle 
            span.end();
        }
    }

    @WithSpan
    public int getRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    @GetMapping("/nested")
    public List<Integer> rollTheDice(int rolls) {
        Span parentSpan = tracer.spanBuilder("parent").startSpan();
        try (Scope scope = parentSpan.makeCurrent()) {
            List<Integer> results = new ArrayList<Integer>();
            for (int i = 0; i < rolls; i++) {
                results.add(this.rollOnce(i, i));
            }
            return results;
        } 
        finally {
            parentSpan.end();
        }
    }

    private int rollOnce(int min,int max) {
        Span childSpan = tracer.spanBuilder("child")
        // NOTE: setParent(...) is not required;
        // `Span.current()` is automatically added as the parent
        .startSpan();
        try(Scope scope = childSpan.makeCurrent()) {
            return ThreadLocalRandom.current().nextInt(min, max + 1);
        } 
        finally {
            childSpan.end();
        }   
    }
    
}
