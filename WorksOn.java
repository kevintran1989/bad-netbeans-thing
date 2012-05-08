/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleapp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@Table(name = "WORKS_ON", catalog = "", schema = "A4")
@IdClass(WorksOnPK.class)
@NamedQueries({
    @NamedQuery(name = "WorksOn.findAll", query = "SELECT w FROM WorksOn w"),
    @NamedQuery(name = "WorksOn.findByEssn", query = "SELECT w FROM WorksOn w WHERE w.essn = :essn"),
    @NamedQuery(name = "WorksOn.findByPno", query = "SELECT w FROM WorksOn w WHERE w.pno = :pno"),
    @NamedQuery(name = "WorksOn.findByHours", query = "SELECT w FROM WorksOn w WHERE w.hours = :hours"),
    @NamedQuery(name = "WorksOn.findByUserid", query = "SELECT w FROM WorksOn w WHERE w.userid = :userid")})
public class WorksOn implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    //@EmbeddedId
    //private WorksOnPK worksOnPK;
    @Id
    @Basic(optional = false)
    @Column(name = "ESSN")
    private String essn;
    @Id
    @Basic(optional = false)
    @Column(name = "PNO")
    private BigInteger pno;
    @Basic(optional = false)
    @Column(name = "HOURS")
    private BigDecimal hours;
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userid;

    public WorksOn() {
    }

    public WorksOn(String essn, BigInteger pno) {
        this.essn = essn;
        this.pno = pno;
    }

    public WorksOn(/*WorksOnPK worksOnPK, */String essn, BigInteger pno, BigDecimal hours, String userid) {
        //this.worksOnPK = worksOnPK;
        this.essn = essn;
        this.pno = pno;
        this.hours = hours;
        this.userid = userid;
    }

    /*public WorksOn(String essn, BigInteger pno) {
        this.worksOnPK = new WorksOnPK(essn, pno);
    }*/

    /*public WorksOnPK getWorksOnPK() {
        return worksOnPK;
    }

    public void setWorksOnPK(WorksOnPK worksOnPK) {
        this.worksOnPK = worksOnPK;
    }*/

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        BigDecimal oldHours = this.hours;
        this.hours = hours;
        changeSupport.firePropertyChange("hours", oldHours, hours);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        String oldUserid = this.userid;
        this.userid = userid;
        changeSupport.firePropertyChange("userid", oldUserid, userid);
    }
    public String getEssn() {
        return essn;
    }

    public void setEssn(String essn) {
        String oldEssn = this.essn;
        this.essn = essn;
        changeSupport.firePropertyChange("essn", oldEssn, essn);
    }

    public BigInteger getPno() {
        return pno;
    }

    public void setPno(BigInteger pno) {
        this.pno = pno;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (essn != null ? essn.hashCode() : 0);
        hash += (pno != null ? pno.hashCode() : 0);
        return hash;
    }

    /*@Override
    public int hashCode() {
        int hash = 0;
        hash += (worksOnPK != null ? worksOnPK.hashCode() : 0);
        return hash;
    }*/

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorksOn)) {
            return false;
        }
        WorksOn other = (WorksOn) object;
        //if ((this.worksOnPK == null && other.worksOnPK != null) || (this.worksOnPK != null && !this.worksOnPK.equals(other.worksOnPK))) {
        //    return false;
        //}
        if ((this.essn == null && other.essn != null) || (this.essn != null && !this.essn.equals(other.essn))) {
            return false;
        }
        if ((this.pno == null && other.pno != null) || (this.pno != null && !this.pno.equals(other.pno))) {
            return false;
        }
        return true;
    }

    //@Override
    //public String toString() {
    //    return "oracleapp.WorksOn[ worksOnPK=" + worksOnPK + " ]";
    //}
     @Override
    public String toString() {
        return "oracleapp.WorksOnPK[ essn=" + essn + ", pno=" + pno + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
