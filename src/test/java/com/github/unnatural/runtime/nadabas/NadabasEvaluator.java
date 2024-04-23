package com.github.unnatural.runtime.nadabas;

public interface NadabasEvaluator<E extends NadabasRecord> {
    boolean match(E record, String query, Object... params);
}