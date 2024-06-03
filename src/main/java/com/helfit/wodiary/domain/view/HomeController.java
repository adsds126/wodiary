package com.helfit.wodiary.domain.view;
import com.helfit.wodiary.domain.wsession.dto.WsessionDto;
import com.helfit.wodiary.domain.wsession.service.WsessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final WsessionService wsessionService;
    private final RestTemplate restTemplate;

    @GetMapping("/home")
    public String home(@RequestParam(value = "year", required = false) Integer year,
                       @RequestParam(value = "month", required = false) Integer month,
                       Model model) {
        LocalDate now = LocalDate.now();
        int currentYear = (year == null) ? now.getYear() : year;
        int currentMonth = (month == null) ? now.getMonthValue() : month;

        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday=0, Monday=1, ..., Saturday=6

        List<Integer> emptyDays = new ArrayList<>();
        for (int i = 0; i < dayOfWeekValue; i++) {
            emptyDays.add(i);
        }

        List<String> days = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.format("%02d", i));
        }

        String paddedMonth = String.format("%02d", currentMonth);

        List<LocalDate> sessionDates = wsessionService.getSessionDates(currentYear, currentMonth);
        List<String> sessionDays = (sessionDates != null) ? sessionDates.stream()
                .map(date -> String.format("%02d", date.getDayOfMonth()))
                .collect(Collectors.toList()) : new ArrayList<>();

        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth); // 패딩 처리하지 않은 숫자
        model.addAttribute("paddedMonth", paddedMonth); // 패딩된 월 문자열
        model.addAttribute("emptyDays", emptyDays);
        model.addAttribute("days", days);
        model.addAttribute("sessionDays", sessionDays);

        return "home";
    }
    @GetMapping("/wsession/{date}")
    public String getSessionView(@PathVariable String date, Model model) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            WsessionDto.Response sessionResponse = wsessionService.getSession(localDate);

            if (sessionResponse == null) {
                model.addAttribute("date", localDate);
                return "addExercise";
            } else {
                model.addAttribute("date", localDate);
                model.addAttribute("wsession", sessionResponse);
                return "viewSession";
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date format");
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while fetching the session");
            return "error";
        }
    }
    @GetMapping("/wsession/{date}/addExercise")
    public String addExercise(@PathVariable String date, Model model) {
        model.addAttribute("date", date);
        return "addExercise";
    }

}
