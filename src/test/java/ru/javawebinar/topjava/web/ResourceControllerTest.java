package ru.javawebinar.topjava.web;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    public void testResources() throws Exception {
        mockMvc.perform(get("/resources/css/style.css"))
//                .andDo(print())
                .andExpect(content().contentType("text/css"))
                .andExpect(status().isOk());
    }
}
