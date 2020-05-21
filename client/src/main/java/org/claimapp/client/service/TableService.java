package org.claimapp.client.service;

import org.claimapp.client.entity.Table;

import java.util.List;

public interface TableService {

    List<Table> getAllListedTables();
    Table getTableByPrivateCode(String privateCode);

}
