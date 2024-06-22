package com.jewelbackend.backend.reports.services.pdf.dto;

import lombok.Data;

import java.awt.*;

@Data
public class TableStyleDTO {
    private TextStyleDTO textStyleDTO;
    private int[] colWidths;
    private int cellHeight;
    private Color fillColor;

    private int colPosition = 0;
    private int xInitialPosition = 0;
}
