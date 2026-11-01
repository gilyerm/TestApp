package com.example.testapp.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class FoodTest {

    @Test
    public void testFoodGettersAndSetters() {
        Food food = new Food();
        food.setId("food_1");
        food.setName("Pizza");
        food.setPrice(15.99);
        food.setImageBase64("base64data");

        assertEquals("food_1", food.getId());
        assertEquals("Pizza", food.getName());
        assertEquals(15.99, food.getPrice(), 0.001);
        assertEquals("base64data", food.getImageBase64());
    }

    @Test
    public void testCopyConstructor() {
        Food original = new Food("f1", "Burger", 10.00, "img_data");
        Food copy = new Food(original);

        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getPrice(), copy.getPrice(), 0.001);
        assertEquals(original.getImageBase64(), copy.getImageBase64());
    }

    @Test
    public void testEqualsAndHashCode() {
        Food f1 = new Food("f1", "Burger", 10.00, null);
        Food f2 = new Food("f1", "Cheeseburger", 12.00, null);
        Food f3 = new Food("f2", "Burger", 10.00, null);

        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}
