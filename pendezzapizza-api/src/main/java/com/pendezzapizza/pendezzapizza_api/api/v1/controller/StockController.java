package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.IngredientModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.RecipeItemModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassembler.IngredientDisassembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.IngredientModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.RecipeItemModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.IngredientDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.IngredientUpdateDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.RecipeItemDTO;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.StockEntryDTO;
import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;
import com.pendezzapizza.pendezzapizza_api.domain.service.IngredientService;
import com.pendezzapizza.pendezzapizza_api.domain.service.RecipeService;
import com.pendezzapizza.pendezzapizza_api.domain.service.StockReportService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController 
@RequestMapping (path = "/v1/restaurants/{restaurantId}/stock", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor 
public class StockController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final StockReportService stockReportService;

    private final IngredientModelAssembler ingredientAssembler;
    private final RecipeItemModelAssembler recipeItemAssembler;
    private final IngredientDisassembler ingredientDisassembler;

    // ---------- Ingredientes ----------

    @GetMapping("/ingredients")
    public Collection<IngredientModel> list(@PathVariable UUID restaurantId) {
        return ingredientAssembler.toCollectionModel(ingredientService.findAll(restaurantId));
    }

    @PostMapping ("/ingredients")
    @ResponseStatus (HttpStatus.CREATED)
    public IngredientModel create(@PathVariable UUID restaurantId, @Valid @RequestBody IngredientDTO dto) {
        Ingredient ingredient = ingredientDisassembler.ingredientDTOToIngredient(dto);
        return ingredientAssembler.toModel(
                ingredientService.create(restaurantId, ingredient, zeroIfNull(dto.getInitialQuantity())));
    }

    @PutMapping ("/ingredients/{ingredientId}")
    public IngredientModel update(@PathVariable UUID restaurantId, @PathVariable UUID ingredientId,
            @Valid @RequestBody IngredientUpdateDTO dto) {
        Ingredient changes = ingredientDisassembler.ingredientUpdateDTOToIngredient(dto);
        return ingredientAssembler.toModel(ingredientService.update(restaurantId, ingredientId, changes));
    }

    @DeleteMapping("/ingredients/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID restaurantId, @PathVariable UUID ingredientId) {
        ingredientService.delete(restaurantId, ingredientId);
    }

    @PostMapping("/ingredients/{ingredientId}/entries")
    public IngredientModel addEntry(@PathVariable UUID restaurantId, @PathVariable UUID ingredientId,
            @Valid @RequestBody StockEntryDTO dto) {
        return ingredientAssembler.toModel(
                ingredientService.addEntry(restaurantId, ingredientId, dto.getQuantity()));
    }

    @GetMapping("/low-stock")
    public Collection<IngredientModel> lowStock(@PathVariable UUID restaurantId) {
        return ingredientAssembler.toCollectionModel(ingredientService.findLowStock(restaurantId));
    }

    // ---------- Ficha técnica ----------

    @GetMapping("/products/{productId}/recipe")
    public Collection<RecipeItemModel> recipe(@PathVariable UUID restaurantId, @PathVariable UUID productId) {
        return recipeItemAssembler.toCollectionModel(recipeService.findByProduct(restaurantId, productId));
    }

    @PutMapping("/products/{productId}/recipe")
    public RecipeItemModel saveRecipeItem(@PathVariable UUID restaurantId, @PathVariable UUID productId,
            @Valid @RequestBody RecipeItemDTO dto) {
        return recipeItemAssembler.toModel(recipeService.save(restaurantId, productId,
                dto.getIngredientId(), dto.getQuantityPerUnit()));
    }

    @DeleteMapping ("/products/{productId}/recipe/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRecipeItem(@PathVariable UUID restaurantId, @PathVariable UUID productId,
            @PathVariable UUID ingredientId) {
        recipeService.remove(restaurantId, productId, ingredientId);
    }
    @GetMapping("/report/summary")
    public List<StockReportService.StockSummaryLine> summary(@PathVariable UUID restaurantId,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
        Instant to = end != null ? end.toInstant() : Instant.now();
        Instant from = start != null ? start.toInstant() : to.minus(7, ChronoUnit.DAYS);
        return stockReportService.summary(restaurantId, from, to);
    }
    // ---------- Relatórios ----------

    @GetMapping("/report/replenishment")
    public List<StockReportService.ReplenishmentLine> replenishment(@PathVariable UUID restaurantId,
            @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam(required = false) @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {
        Instant to = end != null ? end.toInstant() : Instant.now();
        Instant from = start != null ? start.toInstant() : to.minus(7, ChronoUnit.DAYS);
        return stockReportService.replenishment(restaurantId, from, to);
    }

    @GetMapping ("/capacity")
    public List<StockReportService.ProductCapacity> capacity(@PathVariable UUID restaurantId) {
        return stockReportService.capacity(restaurantId);
    }

    private static long zeroIfNull(Long value) {
        return value == null ? 0L : value;
    }
}