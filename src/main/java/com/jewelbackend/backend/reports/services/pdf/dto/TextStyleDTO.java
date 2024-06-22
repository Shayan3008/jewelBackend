package com.jewelbackend.backend.reports.services.pdf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

@Data
@AllArgsConstructor
public class TextStyleDTO {

    private int fontSize;
    private float leading;
    private PdfPositionDTO pdfPositionDTO;
    private String text;
    private PDFont pdFont;
    private Color color;

}
