package com.edoras.recipeproject.bootstrap;

import com.edoras.recipeproject.domains.*;
import com.edoras.recipeproject.repositories.CategoryRepository;
import com.edoras.recipeproject.repositories.RecipeRepository;
import com.edoras.recipeproject.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@Profile("default")
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    public DataLoader(UnitOfMeasureRepository unitOfMeasureRepository, RecipeRepository recipeRepository,
                      CategoryRepository categoryRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loading bootstrap");
        Optional<UnitOfMeasure> ripeOptional = unitOfMeasureRepository.findByDescription("Ripe");
        if (!ripeOptional.isPresent()) {
            throw new RuntimeException("Expected UOM Not Found");
        }
        Optional<UnitOfMeasure> teaspoonOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        if (!teaspoonOptional.isPresent()) {
            throw new RuntimeException("Expected UOM Not Found");
        }
        Optional<UnitOfMeasure> tablespoonOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
        if (!tablespoonOptional.isPresent()) {
            throw new RuntimeException("Expected UOM Not Found");
        }

        UnitOfMeasure ripe = ripeOptional.get();
        UnitOfMeasure teaspoon = teaspoonOptional.get();
        UnitOfMeasure tablespoon = tablespoonOptional.get();

        Set<Recipe> recipes = new HashSet<>();
        Recipe recipe1 = getRecipe1(ripe, teaspoon, tablespoon);
        Recipe recipe2 = getRecipe2(teaspoon, tablespoon);
        recipes.add(recipe1);
        recipes.add(recipe2);

        recipeRepository.saveAll(recipes);
    }

    private Recipe getRecipe1(UnitOfMeasure ripe, UnitOfMeasure teaspoon, UnitOfMeasure tablespoon) {
        Recipe recipe1 = new Recipe();
        recipe1.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        recipe1.setCookTime(8);
        recipe1.setPrepTime(10);
        recipe1.setServings(5);
        recipe1.setDescription("Perfect Guacamole");
        recipe1.setDirections("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. " +
                "Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. " +
                "(See How to Cut and Peel an Avocado.) Place in a bowl." +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole " +
                "should be a little chunky.)" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving." +
                "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");
        recipe1.setDifficulty(Difficulty.EASY);
        recipe1.setSource("Simply Recipes");

        Notes note1 = new Notes();
        note1.setRecipeNotes("Simple Guacamole: The simplest version of guacamole is just mashed avocados with salt. Don’t let the lack of availability of other ingredients stop you from making guacamole.\n" +
                "Quick guacamole: For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                "Don’t have enough avocados? To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.");

        recipe1.setNotes(note1);

        recipe1.addIngredients(new Ingredient("ripe avocados", new BigDecimal(2), ripe));
        recipe1.addIngredients(new Ingredient("salt", new BigDecimal(0.25), teaspoon));
        recipe1.addIngredients(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(1), tablespoon));

        Category category1 = categoryRepository.findByDescription("Mexican").get();
        Category category2 = categoryRepository.findByDescription("American").get();

        recipe1.getCategories().add(category1);
        recipe1.getCategories().add(category2);

        return recipe1;
    }

    private Recipe getRecipe2(UnitOfMeasure teaspoon, UnitOfMeasure tablespoon) {
        Recipe recipe2 = new Recipe();
        recipe2.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        recipe2.setCookTime(9);
        recipe2.setPrepTime(20);
        recipe2.setServings(3);
        recipe2.setDescription("Spicy Grilled Chicken Tacos\n");
        recipe2.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings." +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");
        recipe2.setDifficulty(Difficulty.MODERATE);
        recipe2.setSource("Simply Recipes");

        Notes note2 = new Notes();
        note2.setRecipeNotes("Look for ancho chile powder with the Mexican ingredients at your grocery store, on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano, and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.)");

        recipe2.setNotes(note2);

        recipe2.addIngredients(new Ingredient("ancho chili powder", new BigDecimal(2), tablespoon));
        recipe2.addIngredients(new Ingredient("dried oregano", new BigDecimal(1), teaspoon));
        recipe2.addIngredients(new Ingredient("salt", new BigDecimal(0.5), teaspoon));

        Category category1 = categoryRepository.findByDescription("Mexican").get();
        Category category2 = categoryRepository.findByDescription("American").get();

        recipe2.getCategories().add(category1);
        recipe2.getCategories().add(category2);

        return recipe2;
    }

}
