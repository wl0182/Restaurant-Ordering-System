package com.wassimlagnaoui.RestaurantOrder.Controller;


import com.wassimlagnaoui.RestaurantOrder.DTO.ItemSummaryDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.StartSessionDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.EndSessionResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.SessionSummary;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.StartSessionResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.TableSessionResponse;
import com.wassimlagnaoui.RestaurantOrder.Service.TableService;
import com.wassimlagnaoui.RestaurantOrder.Service.TableSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sessions")
public class TableSessionController {
    @Autowired
    private final TableSessionService tableSessionService;

    @Autowired
    private final TableService tableService;



    public TableSessionController(TableSessionService tableSessionService,TableService tableService) {
        this.tableSessionService = tableSessionService;
        this.tableService = tableService;
    }

    @PostMapping("/start")
    public ResponseEntity<StartSessionResponse> startSession(@RequestBody @Valid StartSessionDTO startSessionDTO){
        StartSessionResponse tableSessionResponse = tableSessionService.startSession(startSessionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tableSessionResponse);
    }



    @PutMapping("/{tableNumber}/end")
    public ResponseEntity<EndSessionResponse> endSession(@PathVariable("tableNumber") String tableNumber){
        EndSessionResponse tableSessionResponse = tableSessionService.endSession(tableNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(tableSessionResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableSessionResponse> getSessionById(@PathVariable Long id){
        TableSessionResponse tableSessionResponse = tableSessionService.getSessionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(tableSessionResponse);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TableSessionResponse>> getActiveSessions(){
        List<TableSessionResponse> tableSessionResponses = tableSessionService.getActiveTableSessions();
        return ResponseEntity.status(HttpStatus.OK).body(tableSessionResponses);
    }

    @GetMapping("/tables")
    public List<Map<String, String>> getAllTables() {
        return tableService.getTableNames()
                .stream()
                .map(name -> Map.of("tableName", name))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/item-summary")
    public ResponseEntity<List<ItemSummaryDTO>> getItemSummary(@PathVariable Long id) {
        List<ItemSummaryDTO> summary = tableSessionService.getItemSummaryForSession(id);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{id}/item-names")
    public ResponseEntity<List<String>> getAllItemNames(@PathVariable Long id) {
        List<String> itemNames = tableSessionService.getAllOrderedItemNames(id);
        return ResponseEntity.ok(itemNames);
    }


    @GetMapping("/active/{tableNumber}")
    public ResponseEntity<TableSessionResponse> getActiveSessionByTable(@PathVariable String tableNumber) {
        TableSessionResponse session = tableSessionService.findActiveSessionByTableNumber(tableNumber);
        return ResponseEntity.ok(session);
    }


    @GetMapping("/{id}/checkout-summary")
    public ResponseEntity<SessionSummary> getSessionSummaryForCheckout(@PathVariable Long id){
        SessionSummary sessionSummary = tableSessionService.getSessionSummaryForCheckout(id);
        return ResponseEntity.ok(sessionSummary);
    }




}
