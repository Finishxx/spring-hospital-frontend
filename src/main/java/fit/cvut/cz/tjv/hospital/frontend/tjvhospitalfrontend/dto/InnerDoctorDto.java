package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto;


public class InnerDoctorDto {
    public Long doctor_id;
    public String doctor_name;

    public Long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }
}