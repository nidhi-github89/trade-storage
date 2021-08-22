package com.assignment.tradestorage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.tradestorage.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, String> {
}