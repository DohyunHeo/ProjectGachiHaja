package com.example.projectgachihaja.domain.Together.event;

import com.example.projectgachihaja.domain.Together.Together;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherUpdateEvent {
    private final Together together;

    private final String message;
}
