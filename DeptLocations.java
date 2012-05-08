/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleapp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Nnnu
 */
@Entity
@Table(name = "DEPT_LOCATIONS", catalog = "", schema = "A4")
@NamedQueries({
    @NamedQuery(name = "DeptLocations.findAll", query = "SELECT d FROM DeptLocations d"),
    @NamedQuery(name = "DeptLocations.findByDnumber", query = "SELECT d FROM DeptLocations d WHERE d.deptLocationsPK.dnumber = :dnumber"),
    @NamedQuery(name = "DeptLocations.findByDlocation", query = "SELECT d FROM DeptLocations d WHERE d.deptLocationsPK.dlocation = :dlocation")})
public class DeptLocations implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DeptLocationsPK deptLocationsPK;
    @JoinColumn(name = "DNUMBER", referencedColumnName = "DNUMBER", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Department department;

    public DeptLocations() {
    }

    public DeptLocations(DeptLocationsPK deptLocationsPK) {
        this.deptLocationsPK = deptLocationsPK;
    }

    public DeptLocations(BigInteger dnumber, String dlocation) {
        this.deptLocationsPK = new DeptLocationsPK(dnumber, dlocation);
    }

    public DeptLocationsPK getDeptLocationsPK() {
        return deptLocationsPK;
    }

    public void setDeptLocationsPK(DeptLocationsPK deptLocationsPK) {
        this.deptLocationsPK = deptLocationsPK;
    }
    
        public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        Department oldDepartment = this.department;
        this.department = department;
        changeSupport.firePropertyChange("department", oldDepartment, department);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deptLocationsPK != null ? deptLocationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeptLocations)) {
            return false;
        }
        DeptLocations other = (DeptLocations) object;
        if ((this.deptLocationsPK == null && other.deptLocationsPK != null) || (this.deptLocationsPK != null && !this.deptLocationsPK.equals(other.deptLocationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracleapp.DeptLocations[ deptLocationsPK=" + deptLocationsPK + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
