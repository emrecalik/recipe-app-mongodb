package com.edoras.recipeproject.services;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageServiceImpl imageService;

    private final String RECIPE_ID = "1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveImage() throws IOException {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile"
                ,"filename.txt","text/plain","This is a mock for image".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        when(recipeRepository.findById(anyString())).thenReturn(java.util.Optional.of(recipe));

        // when
        imageService.saveImage(RECIPE_ID, multipartFile);

        // then
        verify(recipeRepository, times(1)).save(any());
        assertEquals(multipartFile.getBytes().length, recipe.getImage().length);
    }
}