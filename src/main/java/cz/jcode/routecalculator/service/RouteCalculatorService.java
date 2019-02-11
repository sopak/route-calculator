package cz.jcode.routecalculator.service;

import cz.jcode.routecalculator.common.Route;
import javax.annotation.Nullable;

public interface RouteCalculatorService {

    String PROPERTY_PREFIX = "cz.jcode.routecalculator.service";
    String PROPERTY_ALGORITHM_NAME = "algorithm";

    /**
     * Route calculation between origin and destination.
     * Underlying algorithm should return shortest possible route.
     * Te algorithm should be reasonable efficient.
     *
     * @param origin country code of the start point
     * @param destination country code of the end point
     * @return Route, which represents path in graph or null
     */
    @Nullable
    Route calculateRoute(String origin, String destination);
}
