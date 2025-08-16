package iworld.rpc.utils.poi;

import com.google.common.collect.ImmutableMap;
import iworld.rpc.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
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
    
    private static TreeMap<Date, ReadRowDataExtend> cusRowMap = new TreeMap<>(new Comparator<Date>() {
        @Override
        public int compare(Date o1, Date o2) {
            return o1.compareTo(o2);
        }
    });
    
    private static Map<String, Integer> nameSort = new ImmutableMap.Builder<String, Integer>().put("徐良", 1).put("蔡国庆", 2)
            .put("连帅兵", 3).put("李文龙", 4).put("吴瑞金", 5).put("刘昕博", 6).put("陈少博", 7).build();
    
    private static Map<Integer, String> indexNameSort = new ImmutableMap.Builder<Integer, String>().put(3, "徐良").put(4, "蔡国庆")
            .put(5, "连帅兵").put(6, "李文龙").put(7, "吴瑞金").put(8, "刘昕博").put(9, "陈少博").build();
    
    public static void main(String[] args) throws Exception {
        Date date = new Date();
        String packageName = DateUtil.date2String(date, DateUtil.DEFAULT_PATTERN_PACKAGE);
        readFile(packageName);
        String times = "01";
        BigDecimal totalPrice = readAndWriteFile(packageName, times);
        readAndWriteDetailFile(packageName, times);
        System.out.println(totalPrice);
        calcInvoicePrice(totalPrice);
//        String times = "02";
//        readFileFromCus(packageName, times);
//        readAndWriteFileFromCus(packageName, times);
//        readAndWriteDetailFileFromCus(packageName, times);
    }
    
    /**
     * 公司加班文件
     * @param packageName
     * @throws Exception
     */
    private static void readFile(String packageName) throws Exception {
        File parentFile = new File("E:\\m\\document/jb/" + packageName);
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
                if (!nameSort.containsKey(name)) {
                    continue;
                }
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
    
    private static BigDecimal readAndWriteFile(String packageName, String times) throws Exception {
        File inputFile = new File("D:\\doc\\发票\\餐饮/费用单据明细列表-加班餐费和打车费用明细.xlsx");
        
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(1);
        int i = 4;
        BigDecimal totalAmount = BigDecimal.ZERO;
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
            BigDecimal amount = null;
            if (rowData.isOffDay) {
                amount = BigDecimal.valueOf(names.size()).multiply(BigDecimal.valueOf(30)).multiply(BigDecimal.valueOf(2));
            } else {
                amount = BigDecimal.valueOf(names.size()).multiply(BigDecimal.valueOf(30));
            }
            totalAmount = totalAmount.add(amount);
            amountCell.setCellValue(amount.intValue());
            i++;
        }
        sheet.getRow(34).getCell(3).setCellValue(totalAmount.intValue());
        File parentFile = new File("D:\\doc\\发票\\餐饮\\" + packageName + times);
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        File outputFile = new File("D:\\doc\\发票\\餐饮\\" + packageName + times + "/费用单据明细列表-加班餐费和打车费用明细.xlsx");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        workbook.write(new FileOutputStream(outputFile));
        return totalAmount;
    }
    
    private static void readAndWriteDetailFile(String packageName, String times) throws Exception {
        File inputFile = new File("D:\\doc\\发票\\餐饮/费用详情.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(0);
        int i = 1;
        int totalAmount = 0;
        Row firstRow = sheet.getRow(0);
        int j = 1;
        for (Map.Entry<String, Integer> subEntry : nameSort.entrySet()) {
            Cell cell = firstRow.getCell(subEntry.getValue());
            cell.setCellValue(subEntry.getKey());
            j++;
        }
        firstRow.getCell(j++).setCellValue("备注");
        firstRow.getCell(j).setCellValue("总额");
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
            j = 1;
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
        Row row = sheet.getRow(i);
        Cell endOneCell = row.getCell(0);
        endOneCell.setCellValue("总额");
        int totalNameAmount = 0;
        for (Map.Entry<String, Integer> subEntry : nameSort.entrySet()) {
            Cell cell = row.getCell(subEntry.getValue());
            Integer value = nameAmountMap.get(subEntry.getKey());
            if (value == null) {
                value = 0;
            }
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
    
    private static void calcInvoicePrice(BigDecimal usePrice) {
        File parentFile = new File("D:\\doc\\发票\\餐饮\\未使用");
        if (!parentFile.exists()) {
            return ;
        }
        String[] listName = parentFile.list();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (String fileName : listName) {
            String substring = fileName.substring(0, fileName.lastIndexOf("."));
            String[] s = substring.split("_");
            String sPrice = s[s.length - 1];
            totalPrice = totalPrice.add(BigDecimal.valueOf(Double.parseDouble(sPrice)));
        }
        System.out.println(totalPrice.subtract(usePrice));
    }
    
    private static void readFileFromCus(String packageName, String times) throws Exception {
        File parentFile = new File("D:\\m\\document/jb/" + packageName + "/" + times);
        File inputFile = Objects.requireNonNull(parentFile.listFiles())[0];;
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(4);
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < 3) {
                continue;
            }
            int cellIndex = 2;
            Cell dateCell = row.getCell(cellIndex);
            CellType cellType = dateCell.getCellType();
            if (CellType.NUMERIC != cellType) {
                break;
            }
            Date date = dateCell.getDateCellValue();
            
            List<PersonData> personDataList = new ArrayList<>();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cellIndex = cell.getColumnIndex();
                if (cellIndex < 3) {
                    continue;
                }
                if (cellIndex > 8) {
                    break;
                }
                int cellValue = getCellValue(cell);
                if (cellValue > 0) {
                    PersonData personData = new PersonData();
                    personData.setName(indexNameSort.get(cellIndex));
                    personData.setAmount(cellValue);
                    personDataList.add(personData);
                }
            }
            if (CollectionUtils.isNotEmpty(personDataList)) {
                ReadRowDataExtend readRowDataExtend = cusRowMap.get(date);
                if (readRowDataExtend == null) {
                    readRowDataExtend = new ReadRowDataExtend();
                    cusRowMap.put(date, readRowDataExtend);
                }
                readRowDataExtend.setPersonDataList(personDataList);
            }
        }
    }
    
    private static int getCellValue(Cell cell) {
        int cellValue = 0;
        CellType cellType = cell.getCellType();
        if (CellType.NUMERIC == cellType) {
            double numericCellValue = cell.getNumericCellValue();
            cellValue = (int)numericCellValue;
        } else if (CellType.STRING == cellType) {
            String stringCellValue = cell.getStringCellValue();
            if (StringUtils.isNotBlank(stringCellValue)) {
                cellValue = Integer.valueOf(stringCellValue);
            }
        }
        return cellValue;
    }
    
    
    private static void readAndWriteFileFromCus(String packageName, String times) throws Exception {
        File inputFile = new File("D:\\doc\\发票\\餐饮/费用单据明细列表-加班餐费和打车费用明细.xlsx");
        
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(1);
        int i = 4;
        int totalAmount = 0;
        System.out.println(cusRowMap.size());
        for (Map.Entry<Date, ReadRowDataExtend> entry : cusRowMap.entrySet()) {
            Row row = sheet.getRow(i);
            Cell dateCell = row.getCell(0);
            if (dateCell == null) {
                System.out.println(row.getRowNum());
            }
            Date date = entry.getKey();
            String strDate = DateUtil.date2String(date, DateUtil.DEFAULT_PATTERN_POINT);
            dateCell.setCellValue(strDate);
            Cell reasonCell = row.getCell(1);
            reasonCell.setCellValue("需求开发");
            Cell namesCell = row.getCell(2);
            ReadRowDataExtend rowData = entry.getValue();
            List<PersonData> personDataList = rowData.getPersonDataList();
            String strNames = personDataList.stream().sorted(Comparator.comparingInt(o -> nameSort.get(o.getName()))).map(PersonData::getName).collect(Collectors.joining(" "));
            namesCell.setCellValue(strNames);
            Cell amountCell = row.getCell(3);
            int amount = personDataList.stream().map(PersonData::getAmount).reduce(0, Integer::sum);
            totalAmount += amount;
            amountCell.setCellValue(amount);
            i++;
        }
        sheet.getRow(26).getCell(3).setCellValue(totalAmount);
        File parentFile = new File("D:\\doc\\发票\\餐饮\\" + packageName + times);
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        File outputFile = new File("D:\\doc\\发票\\餐饮\\" + packageName + times + "/费用单据明细列表-加班餐费和打车费用明细.xlsx");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        workbook.write(new FileOutputStream(outputFile));
    }
    
    private static void readAndWriteDetailFileFromCus(String packageName, String times) throws Exception {
        File inputFile = new File("D:\\doc\\发票\\餐饮/费用详情.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
        Sheet sheet = workbook.getSheetAt(0);
        int i = 1;
        int totalAmount = 0;
        Row firstRow = sheet.getRow(0);
        int k = 1;
        for (Map.Entry<String, Integer> subEntry : nameSort.entrySet()) {
            Cell cell = firstRow.getCell(subEntry.getValue());
            cell.setCellValue(subEntry.getKey());
            k++;
        }
        firstRow.getCell(k++).setCellValue("备注");
        firstRow.getCell(k).setCellValue("总额");
        Map<String, Integer> nameAmountMap = new HashMap<>();
        for (Map.Entry<Date, ReadRowDataExtend> entry : cusRowMap.entrySet()) {
            Row row = sheet.getRow(i);
            Cell dateCell = row.createCell(0);
            Date date = entry.getKey();
            String strDate = DateUtil.date2String(date, DateUtil.DEFAULT_PATTERN_POINT);
            dateCell.setCellValue(strDate);
            ReadRowDataExtend rowData = entry.getValue();
            List<PersonData> personDataList = rowData.getPersonDataList();
            Map<String, PersonData> namePersonMap = personDataList.stream().collect(Collectors.toMap(PersonData::getName, Function.identity(), (v1, v2) -> v1));
            int amount = 0;
            int j = 1;
            for (Map.Entry<String, Integer> subEntry : nameSort.entrySet()) {
                Cell cell = row.getCell(subEntry.getValue());
                if (namePersonMap.containsKey(subEntry.getKey())) {
                    PersonData personData = namePersonMap.get(subEntry.getKey());
                    Integer curAmount = personData.getAmount();
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
        Row row = sheet.getRow(i);
        Cell endOneCell = row.getCell(0);
        endOneCell.setCellValue("总额");
        int totalNameAmount = 0;
        for (Map.Entry<String, Integer> subEntry : nameSort.entrySet()) {
            Cell cell = row.getCell(subEntry.getValue());
            String key = subEntry.getKey();
            int value = nameAmountMap.get(key) == null ? 0 : nameAmountMap.get(key);
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
        protected boolean isOffDay;
        
        protected String dayType;
    
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
    
    public static class ReadRowDataExtend extends ReadRowData {
        
        private List<PersonData> personDataList = new ArrayList<>();
    
        public List<PersonData> getPersonDataList() {
            return personDataList;
        }
    
        public void setPersonDataList(List<PersonData> personDataList) {
            this.personDataList = personDataList;
        }
    }
    
    public static class PersonData {
        
        private String name;
        
        private Integer amount;
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public Integer getAmount() {
            return amount;
        }
    
        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }
}
