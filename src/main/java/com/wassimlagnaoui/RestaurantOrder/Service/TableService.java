package com.wassimlagnaoui.RestaurantOrder.Service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    private final List<String> tableNames = List.of("T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10","T11","T12");

    public List<String> getTableNames() {
        return tableNames;
    }
}
