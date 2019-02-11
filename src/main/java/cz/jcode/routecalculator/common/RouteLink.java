package cz.jcode.routecalculator.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Link between country and country's neighbours
 */
@Data
public class RouteLink {
    @Getter(AccessLevel.PACKAGE)
    private final Country source;

    @Nullable
    private Map<String, RouteLink> neighbours;

    @Override
    public int hashCode() {
        return getSource().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return getSource().equals(o);
    }

    @Override
    public String toString() {
        return getSource().toString();
    }

    @Nullable
    public RouteLink getNeighbour(String destination) {
        if (neighbours == null) {
            return null;
        }
        return neighbours.get(destination);
    }

    public Collection<RouteLink> getNeighbours() {
        if (neighbours == null) {
            return Collections.emptyList();
        }
        return neighbours.values();
    }

    public String getCountryCode() {
        return getSource().getCountryCode();
    }
}