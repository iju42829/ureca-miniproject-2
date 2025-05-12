package com.ureca.miniproject.game.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class ListGameResultResponse {
    List<GameResultResponse> gameResults;
}
