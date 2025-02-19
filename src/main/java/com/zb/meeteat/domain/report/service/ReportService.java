package com.zb.meeteat.domain.report.service;

import com.zb.meeteat.domain.matching.entity.Matching;
import com.zb.meeteat.domain.matching.repository.MatchingRepository;
import com.zb.meeteat.domain.report.entity.Report;
import com.zb.meeteat.domain.report.repository.ReportRepository;
import com.zb.meeteat.domain.user.entity.User;
import com.zb.meeteat.domain.user.repository.UserRepository;
import com.zb.meeteat.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

  private final AuthService authService;
  private final ReportRepository reportRepository;
  private final MatchingRepository matchingRepository;
  private final UserRepository userRepository;

  public void reportUser(long reportedId, long matchingId) {
    Long userId = authService.getLoggedInUserId();
    Matching matching = matchingRepository.findById(matchingId)
        .orElseThrow(() -> new RuntimeException("Matching not found"));
    User reporter = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Report report = Report.builder()
        .reportedId(reportedId)
        .reporter(reporter)
        .matching(matching)
        .build();
    reportRepository.save(report);
  }

  @Transactional
  public void deleteReport(int reportedId, int matchingId) {
    Long userId = authService.getLoggedInUserId();
    reportRepository.deleteByReportedIdAndReportedIdAndMatchingId(userId, reportedId, matchingId);
  }

  public boolean checkReport(int reportedId, int matchingId) {
    Long userId = authService.getLoggedInUserId();
    Report report = reportRepository.findByReporterIdAndReportedIdAndMatchingId(userId, reportedId,
        matchingId);
    return report != null;
  }
}
