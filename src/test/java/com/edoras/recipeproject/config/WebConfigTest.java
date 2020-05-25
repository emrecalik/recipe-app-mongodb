package com.edoras.recipeproject.config;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

class WebConfigTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        WebConfig webConfig = new WebConfig();
        webTestClient = WebTestClient.bindToRouterFunction(webConfig.getRecipesJson(recipeService)).build();
    }

    @Test
    void getRecipesJson() {
        when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe()));

        webTestClient.get().uri("/api/recipe")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Recipe.class);
    }
}