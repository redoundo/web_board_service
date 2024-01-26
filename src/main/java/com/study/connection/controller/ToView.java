package com.study.connection.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * to 는 이동하고 싶은 위치의 이름. forward , include , sendRedirect 중 하나로 이동 가능.
 */
public class ToView {


    public void forward(@NotNull HttpServletRequest req, HttpServletResponse res , String to)
            throws ServletException, IOException {
        req.getRequestDispatcher(to).forward(req , res);
    }

    public void include(@NotNull HttpServletRequest req, HttpServletResponse res , String to)
            throws ServletException, IOException {
        req.getRequestDispatcher(to)
                .include(req , res);
    }

    public void redirect(@NotNull HttpServletResponse res , String to)
            throws ServletException, IOException {
        res.sendRedirect(to);
    }

}
