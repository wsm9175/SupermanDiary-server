package com.lodong.spring.supermandiary.responseentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
public class Message {
    private StatusEnum status;
    private String message;
    private Object data;

    public Message(){
        this.status = StatusEnum.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }

    public Message(StatusEnum status, String message){
        this.status = status;
        this.message = message;
    }

   /* public Message(){
        this.status
        this.data = null;
    }*/


}
