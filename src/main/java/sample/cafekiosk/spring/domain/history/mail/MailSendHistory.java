package sample.cafekiosk.spring.domain.history.mail;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import sample.cafekiosk.spring.domain.BaseEntity;

@Entity
public class MailSendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromEmail;

    private String toEmail;

    private String subject;

    private String content;

    protected MailSendHistory() {
    }

    public MailSendHistory(String fromEmail, String toEmail, String subject, String content) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

}
