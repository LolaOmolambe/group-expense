package com.expense.tracker.dto.response;

import com.expense.tracker.dto.UserDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StandupUpdateResponseDTO {

    private String description;

    private UserDTO user;


}
