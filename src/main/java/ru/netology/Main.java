package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        //printListOfEmployee(list);

        String json = listToJson(list);

        writeString(json, "data.json");


        // task 2 =========================================
        fileName = "data.xml";
        List<Employee> list2 = parseXML(fileName);
        //printListOfEmployee(list);

        json = listToJson(list2);

        writeString(json, "data2.json");

        // task 3 =========================================
        fileName = "data.json";
        json = readString(fileName);

        List<Employee> list3 = jsonToList(json);
        printListOfEmployee(list3);
    }

    public static List<Employee> jsonToList(String json) {

        List<Employee> employees = new ArrayList<>();
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(json);

            for (Object object : jsonArray) {
                Employee employee = gson.fromJson(object.toString(), Employee.class);
                employees.add(employee);
            }
        }
        catch (Exception e){
            System.out.println("Error while jsonToList: " + e.getMessage());
        }

        return employees;
    }

    public static String readString(String fileName) {
        StringBuilder sb = new StringBuilder();
        try(FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader)){

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s);
            }
        }
        catch (Exception e){
            System.out.println("Error while readString: " + e.getMessage());
        }

        return sb.toString();
    }

    private static void printListOfEmployee(List<Employee> list) {
        for(Employee employee : list){
            System.out.println(employee.toString());
        }
    }

    public static List<Employee> parseXML(String fileName) {

        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileName);

            Node rootNode = document.getDocumentElement();
            NodeList nodeList = rootNode.getChildNodes();
            for (int i = 0 ; i < nodeList.getLength() ; i++){
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element employee = (Element) node;

                    long id = Long.parseLong(employee.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = employee.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());

                    employees.add(new Employee(id, firstName, lastName, country, age));
                }
            }
        }
        catch (Exception e){
            System.out.println("Error while parseXML: " + e.getMessage());
        }

        return employees;
    }

    public static void writeString(String text, String fileName) {

        try(FileWriter fileWriter = new FileWriter(fileName)){
            fileWriter.write(text);
            fileWriter.flush();
            System.out.println("Успешно записано в " + fileName);
        }
        catch (Exception e){
            System.out.println("Error while writeToFile: " + fileName + ": " + e.getMessage());
        }
    }

    public static String listToJson(List<Employee> list) {

        Type listType = new TypeToken<List<Employee>>() {}.getType();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(list, listType);

        return  jsonElement.toString();
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {

        List<Employee> employees = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))){
            ColumnPositionMappingStrategy columnPositionMappingStrategy = new ColumnPositionMappingStrategy();
            columnPositionMappingStrategy.setColumnMapping(columnMapping);
            columnPositionMappingStrategy.setType(Employee.class);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(columnPositionMappingStrategy)
                    .build();

            employees = csvToBean.parse();
        }
        catch (Exception e){
            System.out.println("Error while parseCSV: " + e.getMessage());
        }

        return  employees;
    }
}
