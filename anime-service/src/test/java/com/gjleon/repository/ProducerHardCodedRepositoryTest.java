package com.gjleon.repository;

import com.gjleon.cammons.ProducerUtils;
import com.gjleon.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest {
    @InjectMocks
    private ProducerHardCodedRepository repository;
    @Mock
    private ProducerData producerData;
    private List<Producer> producerList;
    @InjectMocks
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producerList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("findAll returns a list with all producers")
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findAll();
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("findById returns a producer with given id")
    void findById_ReturnsProducersById_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducers = producerList.getFirst();
        var producers = repository.findById(expectedProducers.getId());

        Assertions.assertThat(producers).isPresent().contains(expectedProducers);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findByName(null);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exist")
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducers = producerList.getFirst();
        var producers = repository.findByName(expectedProducers.getName());

        Assertions.assertThat(producers).contains(expectedProducers);
    }

    @Test
    @DisplayName("save creates a producer")
    void save_CreatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producerToSave = producerUtils.newProducerToSave();
        var producer = repository.save(producerToSave);

        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        var producerSavedOptional = repository.findById(producerToSave.getId());

        Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete remove a producer")
    void delete_RemoveProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToDelete = producerList.getFirst();
        repository.delete(producerToDelete);
        var producers = repository.findAll();

        Assertions.assertThat(producers).isNotEmpty().doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("update updates a producer")
    void update_UpdatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToUpdate = this.producerList.getFirst();
        producerToUpdate.setName("Animplex");
        repository.update(producerToUpdate);

        Assertions.assertThat(this.producerList).contains(producerToUpdate);

        var producerUpdatedOptional = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerUpdatedOptional).isPresent();
        Assertions.assertThat(producerUpdatedOptional.get().getName()).isEqualTo("Animplex");
    }
}