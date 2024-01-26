package com.study.connection.factory;

import com.study.connection.command.Command;

public interface Factory {
    public Command getCommand (String name);
}
