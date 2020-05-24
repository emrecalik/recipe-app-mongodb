package com.edoras.recipeproject.services;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    ImageServiceImpl imageService;

    private final String RECIPE_ID = "1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeReactiveRepository);
    }

    @Test
    void saveImage() throws IOException {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile"
                ,"filename.txt","text/plain","This is a mock for image".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(recipe)).thenReturn(Mono.just(recipe));

        // when
        imageService.saveImage(RECIPE_ID, multipartFile);

        // then
        verify(recipeReactiveRepository, times(1)).save(any());
        assertEquals(multipartFile.getBytes().length, recipe.getImage().length);
    }
}