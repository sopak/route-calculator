package cz.jcode.routecalculator.common;

import lombok.Value;

import java.util.List;

/**
 * Value objects which represents route between countries
 */
@Value
public class Route {
    private final List<String> route;
}
