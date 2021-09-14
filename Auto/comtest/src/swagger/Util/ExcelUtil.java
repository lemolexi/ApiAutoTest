package swagger.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @Description: excel的处理
 * @Author: wenmin
 * @Date: 2020/11/6 14:06
 * @Param:
 * @Return:
 */
public class ExcelUtil {

    public static String PATH_ROOT() {
        return new File("src").getAbsolutePath();
    }

    protected static final Logger logger = Logger.getLogger(PATH_ROOT() + "swagger\\test.log");

    /**
     * @description: 判断excel文件是否存在
     * @Param: [filePath]
     * @return: java.lang.Boolean
     * @author: wenmin
     * @Date: 2020/11/6 14:30
     **/
    public static Boolean isFileExist(String filePath) {
        try {
            if (null != filePath) {
                File file = new File(filePath);
                if (!file.exists()) {
                    //创建文件
                    file.createNewFile();
                }
                String fileName = file.getName();
                if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                    //文件已存在
                    String strFileName = file.getAbsolutePath();
                    CheckUtil.printMsg("[文件名]：" + strFileName);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JSONArray getXlsExcelData(String fileName) {
        String filePath = null;
        if (fileName.contains("/")) {
            filePath = fileName;
        } else {
            filePath = ExcelUtil.class.getClassLoader().getResource(fileName).getPath();
        }
        CheckUtil.printMsg("[读取文件路径]：" + filePath);
        JSONObject titles = queryTopCellTitle(filePath);
        JSONArray arr = new JSONArray();
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            jxl.Workbook wb = jxl.Workbook.getWorkbook(fis);
            jxl.Sheet[] sheets = wb.getSheets();
            fis.close();
            jxl.Sheet sheet = sheets[0];

            int iTotalRows = sheet.getRows();
            int iTotalColumns = sheet.getColumns();

            for (int i = 1; i < iTotalRows; i++) { //不读取标题列
                jxl.Cell[] curCell = sheet.getRow(i);
                JSONObject json = new JSONObject();
                Iterator keys = titles.keys();
                if (titles.has("isRun")) {
                    if (curCell[titles.getInt("isRun") - 1].getContents().equals("Y")) {
                        for (int j = 0; j < iTotalColumns; j++) {
                            json.put(keys.next(), curCell[j].getContents());
                        }
                    } else {
                        continue;
                    }
                } else {
                    for (int j = 0; j < iTotalColumns; j++) {
                        json.put(keys.next(), curCell[j].getContents());
                    }
                }
                arr.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static JSONObject queryTopCellTitle(String filePath) {
        Workbook workbook = null;
        //sheet页name
        Sheet sheet = null;
        String SheetName = "";
        Row row = null;
        Cell cell = null;//验证文件格式

        try {
            isFileExist(filePath);
            InputStream is = new FileInputStream(new File(filePath));

            String extString = filePath.substring(filePath.lastIndexOf("."));

            if (".xls".equals(extString)) {
                workbook = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                workbook = new XSSFWorkbook(is);
            }

            JSONObject cellList = new JSONObject();
            //只取第一个sheet页
            sheet = workbook.getSheetAt(0);
            if (sheet != null) {
                SheetName = sheet.getSheetName();
                //获取第一行
                row = sheet.getRow(0);
                //遍历第一行
                int o = 0;
                Iterator<Cell> it = row.iterator();
                while (it.hasNext()) {
                    cell = it.next();
                    o++;
                    if (cell == null) continue;
                    String c = String.valueOf(cell);
                    cellList.put(c, o);
                }
                return cellList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //对EXCEL写入数据；
    public static void executeWiteExcel(String filePath, Object[][] cellData, String[] titles) {
        FileOutputStream fout = null;
        File file = new File(filePath);
        try {
            isFileExist(filePath);

            String extString = filePath.substring(filePath.lastIndexOf("."));

            if (".xls".equals(extString)) {
                xlsExcel(filePath, cellData, titles);
            } else if (".xlsx".equals(extString)) {
                xlsxExcel(filePath, cellData, titles);
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }


    public static void xlsExcel(String filePath, Object[][] cellData, String[] titles) throws Exception {
        //创建一个文件
        File file = new File(filePath);
        file.createNewFile();
        InputStream inputStream = new FileInputStream(new File(filePath));
        //创建Excel文件薄
//        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表sheeet
        HSSFSheet sheet = workbook.createSheet();
        //创建第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        if (sheet.getLastRowNum() <= 0 && titles != null) {
            for (int i = 0; i < titles.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(titles[i]);
            }
        }
        HSSFRow nextrow = null;
        HSSFCell cell2 = null;
        //追加数据
        if (cellData != null) {
            for (int r = 0; r < cellData.length; r++) {
                nextrow = sheet.createRow(sheet.getLastRowNum() + 1);
                for (int c = 0; c < cellData[r].length; c++) {
                    cell2 = nextrow.createCell(c);
                    cell2.setCellValue(cellData[r][c].toString().trim());
                    CheckUtil.printMsg(String.format("写入：第%s行第%s列,值=%s", r + 1, c + 1, cellData[r][c].toString().trim()));
                }
            }
        }

        FileOutputStream stream = FileUtils.openOutputStream(file);
        workbook.write(stream);
        stream.close();

    }

    public static void xlsxExcel(String filePath, Object[][] cellData, String[] titles) throws Exception {
        //创建Excel文件薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheeet
        Sheet sheet = workbook.createSheet();
        //创建第一行
        Row row = sheet.createRow(0);
        Cell cell = null;
        if (sheet.getLastRowNum() <= 0 && titles != null) {
            for (int i = 0; i < titles.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(titles[i]);
            }
        }
        Row nextrow = null;
        Cell cell2 = null;
        //追加数据
        if (cellData != null) {
            for (int r = 0; r < cellData.length; r++) {
                nextrow = sheet.createRow(sheet.getLastRowNum() + 1);
                for (int c = 0; c < cellData[r].length; c++) {
                    cell2 = nextrow.createCell(c);
                    cell2.setCellValue(cellData[r][c].toString().trim());
                    CheckUtil.printMsg(String.format("写入：第%s行第%s列,值=%s", r + 1, c + 1, cellData[r][c].toString().trim()));
                }
            }
        }
        //创建一个文件
        File file = new File(filePath);
        file.createNewFile();
        FileOutputStream stream = FileUtils.openOutputStream(file);
        workbook.write(stream);
        stream.close();

    }


    @Test
    public void test() throws Exception {
        Object[][] obj = {
                {"test1", "test1", "success"},
                {"test2", "{\"JSON\":\"测试中文-abc-test,23454555\"}", "fail"},
        };
        String[] titlemap = {"caseNO", "caseDesc", "casePreResult"};

        String filePath = "D:\\workspace\\idea\\test\\src\\swagger\\casebyexcel\\3.xls";
        String filePath1 = "D:/workspace/idea/test/src/swagger/casebyexcel/esignproRestAccountList.xls";

//        executeWiteExcel(filePath, obj, titlemap);
        String path2 = "D:\\workspace\\idea\\test\\src\\swagger\\casebyexcel\\V1AccountsInnerAccountsCreate.xls";

//        JSONObject str1 = queryTopCellTitle(filePath1);

        JSONArray str1 = getXlsExcelData("esignproRestAccountList.xls");
        System.out.println(str1.toString());
    }


}
