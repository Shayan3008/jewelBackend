package com.jewelbackend.backend.jobs;

import java.time.LocalDate;

import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.setup.models.GoldRate;

@Component
public class GoldRateJob {
    final WebClient webClient;
    final DaoFactory daoFactory;

    public GoldRateJob(DaoFactory daoFactory) {
        this.webClient = WebClient.builder()
                .baseUrl("https://live-metal-prices.p.rapidapi.com/v1/latest//XAU_24K/PKR/GRAM")
                .defaultHeader("X-RapidAPI-Key", "5af7ca3a30mshe8b8a5ea3d6f321p147955jsn3debe620fb4b")
                .defaultHeader("X-RapidAPI-Host", "live-metal-prices.p.rapidapi.com")
                .build();
        this.daoFactory = daoFactory;
    }

    // @Scheduled(fixedDelay = 5000)
    public void getGoldRate() {
        HelperUtils.logMessage(Level.INFO, "Getting current gold rate");
        LocalDate localDate = LocalDate.now();
        String stringDate = HelperUtils.localDateToString(localDate);
        if (!daoFactory.getGoldRateDao().findByDatedString(stringDate).isEmpty()) {
            HelperUtils.logMessage(Level.INFO, "Gold rate already fetched!! returning");
            return;
        }
        MetalPriceDto response = webClient.get().retrieve().bodyToMono(MetalPriceDto.class).block();
        if (response == null || response.getValidationMessage().length > 0) {
            HelperUtils.logMessage(Level.INFO, "Error in fetching response");
            return;
        }
        GoldRate goldRate = new GoldRate();
        goldRate.setDatedString(stringDate);
        goldRate.setPrice(response.getRates().getXau24k());
        daoFactory.getGoldRateDao().save(goldRate);
        HelperUtils.logMessage(Level.INFO, stringDate);
    }
}
