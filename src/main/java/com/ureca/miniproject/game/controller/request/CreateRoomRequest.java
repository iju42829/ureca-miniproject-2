package com.ureca.miniproject.game.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateRoomRequest {

    private String title;
    private Integer maxPlayer;
}
