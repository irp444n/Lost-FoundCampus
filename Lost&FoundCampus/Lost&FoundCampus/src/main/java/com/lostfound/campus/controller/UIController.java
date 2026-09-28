package com.lostfound.campus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lostfound.campus.service.RegistrationService;

@Controller
public class UIController {

    private final RegistrationService registrationService;

    public UIController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/lapor")
    public String lapor() {
        return "lapor";
    }

    @GetMapping("/preview-aksi")
    public String previewItemAction(@RequestParam(defaultValue = "claim") String mode,
                                    @RequestParam(defaultValue = "-") String id,
                                    @RequestParam(defaultValue = "Barang") String title,
                                    @RequestParam(defaultValue = "Umum") String category,
                                    @RequestParam(defaultValue = "-") String location,
                                    @RequestParam(defaultValue = "-") String time,
                                    @RequestParam(defaultValue = "-") String status,
                                    @RequestParam(defaultValue = "-") String reporter,
                                    @RequestParam(defaultValue = "Tidak ada deskripsi tambahan.") String description,
                                    Model model) {
        if (!mode.equals("found") && !mode.equals("claim")) {
            return "redirect:/";
        }
        model.addAttribute("mode", mode);
        model.addAttribute("itemId", id);
        model.addAttribute("itemTitle", title);
        model.addAttribute("itemCategory", category);
        model.addAttribute("itemLocation", location);
        model.addAttribute("itemTime", time);
        model.addAttribute("itemStatus", status);
        model.addAttribute("itemReporter", reporter);
        model.addAttribute("itemDescription", description);
        return "preview-aksi";
    }

    @GetMapping("/chat-demo")
    public String chatDemo(@RequestParam(defaultValue = "claim") String mode,
                           @RequestParam(defaultValue = "-") String itemId,
                           @RequestParam(defaultValue = "Barang") String itemTitle,
                           @RequestParam(defaultValue = "-") String itemLocation,
                           Model model) {
        if (!mode.equals("found") && !mode.equals("claim")) {
            return "redirect:/";
        }
        model.addAttribute("mode", mode);
        model.addAttribute("itemId", itemId);
        model.addAttribute("itemTitle", itemTitle);
        model.addAttribute("itemLocation", itemLocation);
        return "chat-demo";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String submitRegistration(@RequestParam String fullName,
                                     @RequestParam String email,
                                     @RequestParam String whatsapp,
                                     @RequestParam String reporterType,
                                     @RequestParam String studentNumber,
                                     @RequestParam String unit,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        String normalizedWhatsapp = whatsapp.replaceAll("[\\s()-]", "");
        if (normalizedWhatsapp.startsWith("08")) {
            normalizedWhatsapp = "62" + normalizedWhatsapp.substring(1);
        } else if (normalizedWhatsapp.startsWith("+62")) {
            normalizedWhatsapp = normalizedWhatsapp.substring(1);
        }

        String error = validateRegistration(fullName, email, normalizedWhatsapp, reporterType,
                studentNumber, unit, password, confirmPassword);
        if (error != null) {
            populateRegistrationForm(model, fullName, email, whatsapp, reporterType, studentNumber, unit);
            model.addAttribute("errorMessage", error);
            return "register";
        }

        try {
            registrationService.register(fullName, email, normalizedWhatsapp, reporterType,
                    studentNumber, unit, password);
        } catch (IllegalArgumentException | DataIntegrityViolationException exception) {
            populateRegistrationForm(model, fullName, email, whatsapp, reporterType, studentNumber, unit);
            model.addAttribute("errorMessage", exception.getMessage() == null
                    ? "Email atau nomor induk sudah terdaftar."
                    : exception.getMessage());
            return "register";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Akun berhasil dibuat. Silakan masuk.");
        return "redirect:/login";
    }

    private String validateRegistration(String fullName, String email, String whatsapp, String reporterType,
                                        String studentNumber, String unit, String password, String confirmPassword) {
        if (fullName.isBlank() || email.isBlank() || whatsapp.isBlank() || reporterType.isBlank()
                || studentNumber.isBlank() || unit.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return "Semua data wajib diisi.";
        }
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return "Format email tidak valid.";
        }
        if (!whatsapp.matches("62[0-9]{8,13}")) {
            return "Nomor WhatsApp harus berformat 08xx atau +628xx.";
        }
        if (!reporterType.matches("mahasiswa|dosen|tendik")) {
            return "Pilih tipe pelapor yang valid.";
        }
        if (password.length() < 8 || !password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            return "Kata sandi minimal 8 karakter dan harus memuat huruf serta angka.";
        }
        if (!password.equals(confirmPassword)) {
            return "Konfirmasi kata sandi tidak cocok.";
        }
        return null;
    }

    private void populateRegistrationForm(Model model, String fullName, String email, String whatsapp,
                                          String reporterType, String studentNumber, String unit) {
        model.addAttribute("fullName", fullName);
        model.addAttribute("email", email);
        model.addAttribute("whatsapp", whatsapp);
        model.addAttribute("reporterType", reporterType);
        model.addAttribute("studentNumber", studentNumber);
        model.addAttribute("unit", unit);
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email atau kata sandi tidak valid. Silakan coba lagi.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Anda berhasil keluar dari sistem.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "password", required = false) String password,
                              Model model) {
        if (email != null && !email.isBlank() && password != null && !password.isBlank()) {
            return "redirect:/?loginSuccess=true";
        }
        model.addAttribute("errorMessage", "Email dan kata sandi wajib diisi!");
        return "login";
    }
}
