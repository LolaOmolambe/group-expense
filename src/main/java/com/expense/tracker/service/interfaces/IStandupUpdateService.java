package com.expense.tracker.service.interfaces;

import com.expense.tracker.dto.StandupUpdateDTO;

import java.security.Principal;
import java.util.Map;

public interface IStandupUpdateService {

    StandupUpdateDTO addUpdate(StandupUpdateDTO standupUpdateDTO, Principal principal);

    StandupUpdateDTO getSingleUpdate(Long id);

    Map<String, Object> getStandupForTeam(Long teamId, int page, int pageSize) throws Exception;
}
