package com.jewelbackend.backend.reports.services.pdf.util.table;

import com.jewelbackend.backend.reports.services.pdf.dto.TableStyleDTO;
import com.jewelbackend.backend.reports.services.pdf.util.text.PdfTextUtil;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

// Class To Handle All Table Related Pdf Tasks.
public class PdfTableUtil {

    public void addCell(TableStyleDTO tableStyleDTO, PDPageContentStream pdPageContentStream,String text,Boolean isNumber) throws IOException {
        PdfTextUtil pdfTextUtil = new PdfTextUtil();
        // Add Cell to Table
        pdPageContentStream.setNonStrokingColor(1f);
        if(tableStyleDTO.getFillColor() != null) {
            pdPageContentStream.addRect(tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getX(),
                    tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getY(),
                    tableStyleDTO.getColWidths()[tableStyleDTO.getColPosition()], tableStyleDTO.getCellHeight());
            pdPageContentStream.fillAndStroke();
            pdPageContentStream.setNonStrokingColor(tableStyleDTO.getFillColor());
        }
        pdPageContentStream.beginText();
        pdPageContentStream.setFont(tableStyleDTO.getTextStyleDTO().getPdFont(), tableStyleDTO.getTextStyleDTO().getFontSize());
        pdPageContentStream.setLeading(tableStyleDTO.getTextStyleDTO().getLeading());
        pdPageContentStream.setNonStrokingColor(tableStyleDTO.getTextStyleDTO().getColor());
        if(isNumber) {
            float textWidth = pdfTextUtil.getFontWidth(tableStyleDTO.getTextStyleDTO(),text);
            pdPageContentStream.newLineAtOffset(tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getX() + tableStyleDTO.getColWidths()[tableStyleDTO.getColPosition()] - textWidth - 10 ,
                    tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getY() + tableStyleDTO.getCellHeight() - 20);
        }
        else
            pdPageContentStream.newLineAtOffset(tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getX() + 20,
                    tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getY() + tableStyleDTO.getCellHeight() - 20);

        pdPageContentStream.showText(text);
        pdPageContentStream.endText();
        tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().setX(tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getX() + tableStyleDTO.getColWidths()[tableStyleDTO.getColPosition()]);
        tableStyleDTO.setColPosition(tableStyleDTO.getColPosition() + 1);
        if(tableStyleDTO.getColPosition() == tableStyleDTO.getColWidths().length) {
            tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().setX(tableStyleDTO.getXInitialPosition());
            tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().setY(tableStyleDTO.getTextStyleDTO().getPdfPositionDTO().getY() - tableStyleDTO.getCellHeight());
            tableStyleDTO.setColPosition(0);
        }
    }
}
