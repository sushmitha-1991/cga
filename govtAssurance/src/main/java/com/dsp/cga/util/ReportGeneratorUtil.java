package com.dsp.cga.util;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportGeneratorUtil {

    @Autowired
    private TemplateEngine templateEngine;

   public byte[] generatePdf(String templateName, Context context) throws Exception {
        System.out.println("generating PDF...");
        String htmlContent = "";
        try {
            htmlContent = templateEngine.process(templateName, context);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);

            FontProvider provider = new FontProvider();
            provider.addFont("fonts/tahoma-bold.ttf");
            provider.addFont("fonts/NotoSansDevanagari-Regular.ttf");

            System.out.println("Applying template...");
            ConverterProperties properties = new ConverterProperties();
            properties.setFontProvider(provider);
            System.out.println("Converting to PDF...");
            HtmlConverter.convertToPdf(htmlContent, pdf, properties);
            
            pdf.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("HTML: {}", htmlContent);
            e.printStackTrace(System.err);
            throw e;
        }
    }

}