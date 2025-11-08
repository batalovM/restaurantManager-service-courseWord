package ru.batalov.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author batal
 * @Date 09.11.2025
 */
@Configuration
@ConfigurationPropertiesScan(basePackages = {"ru.isands.lib.specification.template.configuration.properties",
        "ru.batalov.configuration"})
@ComponentScan(basePackages = "ru.batalov")
public class RestaurantManagerConf {
}
