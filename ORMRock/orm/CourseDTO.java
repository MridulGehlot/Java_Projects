@Table(name="course")
public class CourseDTO
{
@PrimaryKey
@AutoIncrement
@Column(name="id")
private int id;
@Column(name="title")
private String title;
public CourseDTO() {}
public CourseDTO(int id, String title)
{
this.id=id;
this.title=title;
}
public void setId(int id)
{
this.id=id;
}
public int getId()
{
return this.id;
}
public void setTitle(String title)
{
this.title=title;
}
public String getTitle()
{
return this.title;
}
}
