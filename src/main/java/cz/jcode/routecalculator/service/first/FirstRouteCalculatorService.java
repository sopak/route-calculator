package cz.jcode.routecalculator.service.first;

import cz.jcode.routecalculator.common.Route;
import cz.jcode.routecalculator.common.RouteGraph;
import cz.jcode.routecalculator.common.RouteLink;
import cz.jcode.routecalculator.service.RouteCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.Stack;

/**
 * Dump algorithm which traverse all possible routes.
 * First matching route is returned as an answer.
 * MAX_STACK_DEPTH is used to eliminate traverse over too big graph.
 */
@Service
@ConditionalOnProperty(prefix = RouteCalculatorService.PROPERTY_PREFIX, value = RouteCalculatorService.PROPERTY_ALGORITHM_NAME, havingValue = "first")
public class FirstRouteCalculatorService implements RouteCalculatorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int MAX_STACK_DEPTH = 13;

    private final RouteGraph graph;

    @Autowired
    public FirstRouteCalculatorService(RouteGraph graph) {
        this.graph = graph;
    }

    @Nullable
    @Override
    @Cacheable("routes")
    public Route calculateRoute(String origin, String destination) {
        RouteLink originRouteLink = graph.get(origin);
        Stack<String> routeStack = new Stack<>();
        calculateRoute(routeStack, originRouteLink, destination);

        if (routeStack.isEmpty()) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Route 'origin: %s, destination: %s' not found.", origin, destination));
            }
            return null;
        }
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("Route 'origin: %s, destination: %s' found.", origin, destination));
        }
        return new Route(routeStack);
    }

    private boolean calculateRoute(Stack<String> routeStack, @Nullable RouteLink originRouteLink, String destination) {
        if (originRouteLink == null) {
            return false;
        }
        String countryCode = originRouteLink.getCountryCode();
        if (routeStack.contains(countryCode) || routeStack.size() >= MAX_STACK_DEPTH) {
            return false;
        }
        routeStack.push(countryCode);
        RouteLink directRouteLink = originRouteLink.getNeighbour(destination);
        if (directRouteLink != null) {
            routeStack.push(destination);
            return true;
        } else {
            for (RouteLink routeLink : originRouteLink.getNeighbours()) {
                boolean found = calculateRoute(routeStack, routeLink, destination);
                if (found) {
                    return true;
                }
            }
            routeStack.pop();
            return false;
        }
    }
}
