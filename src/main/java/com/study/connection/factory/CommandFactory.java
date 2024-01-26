package com.study.connection.factory;

import com.study.connection.command.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CommandFactory implements Factory {
    public static Map<String , Command> cachedCommand = new HashMap<>();
    public CommandFactory(){
        cachedCommand.put(CommandName.VIEW_DELETE.name() , new ViewDelete());
        cachedCommand.put(CommandName.VIEW_SELECT.name() , new ViewSelect());

        cachedCommand.put(CommandName.MODIFY_UPDATE.name() , new ModifyUpdate());
        cachedCommand.put(CommandName.MODIFY_SELECT.name(), new ModifySelect());

        cachedCommand.put(CommandName.WRITE_INSERT.name(),  new WriteInsert());
        cachedCommand.put(CommandName.BOARD_SELECT.name(), new BoardSelect());
        cachedCommand.put(CommandName.COMMENT_INSERT.name(), new CommentInsert());
    }

    @Override
    public Command getCommand(String name) {
        if(cachedCommand.get(name) != null) {
            return cachedCommand.get(name);
        } else{
            return null;
        }
    }

}
