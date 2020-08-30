package org.jiahuan.controller;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletResponse;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SysIcomControllerTest {

    @Autowired
//    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

//    @Before
//    public void setup(){
//        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }

    @Test
    public void addIcom() {
    }

    @Test
    public void deleteIcom() {
    }

    @Test
    public void getIcom() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/icom/getById").param("id", "1");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        System.out.println("内容"+mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void getAll() {
    }

    @Test
    public void getPageIcom() {
    }

    @Test
    public void updateIcom() {
    }
}
