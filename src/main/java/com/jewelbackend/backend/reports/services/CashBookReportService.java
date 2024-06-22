package com.jewelbackend.backend.reports.services;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jewelbackend.backend.reports.services.pdf.dto.PdfPositionDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TableStyleDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TextStyleDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.common.exceptions.EmptyReportException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.models.CashBook;

import jakarta.persistence.EntityManager;

@Service
public class CashBookReportService extends BaseReport {
    @Autowired
    EntityManager entityManager;

    public CashBookReportService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
                                 AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    @Override
    public void generateFilters(Map<String, String> parameters) {

        throw new UnsupportedOperationException("Unimplemented method 'generateFilters'");
    }

    @Override
    public byte[] generateReport(Map<String, String> parameters, String format, String reportName)
            throws EmptyReportException, IOException, ParseException {
        CriteriaFilter<CashBook> criteriaFilter = new CriteriaFilter<>();
        List<CashBook> currencyTransactions = criteriaFilter.getEntitiesByCriteriaWithSorting(CashBook.class,
                parameters, entityManager, "trnDate");

        if (format.equals("pdf"))
            return generatePdf(parameters, currencyTransactions);

        // Create the header row
        List<String> headers = Arrays.asList("Transaction Type", "Amount",
                "Transaction Date");
        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a new sheet
        Sheet sheet = workbook.createSheet("Cashbook Transactions");
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        // Populate the rows with employee data

        Row headingRow = sheet.createRow(1);
        Cell cell = headingRow.createCell(3);
        Font font = workbook.createFont();
        font.setBold(Boolean.TRUE);
        font.setFontHeight((short) 300);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Cashbook Transactions");

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));

        Row row = sheet.createRow(3);
        for (var i = 0; i < headers.size(); i++) {
            Cell cell2 = row.createCell(i + 1);

            cell2.setCellValue(headers.get(i));
        }
        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        BigDecimal amount = BigDecimal.ZERO;
        for (var i = 0; i < currencyTransactions.size(); i++) {
            Row dataRow = sheet.createRow(4 + i);
            dataRow.createCell(1).setCellValue(currencyTransactions.get(i).getTrnType());
            dataRow.createCell(2).setCellValue(currencyTransactions.get(i).getAmount().toString());
            dataRow.createCell(3).setCellValue(currencyTransactions.get(i).getTrnDate());
            amount = amount.add(currencyTransactions.get(i).getAmount());
        }

        if (parameters.containsKey("trnType")) {
            Row totalRow = sheet.createRow(5 + currencyTransactions.size());
            totalRow.createCell(2).setCellValue("Total Amount");
            totalRow.createCell(3).setCellValue(amount.toString());

        } else {
            Row saleRow = sheet.createRow(5 + currencyTransactions.size());
            Row purchaseRow = sheet.createRow(6 + currencyTransactions.size());
            Row totalRow = sheet.createRow(7 + currencyTransactions.size());
            BigDecimal sale = BigDecimal.ZERO;
            BigDecimal purchase = BigDecimal.ZERO;

            for (var transaction : currencyTransactions) {
                if (transaction.getTrnType().equals(Constants.SALE_CASH)) {
                    sale = sale.add(transaction.getAmount());
                } else {
                    purchase = purchase.add(transaction.getAmount());
                }
            }
            saleRow.createCell(2).setCellValue("Sale Amount");
            saleRow.createCell(3).setCellValue(sale.toString());
            purchaseRow.createCell(2).setCellValue("Purchase Amount");
            purchaseRow.createCell(3).setCellValue(purchase.toString());
            totalRow.createCell(2).setCellValue("Total Amount");
            totalRow.createCell(3).setCellValue(amount.toString());
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] workbookBytes = null;
            workbook.write(outputStream);
            workbookBytes = outputStream.toByteArray();

            HelperUtils.saveReportLocally(workbookBytes, "Cashbook Transaction", "xlsx");
            workbook.close();
            return workbookBytes;
        } catch (IOException e) {
            e.printStackTrace();
            workbook.close();
            return new byte[0];
        }
    }

    public byte[] generatePdf(Map<String, String> parameters, List<CashBook> cashBooks) throws IOException, ParseException {

        PDDocument pdDocument = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        pdDocument.addPage(page1);
//      Get Opening Balance
        String openingBalance = cashBooks.get(0).getOpeningBalance().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int pageWidth = (int) page1.getMediaBox().getWidth();
        int pageHeight = (int) page1.getMediaBox().getHeight();
        int cellSize = (pageWidth / 5) - 10;
        PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, page1);
        PdfPositionDTO pdfPositionDTO = new PdfPositionDTO();
        generateHeader(pdPageContentStream, pageWidth, pageHeight, "Cashbook Transactions");
