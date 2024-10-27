package com.juliomesquita.admin.catalog.domain.commom.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class InstantUtil {
    private InstantUtil() {
    }

    public static Instant now(){
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }
}
