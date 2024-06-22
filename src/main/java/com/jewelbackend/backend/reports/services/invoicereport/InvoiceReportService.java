package com.jewelbackend.backend.reports.services.invoicereport;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.reports.services.pdf.dto.PdfPositionDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TableStyleDTO;
import com.jewelbackend.backend.reports.services.pdf.dto.TextStyleDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.common.exceptions.EmptyReportException;
import com.jewelbackend.backend.reports.services.BaseReport;
import com.jewelbackend.backend.setup.models.Invoice;

@Service
public class InvoiceReportService extends BaseReport {

    public InvoiceReportService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    @Override
    public void generateFilters(Map<String, String> parameters) {
        throw new UnsupportedOperationException("Unimplemented method 'generateFilters'");
    }

    @Override
    public byte[] generateReport(Map<String, String> parameters, String format, String reportName)
            throws EmptyReportException, IOException, ParseException {
        Invoice invoice = this.getDaoFactory().getInvoiceDao().findById(Integer.parseInt(parameters.get("invoiceId")))
                .orElseThrow(() -> new EmptyReportException("Invoice not found"));
        if (format.equals("PDF")) {
            return generatePdf(invoice);
        }
        return null;
    }

    private byte[] generatePdf(Invoice invoice) throws IOException, ParseException {
        PDDocument pdDocument = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A5);
        pdDocument.addPage(page1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int pageWidth = (int) page1.getMediaBox().getWidth();
        int pageHeight = (int) page1.getMediaBox().getHeight();
        int cellSize = (pageWidth / 5);
        PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, page1);
        PdfPositionDTO pdfPositionDTO = new PdfPositionDTO();
        Integer invoiceId = invoice.getId();
        int[] cellWidths = {110, 80};
        TextStyleDTO vendorNameTextStyle = new TextStyleDTO(12, 12.5f, pdfPositionDTO, invoiceId.toString(), new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), Color.BLACK);
        TextStyleDTO tableTextStyleDTO = new TextStyleDTO(11, 12.5f, pdfPositionDTO, "",
                new PDType1Font(Standard14Fonts.FontName.HELVETICA), Color.BLACK);
        TableStyleDTO tableStyleDTO = new TableStyleDTO();
        tableStyleDTO.setColWidths(cellWidths);
        tableStyleDTO.setColPosition(0);
        tableStyleDTO.setCellHeight(30);
        tableStyleDTO.setXInitialPosition(0);
//        tableStyleDTO.setFillColor(Color.WHITE);
        tableStyleDTO.setTextStyleDTO(tableTextStyleDTO);
        generateHeader(pdPageContentStream, pageWidth, pageHeight);
        PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/image/Real.png", pdDocument);
        pdPageContentStream.drawImage(pdImage, (int) (pageWidth / 2) - 80, pageHeight - 40, 150, 40);
        pdfPositionDTO.setX(10);
        pdfPositionDTO.setY(pageHeight - 60);

        getPdfTextUtil().addTextToPage("Invoice Report", vendorNameTextStyle, pdPageContentStream);
        Map<String, String> map1 = new LinkedHashMap<>();
        map1.put("Invoice ID", invoice.getId().toString());
        map1.put("Invoice Date", sdf.format(invoice.getInvoiceDate()));
        if (invoice.getItem() != null)
            map1.put("Item Name", invoice.getItem().getCategory().getCategoryName());
        if (invoice.getItem() != null)
            map1.put("Design No", invoice.getItem().getDesignNo());
        if (invoice.getItem() != null) {
            map1.put("Item Weight", invoice.getItem().getNetWeight().toString() + " gms");
        } else {
            map1.put("Item Weight", invoice.getTotalWeight().divide(BigDecimal.valueOf(invoice.getWastePer()).divide(BigDecimal.valueOf(100)).add(BigDecimal.valueOf(1))) + " gms");
        }
        map1.put("Wastage", invoice.getWastePer() + "%");
        BigDecimal totalItemWeight = null;
        if (invoice.getItem() != null) {
            totalItemWeight = invoice.getItem().getNetWeight().add(BigDecimal.valueOf(invoice.getWastePer()).divide(BigDecimal.valueOf(100)).multiply(invoice.getItem().getNetWeight()));
        } else {
            totalItemWeight = invoice.getTotalWeight();
        }

        map1.put("Total Weight", totalItemWeight.toString() + " gms");
        map1.put("Gold Rate", invoice.getTotalItemPrice().divide(totalItemWeight, RoundingMode.CEILING).toString() + " Rs per gm");
        map1.put("Total Price", invoice.getTotalItemPrice().toString() + " Rs");
        map1.put("Making", invoice.getMaking().toString() + " Rs");
        map1.put("Bead Amount", invoice.getBeadAmount().toString() + " Rs");
        map1.put("Big Stone Amount", invoice.getBigStoneAmount().toString() + " Rs");
        map1.put("Small Stone Amount", invoice.getSmallStoneAmount().toString() + " Rs");
        map1.put("Chandi Amount", invoice.getChandiAmount().toString() + " Rs");
        map1.put("Diamond Amount", invoice.getDiamondAmount().toString() + " Rs");
        map1.put("Doli Polish", invoice.getDoliPolish().toString() + " Rs");
        map1.put("Discount", invoice.getDiscount().toString() + " Rs");
        map1.put("Final Bill", invoice.getTotalBill().toString() + " Rs");
        ledgerHeader(pdDocument, pdPageContentStream, pageWidth, pageHeight, pdfPositionDTO, map1, cellWidths, vendorNameTextStyle, tableTextStyleDTO, tableStyleDTO);
        pdPageContentStream.setStrokingColor(Color.BLACK);
        pdPageContentStream.setLineWidth(2);
        pdPageContentStream.moveTo(pageWidth - 150, 120);
        pdPageContentStream.lineTo(pageWidth - 25, 120);
        pdPageContentStream.stroke();
        pdfPositionDTO.setX(pageWidth - getPdfTextUtil().getFontWidth(vendorNameTextStyle, "Signature") - 60);
        pdfPositionDTO.setY(100);
        getPdfTextUtil().addTextToPage("Signature", vendorNameTextStyle, pdPageContentStream);
        pdPageContentStream.close();
        pdDocument.save("Ledger_Transactions.pdf");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        pdDocument.save(outputStream);
        pdDocument.close();
        return outputStream.toByteArray();
    }

    private void ledgerHeader(PDDocument pdDocument, PDPageContentStream pdPageContentStream, int pageWidth, int pageHeight, PdfPositionDTO pdfPositionDTO,
                              Map<String, String> map, int[] cellWidths, TextStyleDTO vendorNameTextStyle, TextStyleDTO tableTextStyleDTO, TableStyleDTO tableStyleDTO) throws IOException, ParseException {
        pdfPositionDTO.setX(0);
        pdfPositionDTO.setY(pageHeight - 100);
        int count = 0;
        for (var map1 : map.entrySet()) {
            count += 1;
            if (count % 13 == 0) {
                tableStyleDTO.setXInitialPosition(pageWidth - 220);
                pdfPositionDTO.setX(pageWidth - 220);
                pdfPositionDTO.setY(pageHeight - 100);
            }
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, map1.getKey(), false);
            getPdfTableUtil().addCell(tableStyleDTO, pdPageContentStream, map1.getValue(), false);
        }
    }

}
