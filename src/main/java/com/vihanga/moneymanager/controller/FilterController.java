package com.vihanga.moneymanager.controller;

import com.vihanga.moneymanager.dto.ExpenseDto;
import com.vihanga.moneymanager.dto.FilterDto;
import com.vihanga.moneymanager.dto.IncomeDto;
import com.vihanga.moneymanager.service.ExpenseService;
import com.vihanga.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDto filter){
        //preparing the data or validation
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate():LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate():LocalDate.now();
        String keyword = filter.getKeyword()!=null ? filter.getKeyword():"";
        String sortField = filter.getSortField()!=null ? filter.getSortField():"date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);

        if ("income".equals(filter.getType())){
            List<IncomeDto>incomes = incomeService.filterIncomes(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(incomes);
        }else if ("expense".equalsIgnoreCase(filter.getType())){
            List<ExpenseDto> expenses = expenseService.filterExpenses(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(expenses);
        }else {
            return ResponseEntity.badRequest().body("Invalid type must be 'income' or 'expense'");
        }
    }
}
