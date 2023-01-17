package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.service;

import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.client.DoctorClient;
import fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto.DoctorDto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class DoctorService {
    private DoctorClient doctorClient;
    private boolean isDoctorActive = false;

    public DoctorService(DoctorClient doctorClient) {this.doctorClient = doctorClient; }

    public void create(DoctorDto doctor) { doctorClient.create(doctor); }

    public void setActiveDoctor(Long id) {
        doctorClient.setActiveDoctor(id);
        isDoctorActive = true;
    }

    public boolean idDoctorActive() { return isDoctorActive; }

    public Optional<DoctorDto> readOne() { return doctorClient.readOne(); }

    public Collection<DoctorDto> readAll() { return doctorClient.readAll(); }

    public void update(DoctorDto doctor) { doctorClient.updateOne(doctor); }
}
