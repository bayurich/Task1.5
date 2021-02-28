package ru.netology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    final static String TEST_PATH = "src/test/resources/";
    final static String TEST_JSON_FILE = "temp.json";

    @org.junit.jupiter.api.BeforeAll
    public static void befInit(){
        clear(TEST_PATH + TEST_JSON_FILE);
    }

    @org.junit.jupiter.api.AfterAll
    public static void aftInit(){
        clear(TEST_PATH + TEST_JSON_FILE);
    }

    @org.junit.jupiter.api.Test
    public void testClassEmployee()
    {
        final long expectedId = 1;
        final String expectedFirstName = "John";
        final String expectedLastName = "Smith";
        final String expectedCountry = "USA";
        final int expectedAge = 25;
        final String expectedString = "Employee{id=1, firstName='John', lastName='Smith', country='USA', age=25}";

        Employee employee = new Employee(expectedId,expectedFirstName,expectedLastName,expectedCountry,expectedAge);

        Assertions.assertEquals(expectedId, employee.getId());
        Assertions.assertEquals(expectedFirstName, employee.getFirstName());
        Assertions.assertEquals(expectedLastName, employee.getLastName());
        Assertions.assertEquals(expectedCountry, employee.getCountry());
        Assertions.assertEquals(expectedAge, employee.getAge());
        Assertions.assertEquals(expectedString, employee.toString());
    }

    @org.junit.jupiter.api.Test
    public void testMethodJsonToList()
    {
        final String inputString = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        final List<Employee> expectedList = Arrays.asList(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23));

        List<Employee> resultList = Main.jsonToList(inputString);

        Assertions.assertEquals(expectedList.size(), resultList.size());
        Assertions.assertTrue(isEmployeesEquals(expectedList, resultList));
    }

    @org.junit.jupiter.api.Test
    public void testMethodListToJson(){
        final List<Employee> inputList = Arrays.asList(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23));
        final String expectedString = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";

        String resultString = Main.listToJson(inputList);

        Assertions.assertEquals(expectedString, resultString);
    }

    @org.junit.jupiter.api.Test
    public void testMethodParseCSV(){
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        final List<Employee> expectedList = Arrays.asList(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23));

        List<Employee> resultList = Main.parseCSV(columnMapping, TEST_PATH + "test.csv");

        Assertions.assertEquals(expectedList.size(), resultList.size());
        Assertions.assertTrue(isEmployeesEquals(expectedList, resultList));
    }

    @org.junit.jupiter.api.Test
    public void testMethodParseXML(){
        final List<Employee> expectedList = Arrays.asList(
                new Employee(1, "John", "Smith", "USA", 25),
                new Employee(2, "Inav", "Petrov", "RU", 23));

        List<Employee> resultList = Main.parseXML(TEST_PATH + "test.xml");

        Assertions.assertEquals(expectedList.size(), resultList.size());
        Assertions.assertTrue(isEmployeesEquals(expectedList, resultList));
    }

    @org.junit.jupiter.api.Test
    public void testMethodReadString(){
        final String expectedString = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";

        String resultString = Main.readString(TEST_PATH + "test.json");

        Assertions.assertEquals(expectedString, resultString);
    }



    @org.junit.jupiter.api.Test
    public void testMethodWriteString(){
        final String inputString = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
        final String outputFile = TEST_PATH + TEST_JSON_FILE;

        Main.writeString(inputString, outputFile);

        File file = new File(outputFile);
        Assertions.assertTrue(file.exists());

        String resultString = Main.readString(outputFile);
        Assertions.assertEquals(inputString, resultString);
    }



    private boolean isEmployeesEquals(List<Employee> expectedList, List<Employee> resultList) {
        int i = 0;
        for(Employee expectedEmployee : expectedList){
            if (!isEmployeeEquals(expectedEmployee, resultList.get(i))) return false;
            i++;
        }

        return true;
    }

    private boolean isEmployeeEquals(Employee e1, Employee e2) {
        return e1.getId() == e2.getId() &&
                e1.getFirstName().equals(e2.getFirstName()) &&
                e1.getLastName().equals(e2.getLastName()) &&
                e1.getCountry().equals(e2.getCountry()) &&
                e1.getAge() == e2.getAge();
    }

    private static void clear(String fileName) {
        File file = new File(fileName);
        if (file.exists()){
            file.delete();
        }
    }
}
