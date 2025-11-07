@Table(name="student")
public class StudentDTO
{
@PrimaryKey
@Column(name="roll_number")
private int rollNumber;
@Column(name="first_name")
private String firstName;
@Column(name="last_name")
private String lastName;
@Column(name="aadhar_card_number")
private String aadharCardNumber;
@Column(name="course_code")
private int courseCode;
@Column(name="gender")
private String gender;
@Column(name="date_of_birth")
private java.util.Date dateOfBirth;
public StudentDTO() {}
public StudentDTO(int rollNumber, String firstName, String lastName, String aadharCardNumber, int courseCode, String gender, java.util.Date dateOfBirth)
{
this.rollNumber=rollNumber;
this.firstName=firstName;
this.lastName=lastName;
this.aadharCardNumber=aadharCardNumber;
this.courseCode=courseCode;
this.gender=gender;
this.dateOfBirth=dateOfBirth;
}
public void setRollNumber(int rollNumber)
{
this.rollNumber=rollNumber;
}
public int getRollNumber()
{
return this.rollNumber;
}
public void setFirstName(String firstName)
{
this.firstName=firstName;
}
public String getFirstName()
{
return this.firstName;
}
public void setLastName(String lastName)
{
this.lastName=lastName;
}
public String getLastName()
{
return this.lastName;
}
public void setAadharCardNumber(String aadharCardNumber)
{
this.aadharCardNumber=aadharCardNumber;
}
public String getAadharCardNumber()
{
return this.aadharCardNumber;
}
public void setCourseCode(int courseCode)
{
this.courseCode=courseCode;
}
public int getCourseCode()
{
return this.courseCode;
}
public void setGender(String gender)
{
this.gender=gender;
}
public String getGender()
{
return this.gender;
}
public void setDateOfBirth(java.util.Date dateOfBirth)
{
this.dateOfBirth=dateOfBirth;
}
public java.util.Date getDateOfBirth()
{
return this.dateOfBirth;
}
}
