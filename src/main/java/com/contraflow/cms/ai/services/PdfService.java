package com.contraflow.cms.ai.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    /**
     * Render an arbitrary HTML string to PDF bytes. jsoup first cleans the (possibly imperfect)
     * LLM HTML into well-formed XHTML, then OpenHTMLtoPDF renders it.
     */
    public byte[] htmlToPdf(String html) {
        if (html == null || html.isBlank()) {
            throw new IllegalArgumentException("Cannot render empty HTML to PDF");
        }

        // Parse and re-serialize as XML so the renderer gets well-formed XHTML.
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withW3cDocument(w3cDoc, "/");
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to render HTML to PDF: " + e.getMessage(), e);
        }
    }
}
