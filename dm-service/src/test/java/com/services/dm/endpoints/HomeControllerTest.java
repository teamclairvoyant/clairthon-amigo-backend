package com.services.dm.endpoints;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    HomeController homeController;

    HomeControllerTest() {
        homeController = new HomeController();
    }

    @Test
    void home() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("test");
        Assert.assertNotNull(homeController.home(request));
    }
}