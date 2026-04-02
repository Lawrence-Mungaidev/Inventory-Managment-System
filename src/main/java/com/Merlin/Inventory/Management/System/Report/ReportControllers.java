package com.Merlin.Inventory.Management.System.Report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportControllers {

    private final ReportService reportService;

    @GetMapping("/today")
    public ResponseEntity<DailyReportDto> getDailyReport(){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getDailyReport());
    }

    @GetMapping("/thisMonth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonthlyReportDto> getThisMonthReport(){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getMonthlyReport());
    }

    @GetMapping("/dateRange")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DateRangeReportDto> getDateRangeReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getDateRangeReportDto(startDate, endDate));
    }
}
