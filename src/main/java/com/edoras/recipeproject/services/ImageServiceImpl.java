package com.edoras.recipeproject.services;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImage(String recipeId, MultipartFile file) {
        Recipe recipe = recipeRepository.findById(recipeId).get();

        try {
            byte[] imageBytes = file.getBytes();
            Byte[] imageByteObjects = new Byte[imageBytes.length];

            int i = 0;
            while (i < imageByteObjects.length) {
                imageByteObjects[i] = imageBytes[i];
                i++;
            }

            recipe.setImage(imageByteObjects);
            recipeRepository.save(recipe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
