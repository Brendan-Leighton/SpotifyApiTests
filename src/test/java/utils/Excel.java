package utils;
// JAVA

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// EXCEL
import models.TestData.Endpoint;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 */
public class Excel {

    private static Excel EXCEL_SHEET;

    private final XSSFSheet sheet;
    private final XSSFRow headers;
    private final Map<String, Endpoint> mappedSheet;

    private Excel(int sheetIndex, String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt(sheetIndex);
        headers = sheet.getRow(0);
        mappedSheet = this.mapSheet();
    }

    public static Excel getExcelSheet(int sheetIndex, String filePath) {
        if (EXCEL_SHEET == null) {
            try {
                EXCEL_SHEET = new Excel(sheetIndex, filePath);
            } catch (IOException e) {
                System.out.println("Surprise Mother-Fucka! Failed to get excel sheet @ utils/Excel.java >> getExcelSheet(). \n Exception: \n" + e);
            }
        }
        return EXCEL_SHEET;
    }

    /**
     *
     * Go through each row in the Excel Spreadsheet and gather the data for easy use by devs
     *
     * @return {
     * <br />- "Endpoint-Name" {
     * <br />- - {@link Endpoint},
     * <br />- - Endpoint
     * <br />- },
     * <br />- "Endpoint-Name" {
     * <br />- - Endpoint,
     * <br />- - Endpoint
     * <br />- }
     * <br />}
     */
    public Map<String, Endpoint> mapSheet() {
        Map<String, Endpoint> allData = new HashMap<>();

        Endpoint currentEndpoint = new Endpoint();

        // iterate sheet's rows
        for (int row = 1; row <= this.sheet.getLastRowNum(); row++) {
            // SETUP
            XSSFRow currentRow = sheet.getRow(row);
            String rowName = sheet.getRow(row).getCell(0).toString();

            // STOP RUNNING WHEN WE FIND A ROW WITH AN EMPTY NAME
            // in the above for-loop, getLastRowNum(): rows which had content before and were set to empty later might still be counted as rows by Excel and Apache POI
            if (rowName.equals("")) {
                allData.put(currentEndpoint.getName(), currentEndpoint);
                break;
            }

            // EXTRACT ENDPOINTS NAME: Get_Playlist_14 -> Get_Playlist
            String testName = rowName.substring(0, rowName.lastIndexOf("_"));

            // HANDLE COMING ACROSS A NEW TEST SET
            if (!currentEndpoint.getName().equals(testName)) {
                if (currentEndpoint.getName() == null) continue;
                allData.put(currentEndpoint.getName(), currentEndpoint);
                currentEndpoint = new Endpoint();
            }

            String testNumber = rowName.substring(rowName.lastIndexOf("_") + 1);

            Map<String, String> columnNameAndValue = new HashMap<>();

            // iterate rows' columns/values
            for (int column = 1; column < currentRow.getLastCellNum(); column++) {
                String rowValue = sheet.getRow(row).getCell(column).toString();
                // if this cell is empty then skip to next column
                if (rowValue.equals("")) continue;
                String header = headers.getCell(column).toString();

                columnNameAndValue.put(header, rowValue);
//                rowData.put(testNumber, columnNameAndValue);
                currentEndpoint.addTest(testNumber, columnNameAndValue);
            }
            allData.put(testName, currentEndpoint);
        }
        return allData;
    }

    public Map<String, Endpoint> getMappedSheet() {
        return mappedSheet;
    }

    /**
     * @param functionName <ol>
     *                     <li>Create_Playlist</li>
     *                     <li>Add_Items_To_Playlist</li>
     *                     <li>Get_Playlist</li>
     *                     <li>Get_Playlists_Items</li>
     *                     <li>Change_Playlists_Details</li>
     *                     <li>Update_Playlists_items</li>
     *                     <li>Remove_Playlists_Items</li>
     *                     <li>Get_Current_Users_Playlists</li>
     *                     <li>Get_Users_Playlists</li>
     *                     <li>Get_Featured_Playlists</li>
     *                     <li>Get_Categorys_Playlists</li>
     *                     <li>Get_Playlists_Cover_Image</li>
     *                     <li>Add_Custom_Playlist_Cover_Image</li>
     *                     <li>Unfollow_Playlist</li>
     *                     </ol>
     * @return
     */
    public Endpoint getFunctionData(int functionName) {
//        Map<String, Map<String, String>> result = new HashMap<>();

        String page = null;

        switch (functionName) {
            case 1:
                page = "Create_Playlist";
                break;
            case 2:
                page = "Add_Items_To_Playlist";
                break;
            case 3:
                page = "Get_Playlist";
                break;
            case 4:
                page = "Get_Playlists_Items";
                break;
            case 5:
                page = "Change_Playlists_Details";
                break;
            case 6:
                page = "Update_Playlists_items";
                break;
            case 7:
                page = "Remove_Playlists_Items";
                break;
            case 8:
                page = "Get_Current_Users_Playlists";
                break;
            case 9:
                page = "Get_Users_Playlists";
                break;
            case 10:
                page = "Get_Featured_Playlists";
                break;
            case 11:
                page = "Get_Categorys_Playlists";
                break;
            case 12:
                page = "Get_Playlists_Cover_Image";
                break;
            case 13:
                page = "Add_Custom_Playlist_Cover_Image";
                break;
            case 14:
                page = "Unfollow_Playlist";
                break;
            default:
                page = "null";
                break;
        }
        return this.mappedSheet.get(page);

//        for (Map.Entry<String, Endpoint> sheetRow : this.mappedSheet.entrySet()) {
//            if (sheetRow.getKey().contains(page)) {
//                result.put(sheetRow.getKey(), sheetRow.getValue());
//            }
//        }
//
//        return result;
    }
}
