package com.study.connection.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public interface Command {
    public void run (HttpServletRequest req, HttpServletResponse res) throws IOException, Exception;
}
