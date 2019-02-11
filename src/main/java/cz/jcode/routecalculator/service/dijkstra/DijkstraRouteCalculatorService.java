package cz.jcode.routecalculator.service.dijkstra;

import cz.jcode.routecalculator.common.*;
import cz.jcode.routecalculator.service.RouteCalculatorService;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Use graph specific library with Dijkstra's Algorithm to shortest path in geodesic applications.
 * This is implemented to show probably more effective algorithms provided by JGraphT.
 */
@Service
@ConditionalOnProperty(prefix = RouteCalculatorService.PROPERTY_PREFIX, value = RouteCalculatorService.PROPERTY_ALGORITHM_NAME, havingValue = "dijkstra")
public class DijkstraRouteCalculatorService implements RouteCalculatorService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RouteDirectedMultigraph graph;

    @Autowired
    public DijkstraRouteCalculatorService(RouteDirectedMultigraph graph) {
        this.graph = graph;
    }

    @Nullable
    @Override
    @Cacheable("routes")
    public Route calculateRoute(String origin, String destination) {
        GraphPath<String, DefaultEdge> pathBetween = DijkstraShortestPath.findPathBetween(graph, origin, destination);

        if (pathBetween == null) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Route 'origin: %s, destination: %s' not found.", origin, destination));
            }
            return null;
        }

        List<String> vertexList = pathBetween.getVertexList();

        if (logger.isTraceEnabled()) {
            logger.trace(String.format("First shortest route for 'origin: %s, destination: %s' is '%s'", origin, destination, vertexList));
        }

        return new Route(vertexList);
    }
}
