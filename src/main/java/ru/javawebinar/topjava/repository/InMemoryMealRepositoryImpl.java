package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private static List<Meal> mealList = new CopyOnWriteArrayList<>();
    private static AtomicInteger counter = new AtomicInteger(0);
    private static Comparator<Meal> sortedMeal = (o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime());

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.incrementAndGet());
            mealList.add(meal);
        } else {
            Meal mealToUpdate = get(meal.getId());
            mealToUpdate.setDateTime(meal.getDateTime());
            mealToUpdate.setDescription(meal.getDescription());
            mealToUpdate.setCalories(meal.getCalories());
        }
    }

    @Override
    public void delete(int id) {
        mealList.remove(get(id));
    }

    @Override
    public Meal get(int id) {
        return mealList.stream()
                .filter(m -> id == m.getId())
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Meal> getAll() {
        mealList.sort(sortedMeal);
        return mealList;
    }
}
