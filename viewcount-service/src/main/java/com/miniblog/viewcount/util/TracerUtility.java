package com.miniblog.viewcount.util;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TracerUtility {
    private final Tracer tracer;

    public String getTraceId() {
        return tracer.currentSpan().context().traceId();
    }
}
