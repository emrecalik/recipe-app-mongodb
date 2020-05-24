package com.edoras.recipeproject.services;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.repositories.reactive.RecipeReactiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImage(String recipeId, MultipartFile file) {
        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        try {
            byte[] imageBytes = file.getBytes();
            Byte[] imageByteObjects = new Byte[imageBytes.length];

            int i = 0;
            while (i < imageByteObjects.length) {
                imageByteObjects[i] = imageBytes[i];
                i++;
            }

            recipe.setImage(imageByteObjects);
            recipeReactiveRepository.save(recipe).block();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Mono.empty();
    }
}
