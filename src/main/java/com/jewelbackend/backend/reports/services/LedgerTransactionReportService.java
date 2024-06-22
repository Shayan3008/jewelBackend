package com.jewelbackend.backend.reports.services;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.EmptyReportException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.reports.services.pdf.dto.PdfPositionDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TableStyleDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TextStyleDTO;
import com.jewelbackend.backend.setup.models.LedgerTransaction;
import com.jewelbackend.backend.setup.models.Vendor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class LedgerTransactionReportService extends BaseReport {
    public LedgerTransactionReportService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    @Override
    public void generateFilters(Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] generateReport(Map<String, String> parameters, String format, String reportName) throws EmptyReportException, IOException, ParseException {
        Integer id = Integer.parseInt(parameters.get("vendorId"));
        Optional<Vendor> vendorOptional = this.getDaoFactory().getVendorDao().findById(id);
        if (vendorOptional.isEmpty())
            throw new EmptyReportException("Vendor not found");
        Vendor vendor = vendorOptional.get();
        List<LedgerTransaction> ledgerTransactions = this.getDaoFactory().getLedgerTransactionDao().findByVendor(vendor);
        if (format.equals("PDF")) {
            return generatePdf(ledgerTransactions);
        }
        return null;
    }

    public byte[] generatePdf(List<LedgerTransaction> ledgerTransactions) throws IOException, ParseException {
        PDDocument pdDocument = new PDDocument();

        PDPage page1 = new PDPage(PDRectangle.A4);
        pdDocument.addPage(page1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int pageWidth = (int) page1.getMediaBox().getWidth();
        int pageHeight = (int) page1.getMediaBox().getHeight();
        int cellSize = (pageWidth / 5);
        PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, page1);
        PdfPositionDTO pdfPositionDTO = new PdfPositionDTO();
        String vendorName = ledgerTransactions.get(0).getVendor().getName();
        BigDecimal credit = BigDecimal.ZERO;
        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal creditGoldWeight = BigDecimal.ZERO;
        BigDecimal debitGoldWeight = BigDecimal.ZERO;
        for (var vendorTransaction : ledgerTransactions) {
            if (vendorTransaction.getDebit() != null)
                debit = debit.add(vendorTransaction.getDebit());
            if (vendorTransaction.getCredit() != null)
                credit = credit.add(vendorTransaction.getCredit());
            if (vendorTransaction.getCreditGoldWeight() != null)
                creditGoldWeight = creditGoldWeight.add(vendorTransaction.getCreditGoldWeight());
            if (vendorTransaction.getDebitGoldWeight() != null)
                debitGoldWeight = debitGoldWeight.add(vendorTransaction.getDebitGoldWeight());
        }
        int[] cellWidths = {cellSize - 20, cellSize - 20, cellSize, cellSize, cellSize - 20};
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
        int pageHeight1 = pageHeight - 120;
        int totalPages = ledgerTransactions.size() / 16;
        for (LedgerTransaction ledgerTransaction : ledgerTransactions) {
            if (i % 16 == 0) {
                pdPageContentStream = changePDFPage(i, pdfPositionDTO, totalPages, pdPageContentStream, pdDocument);
                this.ledgerHeader(pdDocument, pdPageContentStream, pageWidth, pageHeight, pdfPositionDTO, vendorName, vendorNameTextStyle, tableStyleDTO);
                pageHeight1 = pageHeight - 120;
            }
            pageHeight1 -= rowHeight;
            pdfPositionDTO.setY(pageHeight1);
            tableTextStyleDTO.setPdfPositionDTO(pdfPositionDTO);
            tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, String.valueOf(i), false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, sdf.format(ledgerTransaction.getTrnDate()), false);
            if (ledgerTransaction.getDebitGoldWeight() == null) {
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "0.0", true);
            } else {
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, ledgerTransaction.getDebitGoldWeight().toString(), true);
            }
            if (ledgerTransaction.getCreditGoldWeight() == null) {
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "0.0", true);
            } else {
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, ledgerTransaction.getCreditGoldWeight().toString(), true);
            }
            if (ledgerTransaction.getDebit() == null && ledgerTransaction.getCredit() == null) {
                getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "0.0", true);
            } else {
                if (ledgerTransaction.getDebit() == null) {
                    getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Credit: " + ledgerTransaction.getCredit().toString(), true);
                } else {
                    getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Debit: " + ledgerTransaction.getDebit().toString(), true);
                }
            }

            i++;
        }

        int pageNo = i / 16;
        TextStyleDTO headerTextStyle = new TextStyleDTO(16, 14.5f, pdfPositionDTO, "", new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD), Color.BLACK);
        headerTextStyle.setFontSize(8);
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(20);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(String.format("page %s - %s ", pageNo, totalPages), headerTextStyle, pdPageContentStream);
        if (pageHeight1 < 190) {
            pdPageContentStream = changePDFPage(i, pdfPositionDTO, totalPages, pdPageContentStream, pdDocument);
//            this.ledgerHeader(pdDocument, pdPageContentStream, pageWidth, pageHeight, pdfPositionDTO, vendorName, vendorNameTextStyle, tableStyleDTO);
            pageHeight1 = pageHeight - 20;
            pdfPositionDTO.setX(20);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Credit Transaction Amount: Rs " + credit, vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Debit Transaction Amount: Rs " + debit, vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Credit Gold Weight : " + creditGoldWeight + " gms", vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Debit Gold Weight : " + debitGoldWeight + " gms", vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            if (debitGoldWeight.compareTo(creditGoldWeight) <= 0) {
                vendorNameTextStyle.setColor(Color.GREEN);
            }else{
                vendorNameTextStyle.setColor(Color.RED);
            }
            getPdfTextUtil().addTextToPage("Total Standing Gold Weight : " + debitGoldWeight.subtract(creditGoldWeight).abs() + " gms", vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            if(debit.compareTo(credit) <= 0){
                vendorNameTextStyle.setColor(Color.GREEN);
            }else{
                vendorNameTextStyle.setColor(Color.RED);
            }

            getPdfTextUtil().addTextToPage("Total Cash Amount : " + debit.subtract(credit).abs() + " Rs", vendorNameTextStyle, pdPageContentStream);
            headerTextStyle.setFontSize(8);
            pdfPositionDTO.setX(10);
            pdfPositionDTO.setY(20);
            headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
            getPdfTextUtil().addTextToPage(String.format("page %s - %s ", pageNo, totalPages), headerTextStyle, pdPageContentStream);
        } else {
            pdfPositionDTO.setX(20);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Credit Transaction Amount: Rs " + credit, vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Debit Transaction Amount: Rs " + debit, vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Credit Gold Weight : " + creditGoldWeight + " gms", vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            getPdfTextUtil().addTextToPage("Debit Gold Weight : " + debitGoldWeight + " gms", vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            if (debitGoldWeight.compareTo(creditGoldWeight) <= 0) {
                vendorNameTextStyle.setColor(Color.RED);
            }else{
                vendorNameTextStyle.setColor(Color.GREEN);
            }
            getPdfTextUtil().addTextToPage("Total Standing Gold Weight : " + debitGoldWeight.subtract(creditGoldWeight) + " gms", vendorNameTextStyle, pdPageContentStream);
            pageHeight1 = pageHeight1 - 20;
            pdfPositionDTO.setY(pageHeight1);
            if(debit.compareTo(credit) <= 0){
                vendorNameTextStyle.setColor(Color.RED);
            }else{
                vendorNameTextStyle.setColor(Color.GREEN);
            }

            getPdfTextUtil().addTextToPage("Total Cash Amount : " + debit.subtract(credit) + " Rs", vendorNameTextStyle, pdPageContentStream);
        }
        headerTextStyle.setFontSize(8);
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(20);
        headerTextStyle.setPdfPositionDTO(pdfPositionDTO);
        getPdfTextUtil().addTextToPage(String.format("page %s - %s ", pageNo, totalPages), headerTextStyle, pdPageContentStream);


        pdPageContentStream.close();
        pdDocument.save("Ledger_Transactions.pdf");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdDocument.save(outputStream);
        pdDocument.close();
        return outputStream.toByteArray();
    }


    private void ledgerHeader(PDDocument pdDocument, PDPageContentStream pdPageContentStream, int pageWidth, int pageHeight, PdfPositionDTO pdfPositionDTO,
                              String vendorName, TextStyleDTO vendorNameTextStyle, TableStyleDTO tableStyleDTO) throws IOException, ParseException {
        generateHeader(pdPageContentStream, pageWidth, pageHeight, "Ledger Transactions");

//      Vendor name.
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(pageHeight - 60);
        getPdfTextUtil().addTextToPage(String.format("Vendor Name : %s", vendorName)
                , vendorNameTextStyle, pdPageContentStream);
        //       Create table
        pdfPositionDTO.setX(20);
        pdfPositionDTO.setY(pageHeight - 120);

        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "SNo", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Ledger Date", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Debit Gold Weight", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Credit Gold Weight", false);
        getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, "Amount", false);
    }
}
