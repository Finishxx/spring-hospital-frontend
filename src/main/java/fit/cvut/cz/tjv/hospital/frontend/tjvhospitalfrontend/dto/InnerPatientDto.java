package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto;

public class InnerPatientDto {
    private Long patient_id;
    private String patient_name;

    public Long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Long patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }
}