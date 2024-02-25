package com.study.connection.error;

import lombok.Getter;

@Getter
public enum SuccessCode {
    SELECT_SUCCESS(200 , "SELECT_00" , "SELECT SUCCESS") ,
    DELETE_SUCCESS(204 , "DELETE_00" , "DELETE SUCCESS") ,
    UPDATE_SUCCESS(205 , "UPDATE_00" , "UPDATE SUCCESS") ,
    INSERT_SUCCESS(201 , "INSERT_00" , "INSERT SUCCESS") ;

    private final int status ;
    private final String codeStatus;
    private final String statusMessage;

    SuccessCode(int status , String codeStatus , String statusMessage){
        this.status = status;
        this.codeStatus = codeStatus;
        this.statusMessage = statusMessage;
    }
}
