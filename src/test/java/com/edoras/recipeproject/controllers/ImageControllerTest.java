package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.services.ImageService;
import com.edoras.recipeproject.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    ImageController imageController;

    private String RECIPE_ID = "1";

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageController = new ImageController(recipeService, imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();
    }

    @Test
    void showImageUploadForm() throws Exception {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        // when
        when(recipeService.findById(anyString())).thenReturn(recipe);

        // then
        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/image"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/imageuploadform"))
                .andExpect(model().attributeExists("recipe"));
        verify(recipeService, times(1)).findById(anyLong());
    }

    @Test
    void saveImage() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile",
                "filename.jpg", "text/plain", "This is a mock for image".getBytes());

        mockMvc.perform(multipart("/recipe/" + RECIPE_ID + "/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + RECIPE_ID + "/show"));

        verify(imageService, times(1)).saveImage(anyLong(), any());
    }

    @Test
    void renderImage() throws Exception {
        String image = "This is a mock image";
        byte[] imageBytes = image.getBytes();
        Byte[] imageByteObjects = new Byte[imageBytes.length];

        int i = 0;
        while(i < imageByteObjects.length) {
            imageByteObjects[i] = imageBytes[i];
            i++;
        }

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setImage(imageByteObjects);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/" + RECIPE_ID + "/recipeImage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(response.getContentAsByteArray().length, imageBytes.length);
    }

    @Test
    void handleNumberFormatException() throws Exception {
        mockMvc.perform(get("/recipe/DUMMY/image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

}