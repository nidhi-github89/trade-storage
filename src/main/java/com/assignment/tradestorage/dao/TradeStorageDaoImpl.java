package com.assignment.tradestorage.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.assignment.tradestorage.model.Trade;

@Repository
public class TradeStorageDaoImpl implements TradeStorageDao {

	public void save(Trade trade) {
		trade.setCreatedDate(LocalDate.now());
		tradeMap.put(trade.getTradeId(), trade);
	}

	public List<Trade> findAll() {
		return tradeMap.values().stream().collect(Collectors.toList());
	}

	public Trade findTrade(String tradeId) {
		return tradeMap.get(tradeId);
	}
}