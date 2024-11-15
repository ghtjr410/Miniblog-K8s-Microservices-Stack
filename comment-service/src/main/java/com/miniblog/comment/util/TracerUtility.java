package com.miniblog.comment.util;

import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Component;

@Component
public class TracerUtility {
    private final Tracer tracer;

    public TracerUtility(Tracer tracer) {
        this.tracer = tracer;
    }

    public String getTraceId() {
        return tracer.currentSpan().context().traceId();
    }
}
