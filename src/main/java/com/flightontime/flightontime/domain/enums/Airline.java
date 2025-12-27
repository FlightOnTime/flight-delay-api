package com.flightontime.flightontime.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Airline {

    AMERICAN_AIRLINES("AA"),
    DELTA_AIRLINES("DL"),
    UNITED_AIRLINES("UA"),
    SOUTHWEST_AIRLINES("WN"),
    JETBLUE("B6"),
    ALASKA_AIRLINES("AS"),
    SPIRIT("NK"),
    FRONTIER("F9"),
    GOL_DEVELOPMENTS("G4"),
    HAWAIIAN_AIRLINES("HA");

    private final String codigo;
}
