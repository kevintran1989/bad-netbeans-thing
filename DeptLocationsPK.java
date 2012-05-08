/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleapp;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Nnnu
 */
@Embeddable
public class DeptLocationsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "DNUMBER")
    private BigInteger dnumber;
    @Basic(optional = false)
    @Column(name = "DLOCATION")
    private String dlocation;

    public DeptLocationsPK() {
    }

    public DeptLocationsPK(BigInteger dnumber, String dlocation) {
        this.dnumber = dnumber;
        this.dlocation = dlocation;
    }

    public BigInteger getDnumber() {
        return dnumber;
    }

    public void setDnumber(BigInteger dnumber) {
        this.dnumber = dnumber;
    }

    public String getDlocation() {
        return dlocation;
    }

    public void setDlocation(String dlocation) {
        this.dlocation = dlocation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dnumber != null ? dnumber.hashCode() : 0);
        hash += (dlocation != null ? dlocation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DeptLocationsPK)) {
            return false;
        }
        DeptLocationsPK other = (DeptLocationsPK) object;
        if ((this.dnumber == null && other.dnumber != null) || (this.dnumber != null && !this.dnumber.equals(other.dnumber))) {
            return false;
        }
        if ((this.dlocation == null && other.dlocation != null) || (this.dlocation != null && !this.dlocation.equals(other.dlocation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracleapp.DeptLocationsPK[ dnumber=" + dnumber + ", dlocation=" + dlocation + " ]";
    }
    
}
