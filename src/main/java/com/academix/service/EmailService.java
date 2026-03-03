package com.academix.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void enviarCorreoRecuperacion(String destinatario, String nombreUsuario, String enlace) throws MessagingException {
        // 1. Creamos el mensaje "Mime" (permite HTML y archivos)
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, 
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
                StandardCharsets.UTF_8.name());

        // 2. Preparamos los datos para la plantilla HTML (Thymeleaf)
        Context context = new Context();
        context.setVariable("nombreUsuario", nombreUsuario);
        context.setVariable("enlaceRecuperacion", enlace);

        // 3. Procesamos la plantilla que guardamos en /templates/mail/
        String html = templateEngine.process("mail/email-recuperacion", context);

        // 4. Configuramos los detalles del envío
        helper.setTo(destinatario);
        helper.setText(html, true); // El 'true' indica que es contenido HTML
        helper.setSubject("Restablecer tu contraseña - Academix Pro");
        helper.setFrom("soporte@academix.co"); // Esto se puede ajustar después

        // 5. ¡Fuego! Se envía el correo
        mailSender.send(message);
    }
}