package models.TestData;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {
    private String name = "";
    private Map<String/*test#-and-rowName*/, Map<String /*column-name*/, String /*cell-value*/>> tests = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Map<String, String>> getTests() {
        return tests;
    }

    public void setTests(Map<String, Map<String, String>> tests) {
        this.tests = tests;
    }

    /**
     * A test == {
     * <br />- testNumber: {
     * <br />- - columnName: cellValue,
     * <br />- - columnName: cellValue
     * <br />}}
     * <br />
     * <br />this.tests == {
     * <br />- test#: test,
     * <br />- test#, test
     * <br />}
     * @param testNumber <br />:: is the number parsed from a test case's name. Ex: Create_Playlist_10 ... testNumber == 10
     * @param columnNameAndValue <br />:: KEY == column-header-name, <br />:: VALUE == cell-value-that-intersects-current-row-and-column-header>
     * @return BOOLEAN: true == add was successful, false == wasn't added
     */
    public boolean addTest(String testNumber, Map<String, String> columnNameAndValue) {
        // TRACKS IF TEST-ADDing IS SUCCESS OR FAIL
        boolean isAdded = false;
        int sizeBefore = tests.size();

        // IF TEST # ALREADY EXIST
        if (tests.containsKey(testNumber)) {
            for (String columnName : columnNameAndValue.keySet()) {
                tests.get(testNumber).put(columnName, columnNameAndValue.get(columnName));
            }
            if (tests.size() > sizeBefore) isAdded = true;
        }

        // IF TEST # DOESN'T EXIST
        if (!tests.containsKey(testNumber)) {
            tests.put(testNumber, columnNameAndValue);
            if (tests.containsKey(testNumber)) isAdded = true;
        }

        // RETURN
        return isAdded;
    }
}