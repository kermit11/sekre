package com.kermit11.sekre.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kermit11.sekre.dao.PollDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MissingShowsService
{
    private final PollDao pollDao;

    @Autowired
    public MissingShowsService(@Qualifier("sqlPollRepo") PollDao pollDao) {
        this.pollDao = pollDao;
    }

    public List<String> getMissingShows(LocalDate startFrom, LocalDate until)
    {
        List<LocalDate> existingBroadcastDates = pollDao.getAllBroadcastDates();

        Map<LocalDate, String> holidaysInRange = getHolidays(startFrom, until);

        List<String> missingDates = startFrom.datesUntil(until)
                .filter(date->date.getDayOfWeek() != DayOfWeek.FRIDAY)
                .filter(date->date.getDayOfWeek() != DayOfWeek.SATURDAY)
                .filter(date->!existingBroadcastDates.contains(date))
                .sorted(Comparator.reverseOrder())
                .map(date->formatDateForSearch(date, holidaysInRange.get(date)))
                .collect(Collectors.toList());

        return missingDates;
    }

    private final static HashMap<Month, String> hebrewMonths = new HashMap<Month, String>()
    {{
        put(Month.JANUARY, "ינואר");
        put(Month.FEBRUARY, "פברואר");
        put(Month.MARCH, "מרץ");
        put(Month.APRIL, "אפריל");
        put(Month.MAY, "מאי");
        put(Month.JUNE, "יוני");
        put(Month.JULY, "יולי");
        put(Month.AUGUST, "אוגוסט");
        put(Month.SEPTEMBER, "ספטמבר");
        put(Month.OCTOBER, "אוקטובר");
        put(Month.NOVEMBER, "נובמבר");
        put(Month.DECEMBER, "דצמבר");
    }}
            ;
    private String formatDateForSearch(LocalDate ld,  String holiday)
    {
        return "\""
                + ld.getDayOfMonth()
                +" ב"
                + hebrewMonths.get(ld.getMonth())
                + " "
                + ld.getYear()
                + "\""
                + ((holiday==null)?"":" ("+holiday+")");
    }

    private Map<LocalDate, String> getHolidays(LocalDate startFrom, LocalDate until)
    {
        Map<LocalDate, String> result = new HashMap<>();

        List<LocalDate> allMonths = startFrom.withDayOfMonth(1).datesUntil(until)
                .filter(d->d.getDayOfMonth()==1)
                .collect(Collectors.toList());

        for (LocalDate month : allMonths)
        {
            result.putAll(getHolidaysFor(""+month.getMonthValue(), ""+month.getYear()));
        }

        return result;
    }

    private Map<LocalDate, String> getHolidaysFor(String month, String year)
    {
        RestTemplate restTemplate = new RestTemplate();
        String calendarURL = "https://www.hebcal.com/hebcal?v=1&cfg=json&maj=on&min=on&mod=on"+
                "&year="+year+
                "&month="+month
                ;
        ResponseEntity<String> response = restTemplate.getForEntity(calendarURL, String.class);
        Map<LocalDate, String> holidays = processAPIResponse(response.getBody());

        return holidays;
    }

    private Map<LocalDate, String> processAPIResponse(String jsonResponseBody)
    {
        Map<LocalDate, String> result = new HashMap<>();
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode allItems = objectMapper.readTree(jsonResponseBody)
                    .path("items");
            for(JsonNode item : allItems)
            {
                LocalDate holidayDate = LocalDate.parse(item.get ("date").asText());
                String holidayName = item.get("hebrew").asText();
                result.put(holidayDate, holidayName);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

}
