package com.gjleon.producer;

import com.gjleon.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
    List<Producer> findByNameIgnoreCase(String name);
}
