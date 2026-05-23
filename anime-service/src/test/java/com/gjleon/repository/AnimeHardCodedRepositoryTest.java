package com.gjleon.repository;

import com.gjleon.cammons.AnimeUtils;
import com.gjleon.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;
    @Mock
    private AnimeData animeData;
    private List<Anime> animeList;
    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        BDDMockito.given(animeData.getAnimes()).willReturn(animeList);
        var animes = repository.findAll();

        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findById returns an anime when given id")
    void findById_ReturnsAnimeById_WhenSuccessful() {
        BDDMockito.given(animeData.getAnimes()).willReturn(animeList);

        var expectedAnime = animeList.getFirst();
        var foundAnime = repository.findById(expectedAnime.getId());

        Assertions.assertThat(foundAnime).isPresent().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns a empty list when name is null")
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.given(animeData.getAnimes()).willReturn(animeList);

        var anime = repository.findByName(null);
        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exist")
    void findByName_ReturnsFoundAnimeInList_WhenNameExist() {
        BDDMockito.given(animeData.getAnimes()).willReturn(animeList);
        var expectedAnime = animeList.getFirst();
        var foundAnime = repository.findByName(expectedAnime.getName());

        Assertions.assertThat(foundAnime).hasSize(1).contains(expectedAnime);
    }

    @Test
    @DisplayName("save creates an anime")
    void save_CreatesAnime_WhenSuccessful() {
        BDDMockito.given(animeData.getAnimes()).willReturn(animeList);

        var animeToSave = animeUtils.newAnimeToSave();
        var anime = repository.save(animeToSave);

        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeSavedOptional = repository.findById(anime.getId());

        Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes an anime")
    void delete_RemoveAnime_WhenSuccessful() {
        BDDMockito.given(animeData.getAnimes()).willReturn(animeList);

        var animeToDelete = animeList.getFirst();
        repository.delete(animeToDelete);

        var animes = repository.findAll();

        Assertions.assertThat(animes).isNotEmpty().doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("update updates an anime")
    void update_UpdatesAnime_WhenSuccessful() {
        BDDMockito.given(animeData.getAnimes()).willReturn(animeList);

        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Naruto");

        repository.update(animeToUpdate);

        Assertions.assertThat(this.animeList).contains(animeToUpdate);

        var animeUpdatedOptional = repository.findById(animeToUpdate.getId());

        Assertions.assertThat(animeUpdatedOptional).isPresent();
        Assertions.assertThat(animeUpdatedOptional.get().getName()).isEqualTo("Naruto");
    }
}