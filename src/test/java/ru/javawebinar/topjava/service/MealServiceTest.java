package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(100005, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(100005, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(100005, ADMIN_ID);
        List<Meal> actual = service.getAll(ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL_4, ADMIN_MEAL_3, ADMIN_MEAL_2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(100005, USER_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actual = service.getBetweenDateTimes(LocalDateTime.parse("2018-03-20T11:00"), LocalDateTime.parse("2018-03-21T14:00"), ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL_3, ADMIN_MEAL_2, ADMIN_MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        assertMatch(actual, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(USER_MEAL_1);
        updatedMeal.setDescription("Cookies");
        service.update(updatedMeal, USER_ID);

        Meal actual = service.get(updatedMeal.getId(), USER_ID);
        assertMatch(actual, updatedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(USER_MEAL_1, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal sandwich = new Meal(LocalDateTime.parse("2018-03-21T22:00"), "Sandwich", 333);
        Meal created = service.create(sandwich, USER_ID);
        sandwich.setId(created.getId());

        List<Meal> actualMealList = service.getAll(USER_ID);
        assertMatch(actualMealList, sandwich, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);

        Meal actual = service.get(sandwich.getId(), USER_ID);
        assertMatch(actual, sandwich);
    }
}