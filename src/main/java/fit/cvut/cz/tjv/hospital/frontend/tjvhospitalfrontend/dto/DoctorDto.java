package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto;


import java.util.Collection;

// in collections we don't use Objects, but ID-s
public class DoctorDto {

    private Long id;
    private String name;
    private String emailAddress;
    private String phoneNumber;

    private Collection<InnerPatientDto> patients;

    private Collection<InnerAppointmentForDoctorDto> appointments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Collection<InnerPatientDto> getPatients() {
        return patients;
    }

    public void setPatients(Collection<InnerPatientDto> patients) {
        this.patients = patients;
    }

    public Collection<InnerAppointmentForDoctorDto> getAppointments() {
        return appointments;
    }

    public void setAppointments(Collection<InnerAppointmentForDoctorDto> appointments) {
        this.appointments = appointments;
    }
}
