package org.claimapp.client.repository;

import org.claimapp.client.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
}
