package com.example.projectgachihaja.domain.Together.event;

import com.example.projectgachihaja.domain.Together.Together;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TogetherCreateEvent {
    private final Together together;
}
