package iworld.rpc.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author gq.cai
 * @version 1.0
 * @description
 * @date 2024/10/20 15:07
 */
public class ExcelConvertChat {
    
    static Map<String, BigDecimal> valueMap = new HashMap<>();
    static Set<String> company = new HashSet<>();
    static Set<String> classic  = new HashSet<>();
    static Map<Integer, String> indexClassicMap = new HashMap<>();
    
    public static void main(String[] args) throws Exception {
            t1();

    }
    
    public static void t1() throws Exception {
        File inputFile = new File("D:\\xxlx.xls");
        InputStream inputStream = new FileInputStream(inputFile);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            String companyName = cell.getStringCellValue();
            company.add(companyName);
            Cell cell1 = row.getCell(1);
            Cell cell2 = row.getCell(2);
            String classicName = cell2.getStringCellValue();
            BigDecimal value = BigDecimal.valueOf(cell1.getNumericCellValue());
            classic.add(classicName);
            String key = companyName + "_" + classicName;
            valueMap.merge(key, value, BigDecimal::add);
        }
        XSSFWorkbook workbook1 = new XSSFWorkbook();
        XSSFSheet sheetTwo = workbook1.createSheet();
        XSSFRow row = sheetTwo.createRow(0);
        int j = 1;
        for (String classicName : classic) {
            XSSFCell cell = row.createCell(j);
            cell.setCellValue(classicName);
            indexClassicMap.put(j, classicName);
            j++;
        }
        int i = 1;
        for (String companyName : company) {
            XSSFRow rowIndex = sheetTwo.createRow(i++);
            XSSFCell cell = rowIndex.createCell(0);
            cell.setCellValue(companyName);
            int k = 1;
            for (String classicName : classic) {
                XSSFCell indexCell = rowIndex.createCell(k++);
                String key = companyName + "_" + classicName;
                BigDecimal bigDecimal = valueMap.get(key);
                if (bigDecimal == null) {
                    continue;
                }
                indexCell.setCellValue(bigDecimal.doubleValue());
            }
        }
        File outFile = new File("D:\\222.xlsx");
        OutputStream out = new FileOutputStream(outFile);
        workbook1.write(out);
    }
}
