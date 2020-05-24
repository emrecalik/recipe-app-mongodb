package com.edoras.recipeproject.services;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface ImageService {
    Mono<Void> saveImage(String recipeId, MultipartFile file);
}
