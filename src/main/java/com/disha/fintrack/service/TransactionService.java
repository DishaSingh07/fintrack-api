package com.disha.fintrack.service;

import com.disha.fintrack.dto.FilterDTO;
import com.disha.fintrack.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    public List<TransactionDTO> getRecentTransactions();

}
