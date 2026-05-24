package com.gjleon.cammons;

import com.gjleon.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList() {
        Anime onePiece = Anime.builder().id(1L).name("One Piece").build();
        Anime gintama = Anime.builder().id(2L).name("Gintama").build();
        Anime samuraiX = Anime.builder().id(3L).name("Samurai X").build();

        return new ArrayList<>(List.of(onePiece, gintama, samuraiX));
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(99L).name("Helsing").build();
    }
}
