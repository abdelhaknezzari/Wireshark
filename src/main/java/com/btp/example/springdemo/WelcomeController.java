package com.btp.example.springdemo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class WelcomeController {

    private static final String strDefine = "Hello, %s!";

    @GetMapping("/welcome")
    public Welcome handleWelcome(@RequestParam(value = "name", defaultValue = "Enthusiast") String name) {
        return new Welcome(String.format(strDefine, name));
    }

    @GetMapping("/sniffByDestination")
    public List<Map<String,String>> getSniffByDestination(@RequestParam(value = "dest") String dest) throws FileNotFoundException {
        return readCsv().stream().filter(map -> map.get("Destination").equals(dest)).toList();
    }


    @GetMapping("/sniff_traces")
    public List<Map<String,String>> getSniffTraces() throws FileNotFoundException {

        return readCsv();

    }

    private static List<Map<String, String>> readCsv() {
        List<List<String>> records = new ArrayList<>();
        Resource resource = new ClassPathResource("data.csv");
        System.out.println("Resource exists: " + resource.exists());

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return records.stream().skip(0).map(
                data -> {
                    Map<String, String> record = new HashMap<>();
                    record.put("ID", data.get(0));
                    record.put("Time", data.get(1));
                    record.put("Source", data.get(2));
                    record.put("Destination", data.get(3));
                    record.put("Protocol", data.get(4));
                    record.put("Length", data.get(5));
                    record.put("Info", data.get(6));
                    return record;
                }
        ).toList();
    }
}