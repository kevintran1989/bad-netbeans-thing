/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleapp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Nnnu
 */
@Entity
@Table(name = "EMPLOYEE", catalog = "", schema = "A4")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByFname", query = "SELECT e FROM Employee e WHERE e.fname = :fname"),
    @NamedQuery(name = "Employee.findByMinit", query = "SELECT e FROM Employee e WHERE e.minit = :minit"),
    @NamedQuery(name = "Employee.findByLname", query = "SELECT e FROM Employee e WHERE e.lname = :lname"),
    @NamedQuery(name = "Employee.findBySsn", query = "SELECT e FROM Employee e WHERE e.ssn = :ssn"),
    @NamedQuery(name = "Employee.findByBdate", query = "SELECT e FROM Employee e WHERE e.bdate = :bdate"),
    @NamedQuery(name = "Employee.findByAddress", query = "SELECT e FROM Employee e WHERE e.address = :address"),
    @NamedQuery(name = "Employee.findBySex", query = "SELECT e FROM Employee e WHERE e.sex = :sex"),
    @NamedQuery(name = "Employee.findBySalary", query = "SELECT e FROM Employee e WHERE e.salary = :salary"),
    @NamedQuery(name = "Employee.findByDno", query = "SELECT e FROM Employee e WHERE e.dno = :dno"),
    @NamedQuery(name = "Employee.findByUserid", query = "SELECT e FROM Employee e WHERE e.userid = :userid")})
public class Employee implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "FNAME")
    private String fname;
    @Column(name = "MINIT")
    private Character minit;
    @Basic(optional = false)
    @Column(name = "LNAME")
    private String lname;
    @Id
    @Basic(optional = false)
    @Column(name = "SSN")
    private String ssn;
    @Column(name = "BDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bdate;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "SEX")
    private Character sex;
    @Column(name = "SALARY")
    private BigInteger salary;
    @Basic(optional = false)
    @Column(name = "DNO")
    private BigInteger dno;
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userid;
    @OneToMany(mappedBy = "superSsn")
    private List<Employee> employeeList;
    @JoinColumn(name = "SUPER_SSN", referencedColumnName = "SSN")
    @ManyToOne
    private Employee superSsn;

    public Employee() {
    }

    public Employee(String ssn) {
        this.ssn = ssn;
    }

    public Employee(String ssn, String fname, String lname, BigInteger dno, String userid) {
        this.ssn = ssn;
        this.fname = fname;
        this.lname = lname;
        this.dno = dno;
        this.userid = userid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        String oldFname = this.fname;
        this.fname = fname;
        changeSupport.firePropertyChange("fname", oldFname, fname);
    }

    public Character getMinit() {
        return minit;
    }

    public void setMinit(Character minit) {
        Character oldMinit = this.minit;
        this.minit = minit;
        changeSupport.firePropertyChange("minit", oldMinit, minit);
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        String oldLname = this.lname;
        this.lname = lname;
        changeSupport.firePropertyChange("lname", oldLname, lname);
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        String oldSsn = this.ssn;
        this.ssn = ssn;
        changeSupport.firePropertyChange("ssn", oldSsn, ssn);
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        Date oldBdate = this.bdate;
        this.bdate = bdate;
        changeSupport.firePropertyChange("bdate", oldBdate, bdate);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        String oldAddress = this.address;
        this.address = address;
        changeSupport.firePropertyChange("address", oldAddress, address);
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        Character oldSex = this.sex;
        this.sex = sex;
        changeSupport.firePropertyChange("sex", oldSex, sex);
    }

    public BigInteger getSalary() {
        return salary;
    }

    public void setSalary(BigInteger salary) {
        BigInteger oldSalary = this.salary;
        this.salary = salary;
        changeSupport.firePropertyChange("salary", oldSalary, salary);
    }

    public BigInteger getDno() {
        return dno;
    }

    public void setDno(BigInteger dno) {
        BigInteger oldDno = this.dno;
        this.dno = dno;
        changeSupport.firePropertyChange("dno", oldDno, dno);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        String oldUserid = this.userid;
        this.userid = userid;
        changeSupport.firePropertyChange("userid", oldUserid, userid);
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Employee getSuperSsn() {
        return superSsn;
    }

    public void setSuperSsn(Employee superSsn) {
        Employee oldSuperSsn = this.superSsn;
        this.superSsn = superSsn;
        changeSupport.firePropertyChange("superSsn", oldSuperSsn, superSsn);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ssn != null ? ssn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.ssn == null && other.ssn != null) || (this.ssn != null && !this.ssn.equals(other.ssn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracleapp.Employee[ ssn=" + ssn + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
