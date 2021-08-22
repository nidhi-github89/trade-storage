package com.assignment.tradestorage.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.assignment.tradestorage.model.Trade;

public interface TradeStorageDao {

	public static Map<String, Trade> tradeMap = new ConcurrentHashMap<String, Trade>();

	public void save(Trade trade);

	List<Trade> findAll();

	Trade findTrade(String tradeId);

}