package com.example.projectgachihaja.Together.event;

import com.example.projectgachihaja.Together.Together;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherUpdateEvent {
    private final Together together;

    private final String message;
}
