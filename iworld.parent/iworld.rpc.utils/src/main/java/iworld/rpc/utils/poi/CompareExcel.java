package iworld.rpc.utils.poi;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * @author gq.cai
 * @version 1.0
 * @description
 * @date 2024/7/25 10:50
 */
public class CompareExcel {
    
    
    private static void readFile(String packageName) throws Exception {
        File parentFile = new File("D:\\m\\document/jb/" + packageName);
        File inputFile = Objects.requireNonNull(parentFile.listFiles())[0];;
        Workbook workbook = new XSSFWorkbook(inputFile);
        Iterator<Sheet> iterator = workbook.iterator();
        Set<PriceRowData> priceRowDataSet = new HashSet<>();
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                String departName = row.getCell(0).getStringCellValue();
                String modelName = row.getCell(1).getStringCellValue();
                PriceRowData priceRowData = new PriceRowData(departName, modelName);
                priceRowDataSet.add(priceRowData);
            }
        }
    }
    
    public static class PriceRowData {
        private String departName;
        
        private String modelName;
        
        public PriceRowData(String departName, String modelName) {
            this.departName = departName;
            this.modelName = modelName;
        }
        public String getDepartName() {
            return departName;
        }
    
        public void setDepartName(String departName) {
            this.departName = departName;
        }
    
        public String getModelName() {
            return modelName;
        }
    
        public void setModelName(String modelName) {
            this.modelName = modelName;
        }
    
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PriceRowData that = (PriceRowData) o;
            return Objects.equals(departName, that.departName) &&
                    Objects.equals(modelName, that.modelName);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(departName, modelName);
        }
    }
}
