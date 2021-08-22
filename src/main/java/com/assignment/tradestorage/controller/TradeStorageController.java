package com.assignment.tradestorage.controller;

import com.assignment.tradestorage.exception.InvalidTradeException;
import com.assignment.tradestorage.model.Trade;
import com.assignment.tradestorage.service.TradeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeStorageController {

    @Autowired
    TradeStorageService tradeService;
    /*
     */
    @PostMapping("/trade")
    public ResponseEntity<String> tradeValidateStore(@RequestBody Trade trade){
       if(tradeService.isValid(trade)) {
           tradeService.persist(trade);
       }else{
           throw new InvalidTradeException(trade.getTradeId()+"  Trade Id is not found");
       }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/trade")
    public List<Trade> findAllTrades(){
        return tradeService.findAll();
    }
}