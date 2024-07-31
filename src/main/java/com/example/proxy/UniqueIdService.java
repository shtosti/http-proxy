package com.example.proxy;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

// ADDED: to keep track of ids and urls
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UniqueIdService {

    // TODO understand this line
    private final Set<String> uniqueIds = ConcurrentHashMap.newKeySet();
    private final RestTemplate restTemplate;
    // ADDED: initialize the csv output file
    private static final String CSV_FILE_PATH = "requests.csv";
    //

    public UniqueIdService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // public void addId(String id) {
    //     uniqueIds.add(id);
    // }
    // ADDED:
    public void addId(String id, String url) {
        uniqueIds.add(id);
        saveRequestToCSV(id, url);
    }
    //

    public int getUniqueIdCount() {
        return uniqueIds.size();
    }

    public void callExternalEndpoint(String url) {
        int uniqueIdCount = getUniqueIdCount();
        restTemplate.postForObject(url, uniqueIdCount, String.class);
    }

    private void saveRequestToCSV(String requestId, String url) {
        try (FileWriter fileWriter = new FileWriter(CSV_FILE_PATH, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.printf("%s,%s%n", requestId, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 60000) // Logs the count every minute
    public void logUniqueIdCount() {
        int count = getUniqueIdCount();
        System.out.println("Unique ID count: " + count);
        System.out.println("     * * *     ");
    }
}
