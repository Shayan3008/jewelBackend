package com.jewelbackend.backend.reports.services.invoicereport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import com.jewelbackend.backend.reports.utils.PdfHelperUtils;
import com.jewelbackend.backend.setup.models.Invoice;

public class InvoiceReportHelper {

        private InvoiceReportHelper() {
        }

        public static int PAGE_HEIGHT = 0;
        public static int PAGE_WIDTH = 0;
        public static float[] CELL_WIDTHS = {};
        public static float[] CELL_POSITIONS = {};

        public static float addHeaderInformationToPDFReport(PDDocument document, PDPageContentStream contentStream,
                        float pageHeight, Invoice invoice, float[] cellWidths,
                        float[] cellPositions) throws IOException {
                PdfHelperUtils.addTextToPage(contentStream, new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), 36,
                                cellPositions[1] + 5, pageHeight - 85, "Sale Report");

                // PDImageXObject pdImage = PDImageXObject.createFromByteArray(document,
                // logoImageBytes, "alfardan_logo_uae.png");
                // contentStream.drawImage(pdImage, 100, pageHeight - 90);
                Date currentDate = new Date();
                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                PdfHelperUtils.addTextToPage(contentStream, new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 30,
                                cellPositions[2] + 15, pageHeight - 85, "Date : " + dateformat.format(currentDate));

