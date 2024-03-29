package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.services.ImageService;
import com.edoras.recipeproject.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class ImageController {

    RecipeService recipeService;
    ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String showImageUploadForm(@PathVariable String recipeId, Model model) {
        Recipe recipe = recipeService.findById(recipeId).block();
        model.addAttribute("recipe", recipe);
        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String saveImage(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile multipartFile) {
        imageService.saveImage(recipeId, multipartFile).block();
        return "redirect:/recipe/" + recipeId + "/show";
    }

//    @GetMapping("/recipe/{recipeId}/recipeImage")
//    public void renderImage(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
//        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();
//
//        if (recipeCommand.getImage() != null) {
//            Byte[] imageByteObjects = recipeCommand.getImage();
//            byte[] imageBytes = new byte[imageByteObjects.length];
//
//            int i = 0;
//            while (i < imageBytes.length) {
//                imageBytes[i] = imageByteObjects[i];
//                i++;
//            }
//
//            response.setContentType("image/jpg");
//            response.getOutputStream().write(imageBytes);
//            response.getOutputStream().close();
//        }
//    }
}
