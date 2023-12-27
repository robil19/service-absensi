package com.intelsysdata.absensi.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

//import com.intelsysdata.absensi.model.AttendanceRecord;
//import com.intelsysdata.absensi.model.AttendanceRecord;
import com.intelsysdata.absensi.model.CustomCellStyle;
import com.intelsysdata.absensi.response.AttendanceRecordResponse;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceReportService {
    private final StylesGenerator stylesGenerator;

    public byte[] generateXlsxReport(List<AttendanceRecordResponse> data) throws IOException {
        Workbook wb = new XSSFWorkbook();
        return generateReport(wb, data);
    }

    public byte[] generateXlsReport(List<AttendanceRecordResponse> data) throws IOException {
        Workbook wb = new HSSFWorkbook();
        return generateReport(wb, data);
    }

    private byte[] generateReport(Workbook wb, List<AttendanceRecordResponse> data) throws IOException {
        Map<CustomCellStyle, CellStyle> styles = stylesGenerator.prepareStyles(wb);
        Sheet sheet = wb.createSheet("Daftar Kehadiran");

        setColumnsWidth(sheet);

        createHeaderRow(sheet, styles, data);
        createStringsRow(sheet, styles, data);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);

        out.close();
        wb.close();

        return out.toByteArray();
    }

    private void setColumnsWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 256 * 20);

        for (int columnIndex = 1; columnIndex < 5; columnIndex++) {
            sheet.setColumnWidth(columnIndex, 256 * 15);
        }
    }

    private void createHeaderRow(Sheet sheet, Map<CustomCellStyle, CellStyle> styles,
            List<AttendanceRecordResponse> data) {
        Row row = sheet.createRow(0);

        CellStyle headerCellStyle = styles.get(CustomCellStyle.GREY_CENTERED_BOLD_ARIAL_WITH_BORDER);

        // Sesuaikan ini dengan atribut-atribut yang ada pada objek model Absence
        createHeaderCell(row, 1, "1", headerCellStyle);
        createHeaderCell(row, 2, "2", headerCellStyle);
        createHeaderCell(row, 3, "3", headerCellStyle);
        createHeaderCell(row, 4, "4", headerCellStyle);
    }

    private void createHeaderCell(Row row, int columnNumber, String headerText, CellStyle cellStyle) {
        Cell cell = row.createCell(columnNumber);
        cell.setCellValue(headerText);
        cell.setCellStyle(cellStyle);
    }

    private void createRowLabelCell(Row row, Map<CustomCellStyle, CellStyle> styles, String label) {
        Cell rowLabel = row.createCell(0);
        rowLabel.setCellValue(label);
        rowLabel.setCellStyle(styles.get(CustomCellStyle.RED_BOLD_ARIAL_WITH_BORDER));
    }

    private void createStringsRow(Sheet sheet, Map<CustomCellStyle, CellStyle> styles,
            List<AttendanceRecordResponse> data) {
        int rowIndex = 1; // Mulai dari baris kedua karena baris pertama sudah berisi label

        for (AttendanceRecordResponse absence : data) {
            Row row = sheet.createRow(rowIndex++);

            createRowLabelCell(row, styles, "Absence Data");

            // Sesuaikan ini dengan atribut-atribut yang ada pada objek model Absence
            createDataCell(row, 1, absence.getAttendanceDate(), styles);
            createDataCell(row, 2, absence.getAttendanceDate(), styles);
            createDataCell(row, 3, absence.getAttendanceDate(), styles);
            createDataCell(row, 4, absence.getAttendanceDate(), styles);
        }
    }

    private void createDataCell(Row row, int columnNumber, Object value, Map<CustomCellStyle, CellStyle> styles) {
        Cell cell = row.createCell(columnNumber);
        cell.setCellValue(value.toString());
        cell.setCellStyle(styles.get(CustomCellStyle.RIGHT_ALIGNED));
    }

}
