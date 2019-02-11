package cz.jcode.routecalculator.service.shortest;

import com.google.common.collect.ImmutableList;
import cz.jcode.routecalculator.app.RouteCalculatorApplication;
import cz.jcode.routecalculator.common.Route;
import cz.jcode.routecalculator.service.RouteCalculatorService;
import cz.jcode.routecalculator.service.RouteCalculatorServiceTestBase;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;

@RunWith(Parameterized.class)
@ContextConfiguration(classes = RouteCalculatorApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShortestRouteCalculatorServiceTest extends RouteCalculatorServiceTestBase {

    @Parameters(name = "{index}: {0}")
    public static Collection<RouteRecord> classes() {
        return ImmutableList.of(
                new RouteRecord("CZE", "ITA", new Route(ImmutableList.of("CZE", "AUT", "ITA"))),
                new RouteRecord("AUT", "CZE", new Route(ImmutableList.of("AUT", "CZE"))),
                new RouteCalculatorServiceTestBase.RouteRecord("ESP", "CZE", new Route(ImmutableList.of("ESP", "FRA", "DEU", "CZE"))),
                new RouteCalculatorServiceTestBase.RouteRecord("CZE", "ESP", new Route(ImmutableList.of("CZE", "DEU", "FRA", "ESP")))
        );
    }

    @BeforeClass
    public static void setUpProperty() {
        System.setProperty(RouteCalculatorService.PROPERTY_PREFIX + "." + RouteCalculatorService.PROPERTY_ALGORITHM_NAME, "shortest");
    }
}
