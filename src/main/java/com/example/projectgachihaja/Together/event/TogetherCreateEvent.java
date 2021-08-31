package com.example.projectgachihaja.Together.event;

import com.example.projectgachihaja.Together.Together;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherCreateEvent {
    private final Together together;
}
