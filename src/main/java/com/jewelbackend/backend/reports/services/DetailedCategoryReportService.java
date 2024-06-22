package com.jewelbackend.backend.reports.services;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.EmptyReportException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.reports.services.pdf.dto.PdfPositionDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TableStyleDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TextStyleDTO;
import com.jewelbackend.backend.setup.models.Category;
import com.jewelbackend.backend.setup.models.Item;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class DetailedCategoryReportService extends BaseReport {
    public DetailedCategoryReportService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    @Override
    public void generateFilters(Map<String, String> parameters) {

    }

    @Override
    public byte[] generateReport(Map<String, String> parameters, String format, String reportName) throws EmptyReportException, IOException, ParseException {
        Integer id = Integer.parseInt(parameters.get("categoryId"));
        Category category = this.getDaoFactory().getCategoryDao().findById(id).orElseThrow(() -> new EmptyReportException("Category not found"));
        List<Item> items = category.getItems();
        if (format.equals("PDF")) {
            return generateDetailedCategoryReport(category, items);
        }
        return new byte[0];
    }


    public byte[] generateDetailedCategoryReport(Category category, List<Item> items) throws IOException, ParseException {
        PDDocument pdDocument = new PDDocument();

        PDPage page1 = new PDPage(PDRectangle.A4);
        pdDocument.addPage(page1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int pageWidth = (int) page1.getMediaBox().getWidth();
        int pageHeight = (int) page1.getMediaBox().getHeight();
        int cellSize = (pageWidth / 5) - 20;
        BigDecimal totalPages = BigDecimal.valueOf(items.size()).divide(BigDecimal.valueOf(16), RoundingMode.CEILING);
        PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, page1);
        PdfPositionDTO pdfPositionDTO = new PdfPositionDTO();
        String vendorName = category.getCategoryName();
        int[] cellWidths = {cellSize, cellSize, cellSize, cellSize, cellSize};
        TextStyleDTO vendorNameTextStyle = new TextStyleDTO(12, 12.5f, pdfPositionDTO, "Vendor Name: " + vendorName, new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), Color.BLACK);
        TextStyleDTO tableTextStyleDTO = new TextStyleDTO(11, 12.5f, pdfPositionDTO, "",
                new PDType1Font(Standard14Fonts.FontName.HELVETICA), Color.BLACK);
        TableStyleDTO tableStyleDTO = new TableStyleDTO();
        tableStyleDTO.setColWidths(cellWidths);
        tableStyleDTO.setColPosition(0);
        tableStyleDTO.setCellHeight(30);
        tableStyleDTO.setXInitialPosition(20);
        tableStyleDTO.setFillColor(Color.WHITE);
        tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
        ledgerHeader(pdDocument, pdPageContentStream, pageWidth, pageHeight, pdfPositionDTO, vendorName, vendorNameTextStyle, tableStyleDTO);
        int i = 1;
        int rowHeight = 40;
        int pageHeight1 = pageHeight - 140;
        for (Item item : items) {
            if (i % 16 == 0) {
                pdPageContentStream = changePDFPage(i, pdfPositionDTO, totalPages.intValue(), pdPageContentStream, pdDocument);
                this.ledgerHeader(pdDocument, pdPageContentStream, pageWidth, pageHeight, pdfPositionDTO, vendorName, vendorNameTextStyle, tableStyleDTO);
                pageHeight1 = pageHeight - 140;
            }
            pageHeight1 -= rowHeight;
            pdfPositionDTO.setY(pageHeight1);
            tableTextStyleDTO.setPdfPositionDTO(pdfPositionDTO);
            tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, String.valueOf(i), false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, item.getDesignNo(), false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, item.getQty().toString(), false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, item.getKarat(), false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, item.getNetWeight().toString() + " gms", true);
            i++;

        }
        BigDecimal pageNo = BigDecimal.valueOf(i).divide(BigDecimal.valueOf(16), RoundingMode.CEILING);
        TextStyleDTO headerTextStyle = new TextStyleDTO(16, 14.5f, pdfPositionDTO, "", new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), Color.BLACK);
        headerTextStyle.setFontSize(8);
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(20);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(String.format("page %s - %s ", pageNo, totalPages), headerTextStyle, pdPageContentStream);
        pdPageContentStream.close();
        pdDocument.save("CategoryCompleteReport.pdf");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdDocument.save(outputStream);
        pdDocument.close();
        return outputStream.toByteArray();
    }

    private void ledgerHeader(PDDocument pdDocument, PDPageContentStream pdPageContentStream, int pageWidth, int pageHeight, PdfPositionDTO pdfPositionDTO,
                              String vendorName, TextStyleDTO vendorNameTextStyle, TableStyleDTO tableStyleDTO) throws IOException, ParseException {
        generateHeader(pdPageContentStream, pageWidth, pageHeight, "Detailed Category Report");

        TextStyleDTO headerTextStyle = new TextStyleDTO(16, 14.5f, pdfPositionDTO, vendorName, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), Color.BLACK);
        ;

        pdfPositionDTO.setX(20);
        pdfPositionDTO.setY(pageHeight - 80);
        vendorNameTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage("Item Name:" + vendorName, vendorNameTextStyle, pdPageContentStream);

        //       Create table
        pdfPositionDTO.setX(20);
        pdfPositionDTO.setY(pageHeight - 140);

        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "SNo", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Design no", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Qty", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Purity", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Net Weight", false);
    }
}