                float startY = pageHeight - 210;
                PdfHelperUtils.addTextToPage(contentStream, new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), 24,
                                cellPositions[1] + 100, pageHeight - 180, "Invoice Report");
                Map<String, String> invoiceMap = new HashMap<>();
                invoiceMap.put("Design No", "43");
                // invoiceMap.put("Item Name", "G necklace");
                // invoiceMap.put("Total Weight", "2.15");
                for (var map : invoiceMap.entrySet()) {
                        PdfHelperUtils.addTextToPage(contentStream,
                                        new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), 24,
                                        cellPositions[0] + 150, pageHeight - 180, map.getKey());
                        PdfHelperUtils.addTextToPage(contentStream,
                                        new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 18,
                                        cellPositions[0] + 150, pageHeight - 210, map.getValue());

                        PdfHelperUtils.drawSingleColBorder(contentStream, pageHeight - 160, 130, cellPositions[0] + 140,
                                        90);
                }

                // Map<String, String> sifFileSummary = new HashMap<>();
                // sifFileSummary.put("1", "SIF Reference Number : ");
                // sifFileSummary.put("2", "Customer Name : ");
                // sifFileSummary.put("3", "MOL Code : ");
                // sifFileSummary.put("4", "Salary Month : ");
                // sifFileSummary.put("5", "Afex Registered Employees: ");
                // sifFileSummary.put("6", "Other Employees : ");
                // sifFileSummary.put("7", "Total Number Of Employees : ");

                // float positionY = PdfHelperUtils.addDataAsABlock(contentStream,
                // sifFileSummary,
                // new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 20, startY,
                // cellPositions[0] + 100,
                // cellWidths[1]);
                // PdfHelperUtils.addTextToPage(contentStream, new
                // PDType1Font(Standard14Fonts.FontName.TIMES_BOLD),
                // PAGE_HEIGHT, startY, positionY, "Sale");

                // float positionY = PdfHelperUtils.addDataAsABlock(contentStream,
                // sifFileSummary,
                // new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 20, startY,
                // cellPositions[1] - 100,
                // cellWidths[1]);
                // sifFileSummary.clear();

                // PdfHelperUtils.addTextToPage(contentStream, new
                // PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), 24,
                // cellPositions[2] - 100, pageHeight - 180, "SIF Charge Details");
                // sifFileSummary.put("1", "Salary Amount : ");

                // sifFileSummary.clear();

                return startY;
        }

        // public static void addDataTableAndSummaryLines(PDDocument document,
        // PDPageContentStream contentStream,
        // List<WpsSifDetailsReportModel> sifDetailsData, float startY, float[]
        // cellWidths, float[] cellPositions,
        // float pageHeight) throws IOException {

        // PDType1Font headerFont = new
        // PDType1Font(Standard14Fonts.FontName.TIMES_BOLD);
        // PDType1Font dataFont = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
        // startY -= 100;
        // PdfHelperUtils.addTextToPage(contentStream, new
        // PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), 24, 10,
        // startY, "SIF Records");
        // startY -= 50;
        // List<String> headerData = Arrays.asList("Employee EID", "Labour ID", "Account
        // Title", "Agent Routing Code",
        // "AFEX Customer Code", "IBAN", "Salary Month", "Month Start Date", "Month End
        // Date", "Leave Days",
        // "Fixed Salary", "Variable Salary", "Total Salary");
        // DateFormat salaryMonthFormat = new SimpleDateFormat("MMM-yyyy");
        // DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

        // float rowHeight = PdfHelperUtils.calculateRowHeight(headerData, headerFont,
        // 20, cellWidths);

        // PdfHelperUtils.addTableHeaderRow(contentStream, headerData, headerFont, 20,
        // cellWidths, cellPositions, startY,
        // true);

        // startY -= rowHeight;

        // var serialNo = 0;

        // for (WpsSifDetailsReportModel data : sifDetailsData) {

        // List<String> rowData = Arrays.asList(
        // data.getEmployeeEid(),
        // data.getLabourId() != null ? data.getLabourId() : "---",
        // data.getAccountTitle() != null ? data.getAccountTitle() : "---",
        // data.getRoutingCode() != null ? data.getRoutingCode() : "---",
        // data.getAfexCustomerId() != null ? data.getAfexCustomerId() : "---",
        // data.getIbanNumber() != null ? data.getIbanNumber() : "---",
        // data.getSalaryMonth() != null ?
        // salaryMonthFormat.format(data.getSalaryMonth()) : "---",
        // data.getStartDate() != null ? dateformat.format(data.getStartDate()) : "---",
        // data.getEndDate() != null ? dateformat.format(data.getEndDate()) : "---",
        // String.valueOf(data.getLeaveDays() != null ? data.getLeaveDays() : "---"),
        // String.valueOf(
        // data.getFixedSalary() != null ? data.getFixedSalary().setScale(2,
        // RoundingMode.CEILING)
        // : "---"),
        // String.valueOf(data.getVariableSalary() != null
        // ? data.getVariableSalary().setScale(2, RoundingMode.CEILING)
        // : "---"),
        // String.valueOf(
        // data.getTotalSalary() != null ? data.getTotalSalary().setScale(2,
        // RoundingMode.CEILING)
        // : "---")

        // );

        // if (startY < pageHeight - 1600) {
        // contentStream.close();
        // PDPage page = new PDPage(new PDRectangle(PAGE_WIDTH, PAGE_HEIGHT));
        // document.addPage(page);
        // contentStream = new PDPageContentStream(document, page);

        // PdfHelperUtils.addTextToPage(contentStream, new
        // PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 20,
        // 100, pageHeight - 98,
        // "Date : " + new
        // SimpleDateFormat(Constants.CURRENT_DEAFUALT_DATE_FORMAT).format(new Date()));
        // startY = page.getMediaBox().getHeight() - 173;

        // rowHeight = PdfHelperUtils.calculateRowHeight(headerData, headerFont, 20,
        // cellWidths);
        // PdfHelperUtils.addTableHeaderRow(contentStream, headerData, headerFont, 20,
        // cellWidths, cellPositions,
        // startY, true);
        // startY -= rowHeight;
        // }

        // rowHeight = PdfHelperUtils.calculateDataRowHeight(rowData, dataFont, 15,
        // cellWidths); // Use entire page
        // // width
        // PdfHelperUtils.addTableDataRow(contentStream, rowData, dataFont, 15,
        // cellWidths, cellPositions, startY,
        // null, null, null);
        // startY -= rowHeight;
        // }

        // float borderHeight = 5;
        // contentStream.setLineWidth(borderHeight);
        // contentStream.moveTo(50, pageHeight - 1600 + borderHeight);
        // contentStream.lineTo(50 + 200, pageHeight - 1600 + borderHeight);
        // contentStream.stroke();

        // PdfHelperUtils.addTextToPage(contentStream, new
        // PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 20, 106,
        // pageHeight - 1650, "Date");

        // contentStream.moveTo(1280, pageHeight - 1600 + borderHeight);
        // contentStream.lineTo(1280 + 200, pageHeight - 1600 + borderHeight);
        // contentStream.stroke();
        // PdfHelperUtils.addTextToPage(contentStream, new
        // PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 20, 1330,
        // pageHeight - 1650, "Signature");

        // contentStream.close();
        // }
}
