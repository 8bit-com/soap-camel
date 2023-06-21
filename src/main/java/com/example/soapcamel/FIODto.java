package com.example.soapcamel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;


@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"lastName", "firstName", "secondName"}
)
@XmlRootElement(
        name = "Period"
)
public class FIODto {
    @XmlElement(
            name = "LastName"
    )
    private String lastName;
    @XmlElement(
            name = "FirstName"
    )
    private String firstName;

    @XmlElement(
            name = "SecondName"
    )
    private String secondName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
