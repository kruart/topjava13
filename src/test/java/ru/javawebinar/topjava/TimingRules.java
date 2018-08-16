package ru.javawebinar.topjava;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class TimingRules implements BeforeAllCallback, AfterAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static final Logger log = LoggerFactory.getLogger("result");

    private StopWatch stopwatch;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        stopwatch = new StopWatch("Execution Time of " + extensionContext.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        log.info("start execution method: " + extensionContext.getRequiredTestClass().getName() + "." + extensionContext.getDisplayName());
        stopwatch.start(extensionContext.getDisplayName());
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        log.info("stop execution method: " + extensionContext.getRequiredTestClass().getName() + "." + extensionContext.getDisplayName());
        stopwatch.stop();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        log.info('\n' + stopwatch.prettyPrint() + '\n');
    }
}
