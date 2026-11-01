package com.example.testapp.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CartTest {

    private Cart cart;
    private Food burger;
    private Food pizza;

    @Before
    public void setUp() {
        cart = new Cart("cart_1", "My Lunch", new ArrayList<>(), "user_123");
        burger = new Food("f1", "Burger", 12.50, null);
        pizza = new Food("f2", "Pizza", 18.00, null);
    }

    @Test
    public void testCartInitialization() {
        Cart emptyCart = new Cart();
        assertNotNull(emptyCart.getFoods());
        assertTrue(emptyCart.getFoods().isEmpty());
    }

    @Test
    public void testCartConstructorWithNullList() {
        Cart nullFoodsCart = new Cart("c1", "Title", null, "u1");
        assertNotNull(nullFoodsCart.getFoods());
        assertTrue(nullFoodsCart.getFoods().isEmpty());
    }

    @Test
    public void testAddAndGetFood() {
        cart.addFood(burger);
        assertEquals(1, cart.getFoods().size());
        assertEquals("Burger", cart.getFood(0).getName());
    }

    @Test
    public void testAddFoodsList() {
        List<Food> items = Arrays.asList(burger, pizza);
        cart.addFoods(items);
        assertEquals(2, cart.getFoods().size());
        assertEquals(30.50, cart.getTotalPrice(), 0.001);
    }

    @Test
    public void testSetFoodsReplacesContent() {
        cart.addFood(burger);
        List<Food> newItems = Collections.singletonList(pizza);
        cart.setFoods(newItems);
        assertEquals(1, cart.getFoods().size());
        assertEquals("Pizza", cart.getFood(0).getName());
    }

    @Test
    public void testRemoveFood() {
        cart.addFood(burger);
        cart.addFood(pizza);
        assertTrue(cart.removeFood(burger));
        assertEquals(1, cart.getFoods().size());
        assertEquals("Pizza", cart.getFood(0).getName());
    }

    @Test
    public void testRemoveFoodByIndex() {
        cart.addFood(burger);
        Food removed = cart.removeFood(0);
        assertEquals(burger, removed);
        assertTrue(cart.getFoods().isEmpty());
    }

    @Test
    public void testRemoveFoodInvalidIndexReturnsNull() {
        assertNull(cart.removeFood(-1));
        assertNull(cart.removeFood(5));
    }

    @Test
    public void testTotalPriceCalculation() {
        cart.addFood(burger);
        cart.addFood(pizza);
        assertEquals(30.50, cart.getTotalPrice(), 0.001);
    }

    @Test
    public void testClearCart() {
        cart.addFood(burger);
        cart.clear();
        assertTrue(cart.getFoods().isEmpty());
        assertEquals(0.0, cart.getTotalPrice(), 0.001);
    }

    @Test
    public void testEqualsAndHashCode() {
        Cart cart1 = new Cart("c1", "Title", null, "u1");
        Cart cart2 = new Cart("c1", "Other Title", null, "u2");
        Cart cart3 = new Cart("c2", "Title", null, "u1");

        assertEquals(cart1, cart2);
        assertNotEquals(cart1, cart3);
        assertEquals(cart1.hashCode(), cart2.hashCode());
    }
}
