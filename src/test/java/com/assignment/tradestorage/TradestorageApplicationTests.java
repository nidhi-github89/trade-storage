package com.assignment.tradestorage;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.assignment.tradestorage.controller.TradeStorageController;
import com.assignment.tradestorage.exception.InvalidTradeException;
import com.assignment.tradestorage.model.Trade;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradestorageApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TradeStorageController tradeController;

	@Test
	void testTradeValidateAndStore_successful() {
		final ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T1", 1, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
		final List<Trade> tradeList = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
	}

	@Test
	void testTradeValidateAndStoreWhenMaturityDatePast() {
		try {
			final LocalDate localDate = getLocalDate(2015, 05, 21);
			final ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T2", 1, localDate));
		} catch (final InvalidTradeException ie) {
			Assertions.assertEquals("Invalid Trade: T2  Trade Id is not found", ie.getMessage());
		}
	}

	@Test
	void testTradeValidateAndStoreWhenOldVersion() {
		// step-1 create trade
		final ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T1", 2, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
		final List<Trade> tradeList = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList.get(0).getVersion());
		Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());

		// step-2 create trade with old version
		try {
			final ResponseEntity responseEntity1 = tradeController
					.tradeValidateStore(createTrade("T1", 1, LocalDate.now()));

		} catch (final InvalidTradeException e) {
			System.out.println(e.getId());
			System.out.println(e.getMessage());
		}
		final List<Trade> tradeList1 = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList1.size());
		Assertions.assertEquals("T1", tradeList1.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList1.get(0).getVersion());
		Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());
	}

	@Test
	void testTradeValidateAndStoreWhenSameVersionTrade() {
		final ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T1", 2, LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity);
		final List<Trade> tradeList = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1", tradeList.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList.get(0).getVersion());
		Assertions.assertEquals("T1B1", tradeList.get(0).getBookId());

		// step-2 create trade with same version
		final Trade trade2 = createTrade("T1", 2, LocalDate.now());
		trade2.setBookId("T1B1V2");
		final ResponseEntity responseEntity2 = tradeController.tradeValidateStore(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity2);
		final List<Trade> tradeList2 = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList2.size());
		Assertions.assertEquals("T1", tradeList2.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList2.get(0).getVersion());
		Assertions.assertEquals("T1B1V2", tradeList2.get(0).getBookId());

		// step-2 create trade with new version
		final Trade trade3 = createTrade("T1", 2, LocalDate.now());
		trade3.setBookId("T1B1V3");
		final ResponseEntity responseEntity3 = tradeController.tradeValidateStore(trade3);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(), responseEntity3);
		final List<Trade> tradeList3 = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList3.size());
		Assertions.assertEquals("T1", tradeList3.get(0).getTradeId());
		Assertions.assertEquals(2, tradeList3.get(0).getVersion());
		Assertions.assertEquals("T1B1V3", tradeList3.get(0).getBookId());

	}

	private Trade createTrade(String tradeId, int version, LocalDate maturityDate) {
		final Trade trade = new Trade();
		trade.setTradeId(tradeId);
		trade.setBookId(tradeId + "B1");
		trade.setVersion(version);
		trade.setCounterParty(tradeId + "Cpty");
		trade.setMaturityDate(maturityDate);
		trade.setExpiredFlag("Y");
		return trade;
	}

	public static LocalDate getLocalDate(int year, int month, int day) {
		final LocalDate localDate = LocalDate.of(year, month, day);
		return localDate;
	}
}