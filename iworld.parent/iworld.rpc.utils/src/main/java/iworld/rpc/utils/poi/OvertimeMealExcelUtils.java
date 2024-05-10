package iworld.rpc.utils.poi;

import com.google.common.collect.ImmutableMap;
import iworld.rpc.utils.DateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author gq.cai
 * @version 1.0
 * @description
 * @date 2024/5/9 17:53
 */
public class OvertimeMealExcelUtils {
    
    private static final String WORK_DAY = "工作日";
    
    private static TreeMap<Date, ReadRowData> rowMap = new TreeMap<>(new Comparator<Date>() {
        @Override
        public int compare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }
    });
    
    private static Map<String, Integer> nameSort = new ImmutableMap.Builder<String, Integer>().put("徐良", 1).put("蔡国庆", 2)
            .put("连帅兵", 3).put("张元能", 4).put("薛文明", 5).put("吴瑞金", 6).put("刘昕博", 7).build();
    
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        String packageName = DateUtil.date2String(date, DateUtil.DEFAULT_PATTERN_PACKAGE);
        readFile(packageName);
        String times = "01";
        readAndWriteFile(packageName, times);
        readAndWriteDetailFile(packageName, times);
    }
    
    private static void readFile(String packageName) throws Exception {
        File parentFile = new File("D:\\m\\document/jb/" + packageName);
        File[] files = parentFile.listFiles();
        File inputFile = Objects.requireNonNull(parentFile.listFiles())[0];;
        Workbook workbook = new XSSFWorkbook(inputFile);
        Iterator<Sheet> iterator = workbook.iterator();
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                String name = row.getCell(1).getStringCellValue();
                Cell dateCell = row.getCell(6);
                Date date = dateCell.getDateCellValue();
                ReadRowData readRowData = rowMap.get(date);
                if (readRowData == null) {
                    readRowData = new ReadRowData();
                    rowMap.put(date, readRowData);
                }
                Cell dayTypeCell = row.getCell(7);
                readRowData.setOffDay(!dayTypeCell.getStringCellValue().equals(WORK_DAY));
                readRowData.getNames().add(name);
                readRowData.setDayType(dayTypeCell.getStringCellValue());
            }
        }
    }
    
    private static void readAndWriteFile(String packageName, String times) throws Exception {
        File inputFile = new File("D:\\doc\\发票\\餐饮/费用单据明细列表-加班餐费和打车费用明细.xlsx");
        
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(1);
        int i = 4;
        int totalAmount = 0;
        for (Map.Entry<Date, ReadRowData> entry : rowMap.entrySet()) {
            Row row = sheet.getRow(i);
            Cell dateCell = row.getCell(0);
            Date date = entry.getKey();
            String strDate = DateUtil.date2String(date, DateUtil.DEFAULT_PATTERN_POINT);
            dateCell.setCellValue(strDate);
            Cell reasonCell = row.getCell(1);
            reasonCell.setCellValue("需求开发");
            Cell namesCell = row.getCell(2);
            ReadRowData rowData = entry.getValue();
            List<String> names = rowData.getNames();
            String strNames = names.stream().sorted(Comparator.comparingInt(o -> nameSort.get(o))).collect(Collectors.joining(" "));
            namesCell.setCellValue(strNames);
            Cell amountCell = row.getCell(3);
            int amount;
            if (rowData.isOffDay) {
                amount = names.size() * 30 * 2;
            } else {
                amount = names.size() * 30;
            }
            totalAmount += amount;
            amountCell.setCellValue(amount);
            i++;
        }
        sheet.getRow(26).getCell(3).setCellValue(totalAmount);
        File parentFile = new File("D:\\doc\\发票\\餐饮\\" + packageName + times);
        if (!parentFile.exists()) {
            parentFile.createNewFile();
        }
        File outputFile = new File("D:\\doc\\发票\\餐饮\\" + packageName + times + "/费用单据明细列表-加班餐费和打车费用明细.xlsx");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        workbook.write(new FileOutputStream(outputFile));
    }
    
    private static void readAndWriteDetailFile(String packageName, String times) throws Exception {
        File inputFile = new File("D:\\doc\\发票\\餐饮/费用详情.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(0);
        int i = 1;
        int totalAmount = 0;
        Map<String, Integer> nameAmountMap = new HashMap<>();
        for (Map.Entry<Date, ReadRowData> entry : rowMap.entrySet()) {
            Row row = sheet.getRow(i);
            Cell dateCell = row.createCell(0);
            Date date = entry.getKey();
            String strDate = DateUtil.date2String(date, DateUtil.DEFAULT_PATTERN_POINT);
            dateCell.setCellValue(strDate);
            ReadRowData rowData = entry.getValue();
            List<String> names = rowData.getNames();
            int amount = 0;
            int j = 1;
            for (Map.Entry<String, Integer> subEntry : nameSort.entrySet()) {
                Cell cell = row.getCell(subEntry.getValue());
                if (names.contains(subEntry.getKey())) {
                    int curAmount = rowData.isOffDay ? 60 : 30;
                    cell.setCellValue(curAmount);
                    nameAmountMap.merge(subEntry.getKey(), curAmount, Integer::sum);
                    amount += curAmount;
                } else {
                    cell.setCellValue(0);
                }
                j++;
            }
            row.getCell(j++).setCellValue(rowData.getDayType());
            row.getCell(j).setCellValue(amount);
            i++;
            totalAmount += amount;
        }
        Row row = sheet.getRow(23);
        int totalNameAmount = 0;
        for (Map.Entry<String, Integer> subEntry : nameSort.entrySet()) {
            Cell cell = row.getCell(subEntry.getValue());
            Integer value = nameAmountMap.get(subEntry.getKey());
            cell.setCellValue(value);
            totalNameAmount += value;
        }
        Cell cell = row.getCell(9);
        cell.setCellValue(totalAmount);
        Cell totalNameCell = row.getCell(10);
        totalNameCell.setCellValue(totalNameAmount);
        File outputFile = new File("D:\\doc\\发票\\餐饮\\" + packageName + times + "/费用详情.xlsx");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        workbook.write(new FileOutputStream(outputFile));
    }
    
    
    
    public static class ReadRowData {
        private List<String> names = new ArrayList<>();
        /**
         * 是否休息日
         */
        private boolean isOffDay;
        
        private String dayType;
    
        public List<String> getNames() {
            return names;
        }
    
        public void setNames(List<String> names) {
            this.names = names;
        }
    
        public boolean isOffDay() {
            return isOffDay;
        }
    
        public void setOffDay(boolean offDay) {
            isOffDay = offDay;
        }
    
        public String getDayType() {
            return dayType;
        }
    
        public void setDayType(String dayType) {
            this.dayType = dayType;
        }
    }
}
