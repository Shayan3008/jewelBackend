package com.jewelbackend.backend.reports.services.pdf.util.text;

import com.jewelbackend.backend.reports.services.pdf.dto.TextStyleDTO;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.List;

// class to handle all text related pdf tasks.
public class PdfTextUtil {
    // create a method which adds text in pdfbox.
    public void addTextToPage(String text, TextStyleDTO textStyleDTO, PDPageContentStream contentStream) throws IOException {
        // add text to page
        setupTextStyle(textStyleDTO, contentStream);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.moveTo(0, 0);

    }

    public void addMultiLineTextToPage(List<String> text, TextStyleDTO textStyleDTO, PDPageContentStream contentStream) throws IOException {

        setupTextStyle(textStyleDTO, contentStream);
        for (String line : text) {
            contentStream.showText(line);
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.moveTo(0, 0);

    }

    public float getFontWidth(TextStyleDTO textStyleDTO) throws IOException {
        return textStyleDTO.getPdFont().getStringWidth(textStyleDTO.getText()) / 1000 * textStyleDTO.getFontSize();
    }

    // Overloaded method which allows to send text.
    public float getFontWidth(TextStyleDTO textStyleDTO, String text) throws IOException {
        return textStyleDTO.getPdFont().getStringWidth(text) / 1000 * textStyleDTO.getFontSize();
    }


    private void setupTextStyle(TextStyleDTO textStyleDTO, PDPageContentStream contentStream) throws IOException {
        contentStream.beginText();
        contentStream.setFont(textStyleDTO.getPdFont(), textStyleDTO.getFontSize());
        contentStream.setLeading(textStyleDTO.getLeading());
        contentStream.setNonStrokingColor(textStyleDTO.getColor());
        contentStream.newLineAtOffset(textStyleDTO.getPdfPositionDTO().getX(), textStyleDTO.getPdfPositionDTO().getY());
    }


}
