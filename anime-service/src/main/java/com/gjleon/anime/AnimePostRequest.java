package com.gjleon.anime;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimePostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
