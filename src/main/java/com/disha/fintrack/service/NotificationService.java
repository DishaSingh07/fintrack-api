package com.disha.fintrack.service;

import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final ProfileRepository profileRepository;
    private final ExpenseService expenseService;

    @Value("${app.frontend.url}")
    private String frontendUrl; // Example frontend URL

    //    @Scheduled(cron = "0 * * * * *", zone = "IST")
    @Scheduled(cron = "0 0 23 * * ?", zone = "IST")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Starting daily income and expense reminder job: sendDailyIncomeExpenseReminder");
        List<ProfileEntity> allProfiles = profileRepository.findAll();
        log.info("Profiles found: {}", allProfiles.size());

        for (ProfileEntity profile : allProfiles) {
            try {
                String email = profile.getEmail();
                String name = profile.getFullName();
                String subject = "Daily Reminder: Log Your Income and Expenses";
                String message = String.format("Dear %s,\n\nThis is a friendly reminder to log your income and expenses for today. Keeping track of your finances is essential for effective budgeting and financial planning.\n\nPlease visit your dashboard to add any new transactions: %s/dashboard\n\nBest regards,\nFinTrack Team", name, frontendUrl);


                emailService.sendEmail(email, subject, message);
                log.info("Sent daily reminder email to profile ID: {}", profile.getId());
            } catch (Exception e) {
                log.error("Failed to send email to profile ID: {}. Error: {}", profile.getId(), e.getMessage());
            }
        }


    }

    @Scheduled(cron = "0 0 23 * * ?", zone = "IST")
    @Transactional
    public void sendDailyExpenseSummary() {
        log.info("Starting daily expense summary job: sendDailyExpenseSummary");

        List<ProfileEntity> allProfiles = profileRepository.findAll();

        for (ProfileEntity profile : allProfiles) {
            try {
                List<ExpenseDTO> todaysExpenses =
                        expenseService.getExpensesByDate(profile.getId(), LocalDate.now());
                if (todaysExpenses.isEmpty()) {
                    log.info("No expenses for profile {}, skipping mail", profile.getId());
                    continue;
                }


                double totalAmount = todaysExpenses.stream()
                        .mapToDouble(e -> e.getAmount().doubleValue())
                        .sum();

                StringBuilder table = new StringBuilder();
                table.append("Date       | Name               | Category           | Amount\n");
                table.append("---------------------------------------------------------------\n");

                for (ExpenseDTO expense : todaysExpenses) {
                    table.append(String.format(
                            "%-10s | %-18s | %-18s | ₹%.2f%n",
                            expense.getDate(),
                            expense.getName(),
                            expense.getCategoryName(),
                            expense.getAmount()
                    ));
                }

                table.append("---------------------------------------------------------------\n");
                table.append(String.format("Total Expenses: ₹%.2f%n", totalAmount));

                String message = String.format(
                        "Dear %s,\n\nHere is your expense summary for today:\n\n%s\n\n" +
                                "Keep up the good work in managing your finances!\n\n" +
                                "Best regards,\nFinTrack Team",
                        profile.getFullName(),
                        table.toString()
                );

                emailService.sendEmail(
                        profile.getEmail(),
                        "Your Daily Expense Summary",
                        message
                );

                log.info("Sent daily expense summary email to profile ID: {}", profile.getId());

            } catch (Exception e) {
                log.error(
                        "Failed to send email to profile ID: {}. Error: {}",
                        profile.getId(),
                        e.getMessage()
                );
            }
        }
    }

}
