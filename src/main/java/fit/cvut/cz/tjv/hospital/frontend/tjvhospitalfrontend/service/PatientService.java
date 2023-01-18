package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.client.PatientClient;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.PatientDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PatientService {
    private PatientClient patientClient;
    private boolean isPatientActive = false;

    public PatientService(PatientClient patientClient) { this.patientClient = patientClient; }

    void create(PatientDto patient) { patientClient.create(patient); }

    public void setActivePatient(Long id) {
        patientClient.setActivePatient(id);
        isPatientActive = true;
    }

    public boolean idPatientActive() { return isPatientActive; }

    public Optional<PatientDto> readOne() { return patientClient.readOne(); }

    public Collection<PatientDto> readAll() { return patientClient.readAll(); }

    public void update(PatientDto patient) { patientClient.updateOne(patient); }

}
