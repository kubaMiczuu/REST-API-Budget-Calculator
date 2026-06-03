package org.jakubmiczek.restapibudgetcalculator.controller;

import lombok.RequiredArgsConstructor;
import org.jakubmiczek.restapibudgetcalculator.dto.SummaryResponse;
import org.jakubmiczek.restapibudgetcalculator.service.SummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/summaries")
class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("{/id}")
    public SummaryResponse summary(@PathVariable Long id){
        return summaryService.accountSummary(id);
    }
}
