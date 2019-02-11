package cz.jcode.routecalculator.service;

import cz.jcode.routecalculator.common.Route;
import lombok.Value;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public abstract class RouteCalculatorServiceTestBase {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @AfterClass
    public static void cleanProperty() {
        System.clearProperty(RouteCalculatorService.PROPERTY_PREFIX + "." + RouteCalculatorService.PROPERTY_ALGORITHM_NAME);
    }

    @SuppressWarnings("NullableProblems")
    @Parameterized.Parameter
    public RouteRecord routeRecord;

    @SuppressWarnings("NullableProblems")
    private RouteCalculatorService routeCalculator;

    @SuppressWarnings("NullableProblems")
    private CacheManager cacheManager;

    @Autowired
    private void setRouteCalculator(RouteCalculatorService routeCalculator) {
        this.routeCalculator = routeCalculator;
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Test
    public void testRoutes() {
        Route route = routeCalculator.calculateRoute(routeRecord.getOrigin(), routeRecord.getDestination());
        assertEquals("Route is not correct", routeRecord.getRoute(), route);
    }

    @Test
    public void simplePerformanceTest() {
        for (int i = 0; i < 10000; i++) {
            clearCache();
            routeCalculator.calculateRoute(routeRecord.getOrigin(), routeRecord.getDestination());
        }
    }

    private void clearCache() {
        Cache routes = cacheManager.getCache("routes");
        if (routes != null) {
            routes.clear();
        }
    }

    @Value
    public static class RouteRecord {
        private String origin;
        private String destination;
        private Route route;

        @Override
        public String toString() {
            return "RouteRecord{" +
                    "origin='" + origin + '\'' +
                    ", destination='" + destination + '\'' +
                    '}';
        }
    }
}
