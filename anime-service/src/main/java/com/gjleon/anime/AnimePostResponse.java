package com.gjleon.anime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class AnimePostResponse {
    private Long id;
    private String name;
}
