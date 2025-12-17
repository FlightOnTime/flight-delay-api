package com.flightontime.flightontime.domain.model;

import lombok.Data;

@Data
public class FlightPredictionRequest {

    private String Airline;
    private String Origin;
    private String Dest;
    private Integer Month;
    private Integer DayOfWeek;
    private Integer CRSDepTime;
    private Integer Distance;
}
