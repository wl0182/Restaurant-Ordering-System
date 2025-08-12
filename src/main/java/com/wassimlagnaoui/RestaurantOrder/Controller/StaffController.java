package com.wassimlagnaoui.RestaurantOrder.Controller;

import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.AddStaffDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.staffAddedResponse;
import com.wassimlagnaoui.RestaurantOrder.Service.StaffManagementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wassimlagnaoui.RestaurantOrder.DTO.Response.StaffInfoDTO;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    private final StaffManagementService staffManagementService;

    public StaffController(StaffManagementService staffManagementService) {
        this.staffManagementService = staffManagementService;
    }

    @PostMapping("/add")
    public ResponseEntity<staffAddedResponse> addStaff(@RequestBody @Valid AddStaffDTO dto) {
        staffAddedResponse response = staffManagementService.addStaff(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StaffInfoDTO>> getAllStaff() {
        List<StaffInfoDTO> staffList = staffManagementService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }
}
