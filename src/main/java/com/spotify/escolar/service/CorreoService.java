package com.spotify.escolar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void enviarCodigoVerificacion(String destinatario, String nombre, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(destinatario);
            helper.setSubject("🎵 Tu código de verificación - Spotify Escolar");

            String html = """
                <div style="font-family: Arial, sans-serif; max-width: 500px; margin: auto;
                            background: #121212; color: #fff; padding: 30px; border-radius: 12px;">
                    <h2 style="color: #1DB954; text-align: center;">🎵 Spotify Escolar</h2>
                    <p>Hola <strong>%s</strong>,</p>
                    <p>Gracias por registrarte. Usa el siguiente código para verificar tu cuenta:</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <span style="font-size: 42px; font-weight: bold; letter-spacing: 12px;
                                     color: #1DB954; background: #282828; padding: 16px 28px;
                                     border-radius: 8px;">%s</span>
                    </div>
                    <p style="color: #aaa; font-size: 13px;">
                        Este código expira en <strong>15 minutos</strong>.<br>
                        Si no te registraste, ignora este correo.
                    </p>
                </div>
                """.formatted(nombre, codigo);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo: " + e.getMessage());
        }
    }
}
