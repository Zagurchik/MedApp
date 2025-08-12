package org.example.medapp.calc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/calc")
public class CalcController {

    // Домашняя страница калькуляторов
    @GetMapping
    public String home() {
        return "redirect:/calc/bmi";
    }

    // --- ИМТ (BMI) ---
    @GetMapping("/bmi")
    public String bmiForm(Model model) {
        model.addAttribute("weightKg", "");
        model.addAttribute("heightCm", "");
        model.addAttribute("bmi", null);
        model.addAttribute("category", null);
        model.addAttribute("resultLine", null);   // ← добавили
        return "calc_bmi";
    }

    @PostMapping("/bmi")
    public String bmiCalc(@RequestParam("weightKg") double weightKg,
                          @RequestParam("heightCm") double heightCm,
                          Model model) {
        double h = heightCm / 100.0;
        double bmiVal = weightKg / (h * h);

        // форматируем красиво: рост/вес без лишних нулей, ИМТ с 1 знаком
        java.text.DecimalFormat oneDec = new java.text.DecimalFormat("#0.0");
        java.text.DecimalFormat upToTwo = new java.text.DecimalFormat("#0.##");

        String heightStr = (Math.floor(heightCm) == heightCm)
                ? String.format("%.0f", heightCm)
                : upToTwo.format(heightCm);
        String weightStr = (Math.floor(weightKg) == weightKg)
                ? String.format("%.0f", weightKg)
                : upToTwo.format(weightKg);
        String bmiStr = oneDec.format(bmiVal);

        String cat;
        if (bmiVal < 18.5) cat = "Недостаточная масса";
        else if (bmiVal < 25) cat = "Норма";
        else if (bmiVal < 30) cat = "Избыточная масса";
        else if (bmiVal < 35) cat = "Ожирение I";
        else if (bmiVal < 40) cat = "Ожирение II";
        else cat = "Ожирение III";

        String line = "Рост = " + heightStr + " см, Вес = " + weightStr + " кг, ИМТ = " + bmiStr + " кг/м²";

        model.addAttribute("weightKg", weightStr);
        model.addAttribute("heightCm", heightStr);
        model.addAttribute("bmi", bmiStr);
        model.addAttribute("category", cat);
        model.addAttribute("resultLine", line);   // ← вот она, готовая строка
        return "calc_bmi";
    }
    // === СКФ + КК (совместно) ===
    @GetMapping("/renal")
    public String renalForm(org.springframework.ui.Model model) {
        model.addAttribute("age", "");
        model.addAttribute("sex", "female");   // female|male
        model.addAttribute("weightKg", "");    // ← вес необязательный
        model.addAttribute("scrValue", "");
        model.addAttribute("scrUnit", "umol"); // mgdl|umol
        model.addAttribute("egfr", null);
        model.addAttribute("crcl", null);
        model.addAttribute("crclNote", null);
        model.addAttribute("ckdStage", null);
        model.addAttribute("ckdStageRange", null);
        return "calc_renal";
    }

    @PostMapping("/renal")
    public String renalCalc(@RequestParam double age,
                            @RequestParam String sex,           // female|male
                            @RequestParam(required = false) String weightKg, // ← строка, может быть пустой
                            @RequestParam("scrValue") double scrValue,
                            @RequestParam("scrUnit") String scrUnit,
                            org.springframework.ui.Model model) {

        // Переводим креатинин в mg/dL при необходимости
        double scrMgDl = scrUnit.equals("umol") ? scrValue / 88.4 : scrValue;

        // CKD-EPI 2021 (мл/мин/1.73 м²)
        double kappa = "female".equals(sex) ? 0.7 : 0.9;
        double alpha = "female".equals(sex) ? -0.241 : -0.302;
        double ratio = scrMgDl / kappa;
        double egfr = 142.0
                * Math.pow(Math.min(ratio, 1.0), alpha)
                * Math.pow(Math.max(ratio, 1.0), -1.200)
                * Math.pow(0.9938, age)
                * ("female".equals(sex) ? 1.012 : 1.0);

        // Стадия ХБП по eGFR
        String stage, range;
        if (egfr >= 90)        { stage = "C1";  range = "≥90"; }
        else if (egfr >= 60)   { stage = "C2";  range = "60–89"; }
        else if (egfr >= 45)   { stage = "C3a"; range = "45–59"; }
        else if (egfr >= 30)   { stage = "C3b"; range = "30–44"; }
        else if (egfr >= 15)   { stage = "C4";  range = "15–29"; }
        else                   { stage = "C5";  range = "<15"; }

        // Cockcroft–Gault (мл/мин) — только если задан вес
        Double crclVal = null;
        String crclNote = null;
        if (weightKg != null && !weightKg.isBlank()) {
            double w;
            try {
                w = Double.parseDouble(weightKg.replace(',', '.'));
                double base = ((140.0 - age) * w) / (72.0 * scrMgDl);
                crclVal = "female".equals(sex) ? base * 0.85 : base;
                model.addAttribute("weightKg", trimNum(w));
            } catch (NumberFormatException e) {
                crclNote = "Некорректный вес";
                model.addAttribute("weightKg", weightKg);
            }
        } else {
            crclNote = "Введите вес для расчёта КК";
            model.addAttribute("weightKg", "");
        }

        // Форматирование
        String egfrStr = oneDec(egfr);
        String crclStr = crclVal == null ? null : oneDec(crclVal);

        model.addAttribute("age", trimNum(age));
        model.addAttribute("sex", sex);
        model.addAttribute("scrValue", trimNum(scrValue));
        model.addAttribute("scrUnit", scrUnit);
        model.addAttribute("egfr", egfrStr);
        model.addAttribute("crcl", crclStr);
        model.addAttribute("crclNote", crclNote);
        model.addAttribute("ckdStage", stage);
        model.addAttribute("ckdStageRange", range);

        return "calc_renal";
    }

    // --- маленькие хелперы для форматирования ---
    private static String oneDec(double v) {
        return new java.text.DecimalFormat("#0.0").format(v);
    }
    private static String trimNum(double v) {
        // без лишних нулей (1 или 2 знака при необходимости)
        return Math.floor(v) == v
                ? String.format("%.0f", v)
                : new java.text.DecimalFormat("#0.##").format(v);
    }

}