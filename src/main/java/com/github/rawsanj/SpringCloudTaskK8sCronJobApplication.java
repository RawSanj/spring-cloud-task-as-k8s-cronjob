package com.github.rawsanj;

import com.github.rawsanj.model.CurrencyRate;
import com.github.rawsanj.repository.CurrencyRateRepository;
import com.github.rawsanj.service.EmailNotificationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@EnableTask
@SpringBootApplication
public class SpringCloudTaskK8sCronJobApplication {

    private final OkHttpClient okHttpClient;
    private final EmailNotificationService emailNotificationService;
    private final Environment environment;
    private final CurrencyRateRepository currencyRateRepository;

    private static final  Logger LOGGER = LoggerFactory.getLogger(SpringCloudTaskK8sCronJobApplication.class.getName());

    private static final String URL = "https://bitpay.com/api/rates";

    public SpringCloudTaskK8sCronJobApplication(OkHttpClient okHttpClient, EmailNotificationService emailNotificationService, Environment environment, CurrencyRateRepository currencyRateRepository) {
        this.okHttpClient = okHttpClient;
        this.emailNotificationService = emailNotificationService;
        this.environment = environment;
        this.currencyRateRepository = currencyRateRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudTaskK8sCronJobApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return (ApplicationArguments args) -> {

            LOGGER.info("Execution Started at : {}", new SimpleDateFormat().format(new Date()));

            String currency = getValueFromParamKey(args, "currency");

            if (currency != null) {

                Request request = getRequest(URL + "/" + currency);
                Response response = okHttpClient.newCall(request).execute();

                String result = response.body().string();

                LOGGER.info("API Response Status: {}", response.code());

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                CurrencyRate rate = gson.fromJson(result, CurrencyRate.class);

                rate.setCheckedAt(LocalDateTime.now());

                //Save to Database
                currencyRateRepository.save(rate);

                // Notify User when Price goes below/above limits
                notifyUser(args, rate);

            } else {
                Request request = getRequest(URL);
                Response response = okHttpClient.newCall(request).execute();

                String result = response.body().string();

                LOGGER.info("API Response Status: {}", response.code());

                LOGGER.info("API Response: {}", response);

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                CurrencyRate[] rates = gson.fromJson(result, CurrencyRate[].class);

                List<CurrencyRate> currencyRateList = Arrays.asList(rates)
                        .stream()
                        .map(rate -> {
                            rate.setCheckedAt(LocalDateTime.now());
                            return rate;
                        }).collect(Collectors.toList());

                //Save to Database
                currencyRateRepository.saveAll(currencyRateList);
            }
        };
    }

    private void notifyUser(ApplicationArguments args, CurrencyRate rate) {

        Double upperLimit = Double.parseDouble(getValueFromParamKey(args, "upperLimit"));
        Double lowerLimit = Double.parseDouble(getValueFromParamKey(args, "lowerLimit"));

        if ((rate.getRate() > upperLimit || rate.getRate() < lowerLimit)) {

            String subject = null;
            if (rate.getRate() > upperLimit) {
                subject = "BitCoin Price went above " + upperLimit;
            }
            if (rate.getRate() < lowerLimit) {
                subject = "BitCoin Price went below " + lowerLimit;
            }

            if (isEmailServiceConfigured()) {
                String to;
                String email = getValueFromParamKey(args, "emailTo");
                if (email == null) {
                    // notify to admin email itself if emailTo is missing
                    to = environment.getProperty("spring.mail.username");
                } else {
                    to = email;
                }
                emailNotificationService.sendSimpleMessage(to, subject, rate.toString());
            } else {
                throw new RuntimeException("username and password arguments cannot be NULL");
            }
        }
    }

    private String getValueFromParamKey(ApplicationArguments arguments, String key) {
        if (arguments.containsOption(key)) {
            return arguments.getOptionValues(key).get(0);
        } else {
            return null;
        }
    }

    private Boolean isEmailServiceConfigured() {

        String username = environment.getProperty("spring.mail.username");
        String password = environment.getProperty("spring.mail.password");

        if (username == null || username.equalsIgnoreCase("") || password == null || password.equalsIgnoreCase("")) {
            LOGGER.info("username and password arguments cannot be NULL");
            return false;
        }
        return true;
    }

    private Request getRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

}
