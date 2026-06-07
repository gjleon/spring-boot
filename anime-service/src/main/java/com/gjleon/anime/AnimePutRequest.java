package com.gjleon.anime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class AnimePutRequest {
    @NotNull(message = "The fiel 'id' cannot be null")
    private Long id;
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
