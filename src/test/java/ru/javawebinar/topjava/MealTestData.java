package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.*;

public class MealTestData {

    public static final Meal USER_MEAL_1 = new Meal(START_SEQ + 2, LocalDateTime.parse("2018-03-20T11:00"), "пельмешки", 535);
    public static final Meal USER_MEAL_2 = new Meal(START_SEQ + 3, LocalDateTime.parse("2018-03-21T13:00"), "sushi", 407);
    public static final Meal USER_MEAL_3 = new Meal(START_SEQ + 4, LocalDateTime.parse("2018-03-21T20:00"), "spaghetti", 321);

    public static final Meal ADMIN_MEAL_1 = new Meal(START_SEQ + 5, LocalDateTime.parse("2018-03-20T13:30"), "burger", 378);
    public static final Meal ADMIN_MEAL_2 = new Meal(START_SEQ + 6, LocalDateTime.parse("2018-03-21T11:00"), "potatoes", 412);
    public static final Meal ADMIN_MEAL_3 = new Meal(START_SEQ + 7, LocalDateTime.parse("2018-03-21T13:00"), "перекус", 216);
    public static final Meal ADMIN_MEAL_4 = new Meal(START_SEQ + 8, LocalDateTime.parse("2018-03-21T20:00"), "yogurt", 260);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertThat(actual).containsSequence(expected);
    }
}
