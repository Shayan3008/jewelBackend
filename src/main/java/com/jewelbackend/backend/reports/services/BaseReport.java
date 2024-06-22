package com.jewelbackend.backend.reports.services;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.reports.services.pdf.dto.PdfPositionDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TextStyleDTO;
import com.jewelbackend.backend.reports.services.pdf.util.table.PdfTableUtil;
import com.jewelbackend.backend.reports.services.pdf.util.text.PdfTextUtil;
import com.jewelbackend.backend.setup.services.BaseService;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import com.jewelbackend.backend.common.exceptions.EmptyReportException;

@Component
@Getter
public abstract class BaseReport extends BaseService {

    private final PdfTextUtil pdfTextUtil;
    private final PdfTableUtil pdfTableUtil;

    public BaseReport(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
        pdfTextUtil = new PdfTextUtil();
        pdfTableUtil = new PdfTableUtil();
    }

    public abstract void generateFilters(Map<String, String> parameters);

    public abstract byte[] generateReport(Map<String, String> parameters, String format, String reportName)
            throws EmptyReportException, IOException, ParseException;

    // Method to generate Header
    public void generateHeader(PDPageContentStream pdPageContentStream, int pageWidth, int pageHeight,String title) throws IOException, ParseException {
        // Add Header
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
        String date = dateWithoutTime.toString();
        PdfPositionDTO pdfPositionDTO = new PdfPositionDTO();
        pdfPositionDTO.setX((float) pageWidth / 2 - 50);
        pdfPositionDTO.setY(pageHeight - 20);
        TextStyleDTO headerTextStyle = new TextStyleDTO(16, 14.5f, pdfPositionDTO, date, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), Color.BLACK);
        pdfPositionDTO.setX((float) (pageWidth / 2) - getPdfTextUtil().getFontWidth(headerTextStyle,title) / 2);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(title, headerTextStyle, pdPageContentStream);
        headerTextStyle.setFontSize(8);
        pdfPositionDTO.setX(pageWidth - getPdfTextUtil().getFontWidth(headerTextStyle) - 10);
        pdfPositionDTO.setY(pageHeight - 20);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(date, headerTextStyle, pdPageContentStream);
    }

    public void generateHeader(PDPageContentStream pdPageContentStream, int pageWidth, int pageHeight) throws IOException, ParseException {
        // Add Header
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
        String date = dateWithoutTime.toString();
        PdfPositionDTO pdfPositionDTO = new PdfPositionDTO();
        TextStyleDTO headerTextStyle = new TextStyleDTO(16, 14.5f, pdfPositionDTO, date, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), Color.BLACK);
        headerTextStyle.setFontSize(8);
        pdfPositionDTO.setX(pageWidth - getPdfTextUtil().getFontWidth(headerTextStyle) - 10);
        pdfPositionDTO.setY(pageHeight - 20);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(date, headerTextStyle, pdPageContentStream);
    }

    protected PDPageContentStream changePDFPage(int i, PdfPositionDTO pdfPositionDTO, int totalPages, PDPageContentStream pdPageContentStream, PDDocument pdDocument) throws IOException, ParseException {
        int pageNo = i / 16;
        TextStyleDTO headerTextStyle = new TextStyleDTO(16, 14.5f, pdfPositionDTO, "", new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), Color.BLACK);
        headerTextStyle.setFontSize(8);
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(20);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(String.format("page %s - %s ", pageNo, totalPages), headerTextStyle, pdPageContentStream);

        pdPageContentStream.close();
        PDPage page = new PDPage(PDRectangle.A4);
        pdDocument.addPage(page);
        pdPageContentStream = new PDPageContentStream(pdDocument, page);
        return pdPageContentStream;
    }

}
