package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.web;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.PatientDto;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service.PatientService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String patient(Model model) {
        model.addAttribute("allPatients", patientService.readAll());
        return "patients";
    }

    @GetMapping("/edit")
    public String editPatient(@RequestParam Long id, Model model) {
        patientService.setActivePatient(id);
        model.addAttribute("patient", patientService.readOne().orElseThrow());
        return "patientsEdit";
    }

    @PostMapping("/edit")
    public String submitEditPatient(@ModelAttribute PatientDto patient, Model model) {
        patientService.setActivePatient(patient.getId());
        try {
            patientService.update(patient);
        } catch (BadRequestException e) {
            model.addAttribute("error", true);
            model.addAttribute("errorMsg", e.getMessage());
        }
        model.addAttribute("patient", patient);
        return "patientsEdit";
    }

    @GetMapping("/create")
    public String createPatient(Model model) {
        model.addAttribute("patient", new PatientDto());
        return "patientsCreate";
    }

    @PostMapping("/create")
    public String submitCreatePatient(@ModelAttribute PatientDto patient, Model model) {
        patientService.create(patient);
        return createPatient(model);
    }

}
