package org.jsp.stocks.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class StockHistoryService {

    // Get last 7 days of stock data
    public List<Map<String, Object>> getLast7DaysHistory(String ticker) {
        List<Map<String, Object>> historyData = new ArrayList<>();
        
        // For now, return mock data (this will work even without internet)
        return generateMockHistoryData(ticker);
    }
    
    // Mock data for testing - creates realistic looking stock data
    private List<Map<String, Object>> generateMockHistoryData(String ticker) {
        List<Map<String, Object>> mockData = new ArrayList<>();
        Random random = new Random();
        
        // Starting price between ₹500 and ₹5000 based on ticker
        double basePrice = getBasePriceForTicker(ticker);
        
        // Generate 7 days of data (today + 6 previous days)
        for (int i = 6; i >= 0; i--) {
            Map<String, Object> point = new HashMap<>();
            LocalDate date = LocalDate.now().minusDays(i);
            
            // Calculate daily variation (-5% to +5%)
            double variation = (random.nextDouble() - 0.5) * 10;
            double closePrice = basePrice * (1 + variation / 100);
            double openPrice = closePrice * (1 + (random.nextDouble() - 0.5) * 2 / 100);
            double highPrice = Math.max(openPrice, closePrice) * (1 + random.nextDouble() * 2 / 100);
            double lowPrice = Math.min(openPrice, closePrice) * (1 - random.nextDouble() * 2 / 100);
            
            point.put("date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            point.put("close", closePrice);
            point.put("open", openPrice);
            point.put("high", highPrice);
            point.put("low", lowPrice);
            point.put("volume", 10000 + random.nextInt(90000));
            
            mockData.add(point);
            
            // Next day's base price is today's close
            basePrice = closePrice;
        }
        
        return mockData;
    }
    
    // Set realistic base prices for different stocks
    private double getBasePriceForTicker(String ticker) {
        switch(ticker.toUpperCase()) {
            case "RELIANCE": return 2850;
            case "TCS": return 3850;
            case "INFY": return 1520;
            case "HDFC": return 1680;
            case "WIPRO": return 420;
            case "SBIN": return 680;
            case "ITC": return 430;
            case "TATAMOTOR": return 780;
            default: return 1000; // default price
        }
    }
}