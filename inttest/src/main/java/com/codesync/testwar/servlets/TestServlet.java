package com.codesync.testwar.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Dharmendra.Singh on 6/11/14.
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        System.out.println("-pllqqqqqqq---");
        System.out.println("Cl");

        PrintWriter writer = httpServletResponse.getWriter();

        writer.write("DD");

        /*NewClass newClass = new NewClass();
        newClass.test();*/


    }
}

