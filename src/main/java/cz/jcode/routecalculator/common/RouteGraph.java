package cz.jcode.routecalculator.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Populated naive graph for first and shortest algorithms
 */
@Component
public class RouteGraph {

    private final Map<String, RouteLink> links;

    public RouteGraph() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = TypeReference.class.getResourceAsStream("/countries.json");

        Set<Country> countries = mapper.readValue(inputStream, new TypeReference<Set<Country>>() {
        });

        links = countries.stream().collect(Collectors.toMap(Country::getCountryCode, RouteLink::new));

        links.values().forEach(routeLink -> {
            Map<String, RouteLink> neighbours = routeLink.getSource().getBorders().stream().map(links::get).collect(Collectors.toMap(cl -> cl.getSource().getCountryCode(), cl -> cl));
            routeLink.setNeighbours(neighbours);
        });
    }

    @Nullable
    public RouteLink get(String origin) {
        return links.get(origin);
    }
}
