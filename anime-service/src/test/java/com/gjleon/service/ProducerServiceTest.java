package com.gjleon.service;

import com.gjleon.cammons.ProducerUtils;
import com.gjleon.domain.Producer;
import com.gjleon.repository.ProducerHardCodedRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerHardCodedRepository repository;
    private List<Producer> producerList;
    @InjectMocks
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("findAll returns a list with all producers when argument is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var producers = service.findAll(null);
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("findAll returns a list with found object when argument exist")
    void findAll_ReturnsProducers_WhenArgumentExist() {
        var producer = producerList.getFirst();

        var expectedProducerFound = singletonList(producer);
        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(expectedProducerFound);

        var producerFound = service.findAll(producer.getName());
        Assertions.assertThat(producerFound).containsAll(expectedProducerFound);
    }

    @Test
    @DisplayName("findAll returns empty list when argument not found")
    void findAll_ReturnsEmptyList_WhenArgumentNotFound() {
        var name = "not found";
        BDDMockito.when(repository.findByName(name)).thenReturn(emptyList());

        var producers = repository.findByName(name);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a producer with given id")
    void findById_ReturnsProducersById_WhenSuccessful() {
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var producers = service.findByIdOrThrowNotFound(expectedProducer.getId());

        Assertions.assertThat(producers).isEqualTo(expectedProducer);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when producer not found")
    void findById_ThrowsResponseStatusException_WhenProducerNotFound() {
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a producer")
    void save_CreatesProducer_WhenSuccessful() {
        var producerToSave = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var savedProducer = service.save(producerToSave);

        Assertions.assertThat(savedProducer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete remove a producer")
    void delete_RemoveProducer_WhenSuccessful() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
        BDDMockito.doNothing().when(repository).delete(producerToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when producer not found")
    void delete_ThrowsResponseStatusException_WhenProducerNotFound() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(producerToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a producer")
    void update_UpdatesProducer_WhenSuccessful() {
        var producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Animplex");

        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
        BDDMockito.doNothing().when(repository).update(producerToUpdate);

        service.update(producerToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when producer not found")
    void update_ThrowsResponseStatusException_WhenProducerNotFound() {
        var producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Animplex");

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(producerToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}