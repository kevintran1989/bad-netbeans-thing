/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleapp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "DEPARTMENT", catalog = "", schema = "A4")
@NamedQueries({
    @NamedQuery(name = "Department.findAll", query = "SELECT d FROM Department d"),
    @NamedQuery(name = "Department.findByDname", query = "SELECT d FROM Department d WHERE d.dname = :dname"),
    @NamedQuery(name = "Department.findByDnumber", query = "SELECT d FROM Department d WHERE d.dnumber = :dnumber"),
    @NamedQuery(name = "Department.findByMgrSsn", query = "SELECT d FROM Department d WHERE d.mgrSsn = :mgrSsn"),
    @NamedQuery(name = "Department.findByMgrStartDate", query = "SELECT d FROM Department d WHERE d.mgrStartDate = :mgrStartDate"),
    @NamedQuery(name = "Department.findByUserid", query = "SELECT d FROM Department d WHERE d.userid = :userid")})
public class Department implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "DNAME")
    private String dname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "DNUMBER")
    private BigDecimal dnumber;
    @Basic(optional = false)
    @Column(name = "MGR_SSN")
    private String mgrSsn;
    @Column(name = "MGR_START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mgrStartDate;
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userid;
        @OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
    private List<DeptLocations> deptLocationsList;

    public Department() {
    }

    public Department(BigDecimal dnumber) {
        this.dnumber = dnumber;
    }

    public Department(BigDecimal dnumber, String dname, String mgrSsn, String userid) {
        this.dnumber = dnumber;
        this.dname = dname;
        this.mgrSsn = mgrSsn;
        this.userid = userid;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        String oldDname = this.dname;
        this.dname = dname;
        changeSupport.firePropertyChange("dname", oldDname, dname);
    }

    public BigDecimal getDnumber() {
        return dnumber;
    }

    public void setDnumber(BigDecimal dnumber) {
        BigDecimal oldDnumber = this.dnumber;
        this.dnumber = dnumber;
        changeSupport.firePropertyChange("dnumber", oldDnumber, dnumber);
    }

    public String getMgrSsn() {
        return mgrSsn;
    }

    public void setMgrSsn(String mgrSsn) {
        String oldMgrSsn = this.mgrSsn;
        this.mgrSsn = mgrSsn;
        changeSupport.firePropertyChange("mgrSsn", oldMgrSsn, mgrSsn);
    }

    public Date getMgrStartDate() {
        return mgrStartDate;
    }

    public void setMgrStartDate(Date mgrStartDate) {
        Date oldMgrStartDate = this.mgrStartDate;
        this.mgrStartDate = mgrStartDate;
        changeSupport.firePropertyChange("mgrStartDate", oldMgrStartDate, mgrStartDate);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        String oldUserid = this.userid;
        this.userid = userid;
        changeSupport.firePropertyChange("userid", oldUserid, userid);
    }
    
        public List<DeptLocations> getDeptLocationsList() {
        return deptLocationsList;
    }

    public void setDeptLocationsList(List<DeptLocations> deptLocationsList) {
        this.deptLocationsList = deptLocationsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dnumber != null ? dnumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Department)) {
            return false;
        }
        Department other = (Department) object;
        if ((this.dnumber == null && other.dnumber != null) || (this.dnumber != null && !this.dnumber.equals(other.dnumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracleapp.Department[ dnumber=" + dnumber + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
