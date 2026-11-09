package com.pendezzapizza.pendezzapizza_api.api.model.stock;

import java.util.UUID;

import com.pendezzapizza.pendezzapizza_api.domain.model.Ingredient;
import com.pendezzapizza.pendezzapizza_api.domain.model.RecipeItem;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.UnitType;

/**
 * DTOs do módulo de estoque. Todas as quantidades trafegam na unidade base (g,
 * ml ou un):
 * o front converte kg -> g e L -> ml antes de enviar.
 * Se preferir seguir o padrão MapStruct do projeto, é só migrar estes records.
 */
public final class StockDtos {

    private StockDtos() {
    }

    public record IngredientInput(String name, UnitType unit, Long lowStockThreshold, Long initialQuantity) {
    }

    public record IngredientUpdateInput(String name, Long lowStockThreshold) {
    }

    public record StockEntryInput(Long quantity) {
    }

    public record RecipeItemInput(UUID ingredientId, Long quantityPerUnit) {
    }

    public record IngredientModel(UUID id, String name, UnitType unit, long stockQuantity,
            long lowStockThreshold, boolean lowStock) {

        public static IngredientModel from(Ingredient i) {
            return new IngredientModel(i.getId(), i.getName(), i.getUnit(), i.getStockQuantity(),
                    i.getLowStockThreshold(), i.getStockQuantity() <= i.getLowStockThreshold());
        }
    }

    public record RecipeItemModel(UUID ingredientId, String ingredientName, UnitType unit, long quantityPerUnit) {

        public static RecipeItemModel from(RecipeItem r) {
            return new RecipeItemModel(r.getIngredient().getId(), r.getIngredient().getName(),
                    r.getIngredient().getUnit(), r.getQuantityPerUnit());
        }
    }
}