package com.edoras.recipeproject.config;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.services.RecipeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebConfig {

    @Bean
    RouterFunction<?> getRecipesJson(RecipeService recipeService) {
        return route(GET("/api/recipe"),
                ServerRequest -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(recipeService.getRecipes(), Recipe.class));
    }
}
