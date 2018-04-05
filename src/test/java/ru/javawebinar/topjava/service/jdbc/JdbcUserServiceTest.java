package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

@ActiveProfiles(value = Profiles.JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
}
