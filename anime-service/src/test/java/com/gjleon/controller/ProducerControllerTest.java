package com.gjleon.controller;

import com.gjleon.cammons.FileUtils;
import com.gjleon.cammons.ProducerUtils;
import com.gjleon.domain.Producer;
import com.gjleon.repository.ProducerData;
import com.gjleon.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = ProducerController.class)
@ComponentScan(basePackages = "com.gjleon")
class ProducerControllerTest {
    private static final String URL = "/v1/producers";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProducerData producerData;
    @SpyBean
    private ProducerHardCodedRepository repository;
    private List<Producer> producerList;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns a list with found object when argument exist")
    void findAll_ReturnsProducers_WhenArgumentExist() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x returns empty list when argument not found")
    void findAll_ReturnsEmptyList_WhenArgumentNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 returns a producer with given id")
    void findById_ReturnsProducersById_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/99 throws ResponseStatusException 404 when producer not found")
    void findById_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");
        var producerToSave = producerUtils.newProducerToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

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
    @DisplayName("PUT v1/producers updates a producer")
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers throws ResponseStatusException when producer not found")
    void update_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {
        var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

    @Test
    @DisplayName("DELETE v1/producers/1 remove a producer")
    void delete_RemoveProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = producerList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers/99 throws ResponseStatusException when producer not found")
    void delete_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }
}