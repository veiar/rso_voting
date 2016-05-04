package pl.edu.pw.elka.rso.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Artur on 2016-05-04.
 */
@RestController
public class DnsController {

    private Map<String, List<String>> lookupTable = new HashMap<>();
    private Random random = new Random();

    @PostConstruct
    public void init() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        InputStream inputStream = new FileInputStream("src/main/resources/application.properties");
        final Properties prop = new Properties();
        prop.load(inputStream);
        prop.stringPropertyNames().stream()
                .forEach(propName -> lookupTable.put(propName, Arrays.asList(prop.getProperty(propName).split(","))));
    }

    @RequestMapping("/{domainName:.+}")
    @ResponseStatus(HttpStatus.OK)
    public Object resolve(@PathVariable String domainName)
    {
        if (!lookupTable.containsKey(domainName)) {
            return ResponseEntity.notFound().build();
        }
        List<String> ips = lookupTable.get(domainName);
        return ips.get(random.nextInt(ips.size()));
    }
}
