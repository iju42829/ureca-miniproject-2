package com.ureca.miniproject.game.controller.request;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter @Service
public class EndGameRequest {
    private List<String> winners;
}
