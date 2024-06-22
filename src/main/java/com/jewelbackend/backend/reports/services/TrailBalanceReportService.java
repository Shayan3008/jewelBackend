package com.jewelbackend.backend.reports.services;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.common.exceptions.EmptyReportException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.reports.services.pdf.dto.PdfPositionDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TextStyleDTO;
import com.jewelbackend.backend.setup.models.VendorHeader;
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
import java.util.List;
import java.util.Map;

@Service
public class TrailBalanceReportService extends BaseReport {
    public TrailBalanceReportService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    @Override
    public void generateFilters(Map<String, String> parameters) {

    }


    @Override
    public byte[] generateReport(Map<String, String> parameters, String format, String reportName) throws EmptyReportException, IOException, ParseException {
        if (!parameters.isEmpty()) {

            CriteriaFilter<VendorHeader> criteriaFilter = new CriteriaFilter<>();
            var vendorHeader = criteriaFilter.getEntitiesByCriteria(VendorHeader.class, parameters, getEntityManager());
            return generatePdf(vendorHeader);

        }
        List<VendorHeader> vendorHeaders = (List<VendorHeader>) this.getDaoFactory().getVendorHeaderDao().findAll();
        if (format.equals("PDF") && !vendorHeaders.isEmpty()) {
            return generatePdf(vendorHeaders);
        }
        return new byte[0];
    }


    private byte[] generatePdf(List<VendorHeader> vendorHeaders) throws IOException, ParseException {

        PDDocument pdDocument = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        pdDocument.addPage(page1);
        int pageWidth = (int) page1.getMediaBox().getWidth();
        int pageHeight = (int) page1.getMediaBox().getHeight();
        BigDecimal totalPages = BigDecimal.valueOf(vendorHeaders.size()).divide(BigDecimal.valueOf(16), RoundingMode.CEILING);
        PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, page1);
        PdfPositionDTO pdfPositionDTO = new PdfPositionDTO();
        TextStyleDTO vendorNameTextStyle = new TextStyleDTO(12, 12.5f, pdfPositionDTO, "", new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), Color.BLACK);
        TextStyleDTO textStyleDTO = new TextStyleDTO(9, 12.5f, pdfPositionDTO, "", new PDType1Font(Standard14Fonts.FontName.HELVETICA), Color.BLACK);
        int yCoordinate = pageHeight - 80;
        generateHeader(pdPageContentStream, pageWidth, pageHeight, "Detailed Trail Balance Report");
        for (var vendorHeader : vendorHeaders) {
            if (pageHeight < 150) {
                pdPageContentStream.close();
                PDPage page = new PDPage(PDRectangle.A4);
                pdDocument.addPage(page);
                pdPageContentStream = new PDPageContentStream(pdDocument, page);
                yCoordinate = pageHeight - 80;
                generateHeader(pdPageContentStream, pageWidth, pageHeight, "Detailed Trail Balance Report");
            }
            pdfPositionDTO.setX(30);
            yCoordinate = yCoordinate - 20;
            pdfPositionDTO.setY(yCoordinate);
            getPdfTextUtil().addTextToPage("Vendor Header: " + vendorHeader.getName(), vendorNameTextStyle, pdPageContentStream);
            var vendors = vendorHeader.getVendors();
            for (var vendor : vendors) {
                if(yCoordinate <= 150){
                    pdPageContentStream.close();
                    PDPage page = new PDPage(PDRectangle.A4);
                    pdDocument.addPage(page);
                    pdPageContentStream = new PDPageContentStream(pdDocument, page);
                    yCoordinate = pageHeight - 80;
                    generateHeader(pdPageContentStream, pageWidth, pageHeight, "Detailed Trail Balance Report");
                }
                pdfPositionDTO.setX(60);
                yCoordinate = yCoordinate - 20;
                pdfPositionDTO.setY(yCoordinate);
                getPdfTextUtil().addTextToPage("Vendor Name: " + vendor.getName(), textStyleDTO, pdPageContentStream);
                var vendorTransactions = vendor.getLedgerTransactions();
                BigDecimal credit = BigDecimal.ZERO;
                BigDecimal debit = BigDecimal.ZERO;
                BigDecimal creditGoldWeight = BigDecimal.ZERO;
                BigDecimal debitGoldWeight = BigDecimal.ZERO;
                for (var vendorTransaction : vendorTransactions) {
                    if (vendorTransaction.getDebit() != null)
                        debit = debit.add(vendorTransaction.getDebit());
                    if (vendorTransaction.getCredit() != null)
                        credit = credit.add(vendorTransaction.getCredit());
                    if(vendorTransaction.getCreditGoldWeight() != null)
                        creditGoldWeight = creditGoldWeight.add(vendorTransaction.getCreditGoldWeight());
                    if(vendorTransaction.getDebitGoldWeight() != null)
                        debitGoldWeight = debitGoldWeight.add(vendorTransaction.getDebitGoldWeight());
                }
                pdfPositionDTO.setX(90);
                yCoordinate = yCoordinate - 20;
                pdfPositionDTO.setY(yCoordinate);
                getPdfTextUtil().addTextToPage("Credit Transaction Amount: Rs " + credit, textStyleDTO, pdPageContentStream);
                yCoordinate = yCoordinate - 20;
                pdfPositionDTO.setY(yCoordinate);
                getPdfTextUtil().addTextToPage("Debit Transaction Amount: Rs " + debit, textStyleDTO, pdPageContentStream);
                yCoordinate = yCoordinate - 20;
                pdfPositionDTO.setY(yCoordinate);
                getPdfTextUtil().addTextToPage("Credit Gold Weight : " + creditGoldWeight + " gms", textStyleDTO, pdPageContentStream);
                yCoordinate = yCoordinate - 20;
                pdfPositionDTO.setY(yCoordinate);
                getPdfTextUtil().addTextToPage("Debit Gold Weight : " + debitGoldWeight + " gms", textStyleDTO, pdPageContentStream);

            }
        }


        BigDecimal pageNo = BigDecimal.valueOf(5).divide(BigDecimal.valueOf(16), RoundingMode.CEILING);
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

}

