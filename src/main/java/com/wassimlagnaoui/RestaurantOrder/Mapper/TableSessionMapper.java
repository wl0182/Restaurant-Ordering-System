package com.wassimlagnaoui.RestaurantOrder.Mapper;

import com.wassimlagnaoui.RestaurantOrder.DTO.TableSessionRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.TableSessionResponse;
import com.wassimlagnaoui.RestaurantOrder.model.TableSession;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TableSessionMapper {
    public static TableSession toTableSession(TableSessionRequest tableSessionRequest) {
        TableSession tableSession = new TableSession();
        tableSession.setSessionStart(tableSessionRequest.getSessionStartTime());
        tableSession.setSessionEnd(tableSessionRequest.getSessionEndTime());
        tableSession.setTableNumber(tableSessionRequest.getTableNumber());

        return tableSession;
    }

    public static TableSessionResponse fromTableSession(TableSession tableSession) {
        TableSessionResponse tableSessionResponse = new TableSessionResponse();

        tableSessionResponse.setSessionStartTime(tableSession.getSessionStart());
        tableSessionResponse.setSessionEndTime(tableSession.getSessionEnd());
        tableSessionResponse.setTableNumber(tableSession.getTableNumber());
        tableSessionResponse.setId(tableSession.getId());

        return tableSessionResponse;
    }


}
