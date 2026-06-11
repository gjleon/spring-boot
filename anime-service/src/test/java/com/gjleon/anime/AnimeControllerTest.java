package com.gjleon.anime;

import com.gjleon.commons.AnimeUtils;
import com.gjleon.commons.FileUtils;
import com.gjleon.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@ComponentScan(basePackages = {"com.gjleon.anime", "com.gjleon.commons"})
@WebMvcTest(controllers = AnimeController.class)
class AnimeControllerTest {
    private static final String URL = "/v1/animes";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeRepository repository;
    private List<Anime> animeList;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("GET v1/animes returns a list with all animes when argument is null")
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/paginated returns a paginated list of animes")
    void findAllPaginated_ReturnsPaginatedAnimes_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-paginated-200.json");
        var pageRequest = PageRequest.of(0, animeList.size());
        var pageAnime = new PageImpl<Anime>(animeList, pageRequest, 1);

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(pageAnime);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/paginated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=One Piece returns a list with found object when argument exist")
    void findAll_ReturnsAnimes_WhenArgumentExist() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-onePiece-name-200.json");
        var name = "One Piece";
        var onePice = animeList.stream().filter(anime -> anime.getName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(Collections.singletonList(onePice));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=x returns empty list when argument not found")
    void findAll_ReturnsEmptyList_WhenArgumentNotFound() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 returns a anime with given id")
    void findById_ReturnsAnimesById_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-200.json");
        var id = 1L;
        var foundAnime = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 throws NotFound 404 when anime not found")
    void findById_ThrowsNotFound_WhenAnimeNotFound() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("POST v1/animes creates a anime")
    void save_CreatesAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");
        var animeToSave = animeUtils.newAnimeToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/animes updates a anime")
    void update_UpdatesAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");
        var id = 1L;
        var foundAnime = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes throws NotFound when anime not found")
    void update_ThrowsNotFound_WhenAnimeNotFound() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        var response = fileUtils.readResourceFile("anime/put-anime-by-id-404.json");

        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/animes/1 remove a anime")
    void delete_RemoveAnime_WhenSuccessful() throws Exception {
        var id = 1L;
        var foundAnime = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/animes/99 throws NotFound when anime not found")
    void delete_ThrowsNotFound_WhenAnimeNotFound() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        var response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are invalids")
    void save_ReturnsBadRequest_WhenFieldsAreInvalids(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolveException = mvcResult.getResolvedException();

        Assertions.assertThat(resolveException).isNotNull();

        Assertions.assertThat(resolveException.getMessage())
                .contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSource")
    @DisplayName("PUT v1/animes returns bad request when fields are invalids")
    void update_ReturnsBadRequest_WhenFieldsAreInvalids(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolveException = mvcResult.getResolvedException();

        Assertions.assertThat(resolveException).isNotNull();

        Assertions.assertThat(resolveException.getMessage())
                .contains(errors);
    }

    private static Stream<Arguments> postAnimeBadRequestSource() {
        var allRequiredErros = allRequiredErrors();
        return Stream.of(
                Arguments.of("post-request-anime-blank-field-400.json", allRequiredErros),
                Arguments.of("post-request-anime-empty-field-400.json", allRequiredErros)
        );
    }

    private static Stream<Arguments> putAnimeBadRequestSource() {
        var allRequiredErros = allRequiredErrors();
        allRequiredErros.add("The fiel 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-anime-blank-fields-400.json", allRequiredErros),
                Arguments.of("put-request-anime-empty-fields-400.json", allRequiredErros)
        );
    }

    private static List<String> allRequiredErrors() {
        String nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }
}