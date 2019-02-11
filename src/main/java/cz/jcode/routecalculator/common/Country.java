package cz.jcode.routecalculator.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Set;

/**
 * Value of necessary fields from the source JSON file
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Value
class Country {
    @JsonProperty("cca3")
    public final String countryCode;
    public final Set<String> borders;
}
