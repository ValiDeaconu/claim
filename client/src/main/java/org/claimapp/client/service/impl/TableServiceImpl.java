package org.claimapp.client.service.impl;

import org.claimapp.client.entity.Table;
import org.claimapp.client.repository.TableRepository;
import org.claimapp.client.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    private TableRepository tableRepository;

    @Autowired
    public TableServiceImpl(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public List<Table> getAllListedTables() {
        return tableRepository.findAll()
                .stream()
                .filter(Table::isListed)
                .collect(Collectors.toList());
    }

    @Override
    public Table getTableByPrivateCode(String privateCode) {
        Optional<Table> tableOptional = tableRepository.findAll()
                .stream()
                .filter(table -> table.getPrivateCode().equals(privateCode))
                .findAny();

        return tableOptional.orElse(null);
    }
}
