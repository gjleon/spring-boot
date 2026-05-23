package com.gjleon.repository;

import com.gjleon.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private List<Anime> animes = new ArrayList<>();

    {
        Anime dragonBall = Anime.builder().id(1L).name("Dragon Ball").build();
        Anime naruto = Anime.builder().id(2L).name("Naruto").build();
        Anime bleach = Anime.builder().id(3L).name("Bleach").build();

        animes.addAll(List.of(dragonBall, naruto, bleach));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
