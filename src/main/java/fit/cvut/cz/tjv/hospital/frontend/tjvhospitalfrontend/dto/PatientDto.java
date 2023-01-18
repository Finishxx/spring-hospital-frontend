package fit.cvut.cz.tjv.hospital.frontend.tjvhospitalfrontend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

// in collections we don't use Objects, but ID-s
public class PatientDto {

    private Long id;
    private String name;
    // https://www.baeldung.com/spring-boot-formatting-json-dates
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthdate;
    private String emailAddress;
    private String phoneNumber;

    private Collection<InnerDoctorDto> doctors;

    private Collection<InnerAppointmentForPatientDto> appointments;

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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
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

    public Collection<InnerDoctorDto> getDoctors() {
        return doctors;
    }

    public void setDoctors(Collection<InnerDoctorDto> doctors) {
        this.doctors = doctors;
    }

    public Collection<InnerAppointmentForPatientDto> getAppointments() {
        return appointments;
    }

    public void setAppointments(Collection<InnerAppointmentForPatientDto> appointments) {
        this.appointments = appointments;
    }
}
