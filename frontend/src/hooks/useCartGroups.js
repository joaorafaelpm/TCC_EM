import { useState, useEffect } from "react";
import api from "../services/api";

/**
 * Agrupa os itens do carrinho por restaurante e enriquece
 * com dados do backend (nome, shippingFee, formas de pagamento).
 *
 * Responsabilidade única: transformar { cartItems, food_list }
 * no objeto restaurantGroups que o PlaceOrder precisa renderizar.
 */
export function useCartGroups(cartItems, food_list) {
  const [restaurantGroups, setRestaurantGroups] = useState({});
  const [isLoadingGroups, setIsLoadingGroups] = useState(false);

  useEffect(() => {
    const itemsInCart = food_list.filter((item) => cartItems[item.id] > 0);
    if (itemsInCart.length === 0) {
      setRestaurantGroups({});
      return;
    }

    // Agrupa localmente por restaurante
    const grouped = itemsInCart.reduce((acc, item) => {
      const rId = item.restaurantId;
      if (!acc[rId])
        acc[rId] = {
          products: [],
          subtotal: 0,
          shippingFee: 0,
          restaurantName: "",
          paymentMethods: [],
          selectedPaymentMethod: "",
        };
      acc[rId].products.push({ ...item, quantity: cartItems[item.id] });
      acc[rId].subtotal += item.price * cartItems[item.id];
      return acc;
    }, {});

    // Enriquece com dados do backend
    const fetchRestaurantData = async () => {
      setIsLoadingGroups(true);
      try {
        const enriched = { ...grouped };
        await Promise.all(
          Object.keys(grouped).map(async (rId) => {
            const [restaurantRes, paymentRes] = await Promise.all([
              api.get(`/v1/restaurants/${rId}`),
              api.get(`/v1/restaurants/${rId}/payment-methods`),
            ]);
            enriched[rId].shippingFee = restaurantRes.data.shippingFee || 0;
            enriched[rId].restaurantName = restaurantRes.data.name;
            enriched[rId].paymentMethods = paymentRes.data["content"] || [];
          }),
        );
        setRestaurantGroups(enriched);
      } finally {
        setIsLoadingGroups(false);
      }
    };

    fetchRestaurantData();
  }, [cartItems, food_list]);

  // Atualiza a forma de pagamento selecionada sem re-buscar do backend
  const setPaymentMethod = (restaurantId, value) => {
    setRestaurantGroups((prev) => ({
      ...prev,
      [restaurantId]: { ...prev[restaurantId], selectedPaymentMethod: value },
    }));
  };

  const grandTotal = Object.values(restaurantGroups).reduce(
    (sum, g) => sum + g.subtotal + g.shippingFee,
    0,
  );

  return { restaurantGroups, isLoadingGroups, setPaymentMethod, grandTotal };
}
