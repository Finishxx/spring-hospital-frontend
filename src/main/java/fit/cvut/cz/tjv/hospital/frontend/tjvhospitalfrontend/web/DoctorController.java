package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.DoctorDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.DoctorService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService) { this.doctorService = doctorService; }

    @GetMapping
    public String doctor(Model model) {
        model.addAttribute("allDoctors", doctorService.readAll());
        return "doctor";
    }

    @GetMapping("/edit")
    public String editDoctor(@RequestParam Long id, Model model) {
        doctorService.setActiveDoctor(id);
        model.addAttribute("doctor", doctorService.readOne().orElseThrow());
        return "doctor-edit";
    }

    @PostMapping("/edit")
    public String submitEditDoctor(@ModelAttribute DoctorDto doctor, Model model) {
        doctorService.setActiveDoctor(doctor.getId());
        try {
            doctorService.update(doctor);
        } catch (BadRequestException e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
        }
        model.addAttribute("doctor", doctor);
        return "doctorEdit";
    }
}