//        Add Opening Balance
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(pageHeight - 60);
        TextStyleDTO openingBalanceTextStyle = new TextStyleDTO(12, 12.5f, pdfPositionDTO, "Opening Balance: " + openingBalance, new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), Color.BLACK);
        getPdfTextUtil().addTextToPage(String.format("Opening Balance for %s : %s", parameters.get("trnDateLess"), openingBalance), openingBalanceTextStyle, pdPageContentStream);
//       Create table
        int[] cellWidths = {cellSize, cellSize, cellSize, cellSize, cellSize};
        pdfPositionDTO.setX(20);
        pdfPositionDTO.setY(pageHeight - 120);
        TextStyleDTO tableTextStyleDTO = new TextStyleDTO(11, 12.5f, pdfPositionDTO, "", new PDType1Font(Standard14Fonts.FontName.HELVETICA), Color.BLACK);
        TableStyleDTO tableStyleDTO = new TableStyleDTO();
        tableStyleDTO.setColWidths(cellWidths);
        tableStyleDTO.setColPosition(0);
        tableStyleDTO.setCellHeight(30);
        tableStyleDTO.setXInitialPosition(20);
        tableStyleDTO.setFillColor(Color.WHITE);
        tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Transaction Type", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Transaction Date", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Amount", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Starting Amount", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Final Amount", false);
        int rowHeight = 40;
        int pageHeight1 = pageHeight - 120;
        int i = 1;
        int totalPages = (int) Math.ceil(cashBooks.size() / 16.0);
        for (var cashBook : cashBooks) {
            if (i % 16 == 0) {
                pdPageContentStream = changePDFPage(i, pdfPositionDTO, totalPages, pdPageContentStream, pdDocument);
                pdfPositionDTO.setX(20);
                pdfPositionDTO.setY(pageHeight - 120);
                tableStyleDTO.setColWidths(cellWidths);
                tableStyleDTO.setColPosition(0);
                tableStyleDTO.setCellHeight(30);
                tableStyleDTO.setXInitialPosition(20);
                tableStyleDTO.setFillColor(Color.WHITE);
                tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Transaction Type", false);
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Transaction Date", false);
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Amount", false);
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Starting Amount", false);
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Final Amount", false);
                pageHeight1 = pageHeight - 120;
            }
            pageHeight1 -= rowHeight;
            pdfPositionDTO.setY(pageHeight1);
            tableTextStyleDTO.setPdfPositionDTO(pdfPositionDTO);
            tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, cashBook.getTrnType().equals(Constants.SALE_CASH)
                    ? "Credit" : "Debit", false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, sdf.format(cashBook.getTrnDate()), false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, cashBook.getAmount().toString(), true);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, cashBook.getOpeningBalance().toString(), true);
            if (cashBook.getTrnType().equals(Constants.SALE_CASH)) {
                tableTextStyleDTO.setColor(Color.GREEN);
                tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, cashBook.getFinalBalance().toString(), true);
            } else {
                tableTextStyleDTO.setColor(Color.RED);
                tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, cashBook.getFinalBalance().toString(), true);

            }
            tableTextStyleDTO.setColor(Color.BLACK);
            tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
            i += 1;
        }
        int pageNo = (int) Math.ceil(i / 16.0);
        TextStyleDTO headerTextStyle = new TextStyleDTO(16, 14.5f, pdfPositionDTO, "", new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), Color.BLACK);
        headerTextStyle.setFontSize(8);
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(20);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(String.format("page %s - %s ", pageNo, totalPages), headerTextStyle, pdPageContentStream);
        pdPageContentStream.close();
        pdDocument.save("Cashbook Transactions.pdf");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdDocument.save(outputStream);
        pdDocument.close();
        return outputStream.toByteArray();
    }
}
