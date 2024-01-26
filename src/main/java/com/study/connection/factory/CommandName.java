package com.study.connection.factory;

import com.study.connection.command.*;
import lombok.Getter;

@Getter
public enum CommandName {

    VIEW_DELETE ,
    VIEW_SELECT ,
    MODIFY_UPDATE ,
    MODIFY_SELECT ,
    WRITE_INSERT ,
    BOARD_SELECT ,
    COMMENT_INSERT
    ;
}
