package cz.jcode.routecalculator.api;

import cz.jcode.routecalculator.common.Route;
import cz.jcode.routecalculator.service.RouteCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Mapping for REST API
 */
@RestController
class RoutingController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RouteCalculatorService routeCalculator;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public RoutingController(RouteCalculatorService routeCalculator) {
        this.routeCalculator = routeCalculator;
    }

    @RequestMapping(value = "/routing/{origin}/{destination}", method = GET)
    public Route routing(@PathVariable String origin, @PathVariable String destination) {
        Route route = routeCalculator.calculateRoute(origin, destination);

        if (route == null) {
            String msg = String.format("Route 'origin: %s, destination: %s' not found or is too long.", origin, destination);
            logger.debug(msg);
            throw new RouteNotFoundException(msg);
        }
        if(logger.isTraceEnabled()) {
            logger.trace(String.format("Route 'origin: %s, destination: %s' found as %s.", origin, destination, route));
        }
        return route;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    class RouteNotFoundException extends RuntimeException {

        RouteNotFoundException(String message) {
            super(message);
        }
    }
}
