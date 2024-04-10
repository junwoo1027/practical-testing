package sample.cafekiosk.spring.api.service.mail;

import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@Service
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public MailService(MailSendClient mailSendClient, MailSendHistoryRepository mailSendHistoryRepository) {
        this.mailSendClient = mailSendClient;
        this.mailSendHistoryRepository = mailSendHistoryRepository;
    }

    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {
        boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, content);
        if (result) {
            mailSendHistoryRepository.save(
                new MailSendHistory(
                    fromEmail,
                    toEmail,
                    subject,
                    content
                )
            );
            return true;
        }

        return false;
    }
}
