package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        MealRepository repo = new InMemoryMealRepositoryImpl();

        printAll(repo.getAll());
        repo.delete(3);
        printAll(repo.getAll());

        System.out.println(repo.get(6));
        System.out.println("=================");

        Meal update = new Meal(6, LocalDateTime.now(), "пельмені", 777);
        repo.save(update);
        printAll(repo.getAll());

        Meal save = new Meal(LocalDateTime.now(), "млинці з сиром", 555);
        repo.save(save);
        printAll(repo.getAll());
    }

    private static void printAll(List<Meal> mealList) {
        mealList.forEach(System.out::println);
        System.out.println("=================");
    }
}
