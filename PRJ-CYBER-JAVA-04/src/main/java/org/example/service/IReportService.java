package org.example.service;

import java.util.Map;

// Interface cho ReportService (bao cao doanh thu) - ap dung DIP
public interface IReportService {
    Map<String, Object> getRevenueReport(String fromDate, String toDate);
    Map<String, Object> getDailyReport(String date);
    Map<String, Object> getOverallStats();
}
