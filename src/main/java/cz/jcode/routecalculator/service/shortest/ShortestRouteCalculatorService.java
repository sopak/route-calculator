package cz.jcode.routecalculator.service.shortest;

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

import java.util.*;

/**
 * Little bit better, but still naive algorithm which traverse all possible routes.
 * First shortest matching route is returned as an answer.
 * Search is performed in loop and size of graph is increased if possible routes are not in given graph size
 * MAX_STACK_DEPTH is used to eliminate traverse over too big graph.
 */
@Service
@ConditionalOnProperty(prefix = RouteCalculatorService.PROPERTY_PREFIX, value = RouteCalculatorService.PROPERTY_ALGORITHM_NAME, havingValue = "shortest", matchIfMissing = true)
public class ShortestRouteCalculatorService implements RouteCalculatorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int MAX_STACK_DEPTH = 13;
    private static final int INITIAL_STACK_DEPTH = 5;

    private final RouteGraph graph;

    @Autowired
    public ShortestRouteCalculatorService(RouteGraph graph) {
        this.graph = graph;
    }

    @Nullable
    @Override
    @Cacheable("routes")
    public Route calculateRoute(String origin, String destination) {
        RouteLink originRouteLink = graph.get(origin);
        Stack<String> routeStack = new Stack<>();
        Set<List<String>> routeFound = new HashSet<>();
        int stackDepth = INITIAL_STACK_DEPTH;
        while (routeFound.isEmpty() && stackDepth <= MAX_STACK_DEPTH) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Route 'origin: %s, destination: %s' depth level increased set to '%d'.", origin, destination, stackDepth));
            }
            calculateRoute(routeStack, routeFound, originRouteLink, destination, stackDepth++);
        }

        if (routeFound.isEmpty()) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Route 'origin: %s, destination: %s' not found.", origin, destination));
            }
            return null;
        }

        Optional<List<String>> shortestRoute = routeFound.stream().min(Comparator.comparingInt(List::size));
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("First shortest route for 'origin: %s, destination: %s' is '%s'", origin, destination, shortestRoute));
        }
        return new Route(shortestRoute.get());
    }

    private void calculateRoute(Stack<String> routeStack, Set<List<String>> routeFound, @Nullable RouteLink originRouteLink, String destination, int stackDepth) {
        if (originRouteLink == null) {
            return;
        }
        String countryCode = originRouteLink.getCountryCode();
        if (routeStack.contains(countryCode)) {
            return;
        }
        routeStack.push(countryCode);
        RouteLink directRouteLink = originRouteLink.getNeighbour(destination);
        if (directRouteLink != null) {
            routeStack.push(destination);
            ArrayList<String> newPath = new ArrayList<>(routeStack);
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Possible route 'origin: %s, destination: %s' found on level '%d' as '%s'.", originRouteLink.getCountryCode(), destination, stackDepth, routeStack));
            }
            routeFound.add(newPath);
            routeStack.pop();
        } else if (routeStack.size() < stackDepth) {
            for (RouteLink routeLink : originRouteLink.getNeighbours()) {
                calculateRoute(routeStack, routeFound, routeLink, destination, stackDepth);
            }
        }
        routeStack.pop();
    }
}
