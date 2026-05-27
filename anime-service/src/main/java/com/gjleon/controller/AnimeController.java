package com.gjleon.controller;

import com.gjleon.mapper.AnimeMapper;
import com.gjleon.request.AnimePostRequest;
import com.gjleon.request.AnimePutRequest;
import com.gjleon.resonse.AnimeGetResponse;
import com.gjleon.resonse.AnimePostResponse;
import com.gjleon.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeMapper mapper;
    private final AnimeService service;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received for list All animes, param name: '{}'", name);
        var animeList = service.findAll(name);
        var animeGetResponseList = mapper.toAnimeGetResponseList(animeList);

        return ResponseEntity.ok(animeGetResponseList);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);
        var anime = service.findByIdOrThrowNotFound(id);
        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest postRequest) {
        log.debug("Request to save anim: {}", postRequest.toString());
        var anime = mapper.toAnime(postRequest);

        var animeSaved = service.save(anime);

        var animePostResponse = mapper.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime: {}", request);

        var animeUpdate = mapper.toAnime(request);

        service.update(animeUpdate);

        return ResponseEntity.noContent().build();
    }
}
