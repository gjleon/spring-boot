package com.gjleon.anime;

import com.gjleon.commons.AnimeUtils;
import com.gjleon.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;
    @Mock
    private AnimeRepository repository;
    private List<Anime> animeList;
    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns a list with all animes when argument is null")
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var animes = service.findAll(null);
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAll returns a list with found object when argument exist")
    void findAll_ReturnsAnimes_WhenArgumentExist() {
        var anime = animeList.getFirst();
        var expectedAnimeFound = singletonList(anime);
        var name = anime.getName();

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(expectedAnimeFound);

        var animeFound = service.findAll(name);
        Assertions.assertThat(animeFound).containsAll(expectedAnimeFound);
    }

    @Test
    @DisplayName("findAll returns empty list when argument not found")
    void findAll_ReturnsEmptyList_WhenArgumentNotFound() {
        var name = "not found";
        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(emptyList());

        var animes = repository.findByNameIgnoreCase(name);
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns an anime with given id")
    void findById_ReturnsAnimesById_WhenSuccessful() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var animes = service.findByIdOrThrowNotFound(expectedAnime.getId());

        Assertions.assertThat(animes).isEqualTo(expectedAnime);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime not found")
    void findById_ThrowsResponseStatusException_WhenAnimeNotFound() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an anime")
    void save_CreatesAnime_WhenSuccessful() {
        var animeToSave = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        var savedAnime = service.save(animeToSave);

        Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete remove an anime")
    void delete_RemoveAnime_WhenSuccessful() {
        var animeToDelete = animeList.getFirst();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when anime not found")
    void delete_ThrowsResponseStatusException_WhenAnimeNotFound() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an anime")
    void update_UpdatesAnime_WhenSuccessful() {
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Helsing");

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.when(repository.save(animeToUpdate)).thenReturn(animeToUpdate);

        service.update(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when anime not found")
    void update_ThrowsResponseStatusException_WhenAnimeNotFound() {
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Helsing");

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}