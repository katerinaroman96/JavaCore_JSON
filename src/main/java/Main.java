import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return staff;
    }

    public static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            Node root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) { // employee
                    NodeList listEmp = node.getChildNodes();
                    List<String> content = new ArrayList<>();
                    for (int j = 0; j < listEmp.getLength(); j++) {
                        Node nodeEmp = listEmp.item(j);
                        if (Node.ELEMENT_NODE == nodeEmp.getNodeType()) { // id, firstName, ...
                            content.add(nodeEmp.getTextContent());
                        }
                    }
                    employees.add(new Employee(
                            Integer.parseInt(content.get(0)),
                            content.get(1),
                            content.get(2),
                            content.get(3),
                            Integer.parseInt(content.get(4))));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return employees;
    }

    public static String readString(String fileJson) {
        String s;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileJson))) {
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> list = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(json);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = new JSONObject((HashMap) jsonArray.get(i));
                Employee employee = gson.fromJson(jsonObject.toJSONString(), Employee.class);
                list.add(employee);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        /*String[] columnMapping = {
                "id",
                "firstName",
                "lastName",
                "country",
                "age"
        };
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName); // 1 домашка ...
        String json = listToJson(list);
        String jsonFileName = "data.json";
        writeString(json, jsonFileName);*/ // ... 1 домашка

       /* String xmlFileName = "data.xml"; // 2 домашка ...
        List<Employee> list2 = parseXML(xmlFileName);
        String json2 = listToJson(list2);
        String jsonFileName2 = "data2.json";
        writeString(json2, jsonFileName2);*/ // ... 2 домашка

        String json3 = readString("new_data.json"); // 3 домашка ...
        List<Employee> listJson =  jsonToList(json3);
        System.out.println(listJson); // ... 3 домашка

    }

}
