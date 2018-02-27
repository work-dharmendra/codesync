package com.codesync.inttest.servlet;

import com.codesync.inttest.common.Util;
import com.codesync.test.common.IntTestExecutor;
import com.codesync.test.common.NewClass;
import com.codesync.test.common.TestDto;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Dharmendra.Singh on 6/11/14.
 */
@WebServlet("/inttest")
public class IntTestServlet extends HttpServlet{

    private static final Logger LOGGER = Logger.getLogger(IntTestServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.FINE, "executing inttest1");
        String postBody = Util.getPostBody(request.getReader());

        LOGGER.log(Level.FINE, "executing inttest for post {0}", postBody);

        Gson gson = new Gson();
        TestDto testDto = gson.fromJson(postBody, TestDto.class);

        IntTestExecutor intTestExecutor = new IntTestExecutor();

        TestDto result = intTestExecutor.execute(testDto);
        response.setContentType("application/json");
        response.setHeader("Accept", "application/json");
        response.setCharacterEncoding("utf-8");

        response.getWriter().print(gson.toJson(result));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().print("IntTestServlet.doGet");
    }
}

