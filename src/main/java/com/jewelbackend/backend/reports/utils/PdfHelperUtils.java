package com.jewelbackend.backend.reports.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.awt.Color;

public class PdfHelperUtils {

    private PdfHelperUtils() {
    }

    public static void addCenteredImageToPage(PDDocument document, PDPageContentStream contentStream, float pageWidth,
            float pageHeight, float y, byte[] logoImageBytes) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, logoImageBytes, "alfardan_logo_uae.png");
        float imageWidth = pdImage.getWidth();
        float imageHeight = pdImage.getHeight();
        float x = (pageWidth - imageWidth) / 2;
        contentStream.drawImage(pdImage, x, pageHeight - y, imageWidth, imageHeight);
    }

    public static void addTextToPage(PDPageContentStream contentStream, PDFont font, int fontSize, float x, float y,
            String text) throws IOException {
        contentStream.setFont(font, fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    public static void addCenteredTextToPage(PDPageContentStream contentStream, PDFont font, int fontSize,
            int pageWidth, float y, String text) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float textX = (pageWidth - textWidth) / 2;

        addTextToPage(contentStream, font, fontSize, textX, y, text);
    }

    public static void addTwoColumnTextToPage(PDPageContentStream contentStream, PDType1Font font, int fontSize,
            String leftText, String rightText, float leftX, float rightX, float y) throws IOException {
        addTextToPage(contentStream, font, fontSize, leftX, y, leftText);
        addTextToPage(contentStream, font, fontSize, rightX, y, rightText);
    }

    public static void addCenteredBoldAndRegularTextToPage(PDPageContentStream contentStream, PDType1Font boldFont,
            PDType1Font regularFont, int fontSize, String leftText, String rightText, int pageWidth, float startY)
            throws IOException {
        float middleX = pageWidth / 2;

        float leftTextWidth = boldFont.getStringWidth(leftText) / 1000 * fontSize;
        float rightTextWidth = regularFont.getStringWidth(rightText) / 1000 * fontSize;

        float totalWidth = leftTextWidth + rightTextWidth;

        float leftStartX = middleX - totalWidth / 2;
        float rightStartX = leftStartX + leftTextWidth;

        addTextToPage(contentStream, boldFont, fontSize, leftStartX, startY, leftText);
        addTextToPage(contentStream, regularFont, fontSize, rightStartX, startY, rightText);
    }

    // Header Row in Table
    public static void addTableHeaderRow(PDPageContentStream contentStream, List<String> rowData, PDFont font,
            int fontSize, float[] cellWidths, float[] cellPositions, float y, boolean isCenterAlign)
            throws IOException {
        float maxCellHeight = 0;
        for (int i = 0; i < rowData.size(); i++) {
            float cellHeight = calculateCellHeight(rowData.get(i), font, fontSize, cellWidths[i]);
            maxCellHeight = Math.max(maxCellHeight, cellHeight);
        }

        drawRowBorders(contentStream, y, cellWidths, cellPositions, maxCellHeight);

        for (int i = 0; i < rowData.size(); i++) {
            if (!isCenterAlign) {
                drawHeaderCell(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y, cellWidths[i],
                        maxCellHeight);
            } else {
                headerDrawCellCenterAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y,
                        cellWidths[i], maxCellHeight);
            }
        }
    }

    public static void drawRowBorders(PDPageContentStream contentStream, float y, float[] cellWidths,
            float[] cellPositions, float height) throws IOException {
        contentStream.setStrokingColor(0.3f, 0.3f, 0.3f);
        for (int i = 0; i < cellPositions.length; i++) {
            contentStream.addRect(cellPositions[i] - 4, y - height + 10, cellWidths[i], height);
            contentStream.stroke();
        }
    }

    public static void drawSingleColBorder(PDPageContentStream contentStream, float y, float cellWidth,
    float cellPosition, float height) throws IOException{
        contentStream.setStrokingColor(0.3f, 0.3f, 0.3f);
        
            contentStream.addRect(cellPosition - 4, y - height + 10, cellWidth, height);
            contentStream.stroke();
        
    }

    public static void drawHeaderCell(PDPageContentStream contentStream, String text, PDFont font, int fontSize,
            float x, float y, float width, float height) throws IOException {
        List<String> lines = splitTextIntoLines(text, font, fontSize, width);
        float textHeight = calculateTextHeight(font, fontSize, lines);

        float verticalPadding = (height - textHeight) / 2;

        float textY = y - verticalPadding;

        contentStream.setFont(font, fontSize);
        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + 2, textY);
            contentStream.showText(line);
            contentStream.endText();
            textY -= (fontSize + 2);
        }
    }

    public static void headerDrawCellCenterAlign(PDPageContentStream contentStream, String text, PDFont font,
            int fontSize, float x, float y, float width, float height) throws IOException {
        List<String> lines = splitTextIntoLines(text, font, fontSize, width);
        float textHeight = calculateTextHeight(font, fontSize, lines);

        float verticalPadding = (height - textHeight) / 2;

        float textY = y - verticalPadding;

        contentStream.setFont(font, fontSize);
        for (String line : lines) {
            float textWidth = font.getStringWidth(line) / 1000 * fontSize;
            float horizontalPadding = (width - textWidth) / 2;
            contentStream.beginText();
            contentStream.newLineAtOffset(x + horizontalPadding, textY);
            contentStream.showText(line);
            contentStream.endText();
            textY -= (fontSize + 2);
        }
    }

    public static List<String> splitTextIntoLines(String text, PDFont font, int fontSize, float width)
            throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        String currentLine = words[0];
        for (int i = 1; i < words.length; i++) {
            if (font.getStringWidth(currentLine + " " + words[i]) / 1000 * fontSize < width - 4) {
                currentLine += " " + words[i];
            } else {
                lines.add(currentLine);
                currentLine = words[i];
            }
        }
        lines.add(currentLine);
        return lines;
    }

    public static float calculateCellHeight(String text, PDFont font, int fontSize, float width) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        String currentLine = words[0];
        float lineHeight = fontSize + 2;

        for (int i = 1; i < words.length; i++) {
            if (font.getStringWidth(currentLine + " " + words[i]) / 1000 * fontSize < width - 4) {
                currentLine += " " + words[i];
            } else {
                lines.add(currentLine);
                currentLine = words[i];
            }
        }
        lines.add(currentLine);
        return lines.size() * lineHeight + 10;
    }

    public static float calculateTextHeight(PDFont font, int fontSize, List<String> lines) {
        float lineHeight = fontSize + 2;
        return lines.size() * lineHeight;
    }

    public static float calculateRowHeight(List<String> rowData, PDFont font, int fontSize, float[] cellWidths)
            throws IOException {
        float maxCellHeight = 0;
        for (int i = 0; i < rowData.size(); i++) {
            float cellHeight = calculateCellHeight(rowData.get(i), font, fontSize, cellWidths[i]);
            maxCellHeight = Math.max(maxCellHeight, cellHeight);
        }
        return maxCellHeight;
    }

    // Data Row of Table
    public static void addTableDataRow(PDPageContentStream contentStream, List<String> rowData, PDFont font,
            int fontSize, float[] cellWidths, float[] cellPositions, float y, List<Integer> rightAlignColumns,
            List<Integer> leftAlignColumns, List<Integer> centerAlignColumns) throws IOException {

        float maxCellHeight = calculateDataRowHeight(rowData, font, fontSize, cellWidths);
        drawRowBorders(contentStream, y, cellWidths, cellPositions, maxCellHeight);

        for (int i = 0; i < rowData.size(); i++) {
            if (rightAlignColumns != null && rightAlignColumns.contains(i)) {
                drawDataCellRightAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y,
                        cellWidths[i], maxCellHeight);
            } else if (leftAlignColumns != null && leftAlignColumns.contains(i)) {
                drawDataCellLeftAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y, cellWidths[i],
                        maxCellHeight);
            } else if (centerAlignColumns != null && centerAlignColumns.contains(i)) {
                DrawDataCellCenterAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y,
                        cellWidths[i], maxCellHeight);
            } else {
                drawDataCellLeftAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y, cellWidths[i],
                        maxCellHeight);
            }
        }
    }

    // Data Row of Table without borders
    public static void addTableDataRow(PDPageContentStream contentStream, List<String> rowData, PDFont font,
            int fontSize, float[] cellWidths, float[] cellPositions, float y, List<Integer> rightAlignColumns,
            List<Integer> leftAlignColumns, List<Integer> centerAlignColumns, boolean showBorder) throws IOException {

        float maxCellHeight = calculateDataRowHeight(rowData, font, fontSize, cellWidths);
        if (showBorder) {
            drawRowBorders(contentStream, y, cellWidths, cellPositions, maxCellHeight);
        }

        for (int i = 0; i < rowData.size(); i++) {
            if (rightAlignColumns != null && rightAlignColumns.contains(i)) {
                drawDataCellRightAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y,
                        cellWidths[i], maxCellHeight);
            } else if (leftAlignColumns != null && leftAlignColumns.contains(i)) {
                drawDataCellLeftAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y, cellWidths[i],
                        maxCellHeight);
            } else if (centerAlignColumns != null && centerAlignColumns.contains(i)) {
                DrawDataCellCenterAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y,
                        cellWidths[i], maxCellHeight);
            } else {
                drawDataCellLeftAlign(contentStream, rowData.get(i), font, fontSize, cellPositions[i], y, cellWidths[i],
                        maxCellHeight);
            }
        }
    }

    public static void drawDataCellRightAlign(PDPageContentStream contentStream, String text, PDFont font, int fontSize,
            float x, float y, float width, float height) throws IOException {
        List<String> lines = splitDataTextIntoLines(text, font, fontSize, width);
        float textHeight = calculateTextHeight(font, fontSize, lines);

        float verticalPadding = (height - textHeight) / 2;

        float textY = y - verticalPadding;

        contentStream.setFont(font, fontSize);
        for (String line : lines) {
            float textWidth = font.getStringWidth(line) / 1000 * fontSize;
            float textX = x + width - textWidth - 10;
            contentStream.beginText();
            contentStream.newLineAtOffset(textX, textY);
            contentStream.showText(line);
            contentStream.endText();
            textY -= (fontSize + 2);
        }
    }

    public static void drawDataCellLeftAlign(PDPageContentStream contentStream, String text, PDFont font, int fontSize,
            float x, float y, float width, float height) throws IOException {
        List<String> lines = splitDataTextIntoLines(text, font, fontSize, width);
        float textHeight = calculateTextHeight(font, fontSize, lines);

        float verticalPadding = (height - textHeight) / 2;

        float textY = y - verticalPadding;

        contentStream.setFont(font, fontSize);
        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + 2, textY);
            contentStream.showText(line);
            contentStream.endText();
            textY -= (fontSize + 2);
        }
    }

    public static void DrawDataCellCenterAlign(PDPageContentStream contentStream, String text, PDFont font,
            int fontSize, float x, float y, float width, float height) throws IOException {
        List<String> lines = splitDataTextIntoLines(text, font, fontSize, width);
        float textHeight = calculateTextHeight(font, fontSize, lines);

        float verticalPadding = (height - textHeight) / 2;

        float textY = y - verticalPadding;

        contentStream.setFont(font, fontSize);
        for (String line : lines) {
            float textWidth = font.getStringWidth(line) / 1000 * fontSize;
            float horizontalPadding = (width - textWidth) / 2;
            contentStream.beginText();
            contentStream.newLineAtOffset(x + horizontalPadding, textY);
            contentStream.showText(line);
            contentStream.endText();
            textY -= (fontSize + 2);
        }
    }

    private static List<String> splitDataTextIntoLines(String text, PDFont font, int fontSize, float width)
            throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        if (words.length == 1) {
            int beginIndex = 0;
            for (int i = 0; i < text.length(); i++) {
                String subString = text.substring(beginIndex, i + 1);
                float stringWidth = font.getStringWidth(subString) / 1000 * fontSize;
                if (stringWidth > width - 10) {
                    lines.add(text.substring(beginIndex, i));
                    beginIndex = i;
                }
            }
            lines.add(text.substring(beginIndex));
        } else {
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++) {
                if (font.getStringWidth(currentLine + " " + words[i]) / 1000 * fontSize < width - 4) {
                    currentLine += " " + words[i];
                } else {
                    lines.add(currentLine);
                    currentLine = words[i];
                }
            }
            lines.add(currentLine);
        }
        return lines;
    }

    public static float calculateDataCellHeight(String content, PDFont font, int fontSize, float width)
            throws IOException {
        List<String> wrappedText = splitDataTextIntoLines(content, font, fontSize, width);
        int numberOfLines = wrappedText.size();
        return (numberOfLines * fontSize * 1.2f) + 10;
    }

    public static float calculateDataRowHeight(List<String> rowData, PDFont font, int fontSize, float[] cellWidths)
            throws IOException {
        float maxHeight = 0;
        for (int i = 0; i < rowData.size(); i++) {
            float cellHeight = calculateDataCellHeight(rowData.get(i), font, fontSize, cellWidths[i]);
            maxHeight = Math.max(maxHeight, cellHeight);
        }
        return maxHeight;
    }

    public static void addMergedCellsRow(PDPageContentStream contentStream, String text, PDFont font, int fontSize,
            float startY, float[] cellWidths, float[] cellPositions) throws IOException {
        float startX = cellPositions[0];
        float totalWidth = cellPositions[cellPositions.length - 1] + cellWidths[cellWidths.length - 1] - startX;

        float height = calculateCellHeight(text, font, fontSize, totalWidth);
        drawHeaderCell(contentStream, text, font, fontSize, startX, startY, totalWidth, height);
    }

    public static void addMergedDatasRow(PDPageContentStream contentStream, List<String> rowData, PDFont font,
            int fontSize, float startY, float[] cellWidth, float[] cellPositions, Set<Integer> rightAlignedColumns)
            throws IOException {
        int cell_width_no = 0;
        float startX = 0;
        float totalWidth = 0;

        for (int i = 0; i < rowData.size(); i++) {

            if (i != 0) {
                startX = cellPositions[cell_width_no];
                totalWidth = cellPositions[cell_width_no] + cellWidth[cell_width_no] - startX;
                if (i == 10) {
                    startX = cellPositions[cell_width_no];
                }
            } else {
                startX = cellPositions[i];
                totalWidth = cellPositions[i] + cellWidth[i] - startX;

            }

            float height = calculateCellHeight(rowData.get(i), font, fontSize, totalWidth);

            if (rightAlignedColumns.contains(i)) {
                drawDataCellRightAlign(contentStream, rowData.get(i), font, fontSize, startX, startY, totalWidth,
                        height);
            } else {
                drawDataCellLeftAlign(contentStream, rowData.get(i), font, fontSize, startX, startY, totalWidth,
                        height);
            }

            cell_width_no++;

            if (cell_width_no == 11) {
                cell_width_no = 1;
            }
        }
    }

    public static float calculateSummaryLinesHeight(int fontSize, int noOfLines) throws IOException {
        return noOfLines * (fontSize + noOfLines);
    }

    public static String formatBalanceAsCurrency(BigDecimal balance, int decimalPlaces) {
        String pattern = decimalPlaces == 3 ? "#,##0.000" : "#,##0.00";
        DecimalFormat df = new DecimalFormat(pattern);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return "(" + df.format(balance.abs()) + ")";
        } else {
            return df.format(balance);
        }
    }

    public static float addDataAsABlock(PDPageContentStream contentStream, Map<String, String> data, PDFont font,
            int fontSize, float startY, float cellPosition, float cellWidth) throws IOException {
        float maxCellHeight = 0;
        float y = startY;
        for (String key : data.keySet()) {
            float cellHeight = calculateCellHeight(data.get(key), font, fontSize, cellWidth);
            maxCellHeight = Math.max(maxCellHeight, cellHeight);
            List<String> lines = splitTextIntoLines(data.get(key), font, fontSize, cellWidth);
            float textHeight = calculateTextHeight(font, fontSize, lines);
            int linesSize = lines.size();
            float verticalPadding = (maxCellHeight - textHeight) / 2;

            float textY = y - verticalPadding;

            contentStream.setFont(font, fontSize);
            for (String line : lines) {
                contentStream.beginText();
                contentStream.newLineAtOffset(cellPosition + 2, textY);
                contentStream.showText(line);
                contentStream.endText();
                textY -= (fontSize + 2);
                y -= linesSize * 5;
            }
            y -= 25;
        }
        return y;
    }

    public static String setToCommaSeparatedString(Set<String> set) {
        StringBuilder result = new StringBuilder();
        for (String element : set) {
            if (result.length() > 0) {
                // Add a comma before each element (except the first one)
                result.append(",");
            }
            result.append(element);
        }
        return result.toString();
    }

    public static void splitTextToLinesWithCustomGap(PDPageContentStream contentStream, PDFont font, int fontSize,
            float x, float y, int spaceBetweenLines, float maxWidth, String text) throws IOException {
        List<String> lines = PdfHelperUtils.splitTextIntoLines(text, font, fontSize, maxWidth);
        contentStream.setFont(font, fontSize);
        float startY = y;
        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, startY);
            contentStream.showText(line);
            contentStream.endText();
            startY -= spaceBetweenLines;
        }
    }

    public static List<String> splitTextWithCommaIntoLines(String text, PDFont font, int fontSize, float width)
            throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(",");
        String currentLine = words[0];
        for (int i = 1; i < words.length; i++) {
            if (font.getStringWidth(currentLine + " " + words[i]) / 1000 * fontSize < width - 4) {
                if (i == words.length - 1) {
                    currentLine += " " + words[i];
                } else {
                    currentLine += " " + words[i] + ",";
                }
            } else {
                lines.add(currentLine);
                currentLine = words[i];
            }
        }
        lines.add(currentLine);
        return lines;
    }

    public static float addRightAlignedTextToPage(PDPageContentStream contentStream, PDFont font, int fontSize,
            float margin, float y, String text, int pageWidth) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        float textX = pageWidth - margin - textWidth;

        addTextToPage(contentStream, font, fontSize, textX, y, text);
        return textWidth;
    }

    public static String breakText(String text, int maxLength) {
        List<String> lines = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return ""; // Return an empty string for empty input
        }

        String[] words = text.split("\\s+");

        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxLength) {
                // If adding the current word does not exceed the maxLength, add it to the
                // current line
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                // If adding the current word exceeds the maxLength, break the word and start a
                // new line
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();

                // Split the word into segments that fit within the maxLength
                for (int i = 0; i < word.length(); i += maxLength) {
                    int end = Math.min(i + maxLength, word.length());
                    lines.add(word.substring(i, end));
                }
            }
        }

        // Add the last line
        lines.add(currentLine.toString());

        // Join the lines into a single string with spaces between each line
        return String.join(" ", lines);
    }

    public static void addColoredText(PDPageContentStream contentStream, PDType1Font font,
            int fontSize, float x, float y, Color color, String text) throws IOException {
        contentStream.setNonStrokingColor(color);
        contentStream.setFont(font, fontSize);
        // Add red text
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();

        // Close the content stream
        contentStream.close();

    }

}