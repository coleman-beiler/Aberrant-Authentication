package API.persistance.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GROUP_NAMES")
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="group_id")
    private Integer id;
    @Column(name="group_name")
    private String groupName;

    public Group(Integer id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public Group() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
