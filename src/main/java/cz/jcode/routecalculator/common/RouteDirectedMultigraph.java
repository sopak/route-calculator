package cz.jcode.routecalculator.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

/**
 * Populated directed multigraph for searching route with specific library JGraphT
 */
@Component
public class RouteDirectedMultigraph extends DirectedMultigraph<String, DefaultEdge> {
    public RouteDirectedMultigraph() {
        super(DefaultEdge.class);
    }

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = TypeReference.class.getResourceAsStream("/countries.json");

        Set<Country> countries = mapper.readValue(inputStream, new TypeReference<Set<Country>>() {
        });

        countries.stream().map(Country::getCountryCode).forEach(this::addVertex);

        countries.forEach(
                c ->
                        c.borders.forEach(
                                b -> countries.stream().filter(c2 -> Objects.equals(c2.getCountryCode(), b)).forEach(
                                        e -> this.addEdge(c.getCountryCode(), e.getCountryCode())
                                )
                        )

        );
    }
}
