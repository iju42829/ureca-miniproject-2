package com.ureca.miniproject.game.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter @Service
@AllArgsConstructor
@NoArgsConstructor
public class EndGameRequest {
    private List<String> winners;
}
