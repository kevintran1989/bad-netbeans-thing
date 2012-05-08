/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleapp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Nnnu
 */
@Entity
@Table(name = "DEPENDENT", catalog = "", schema = "A4")
@IdClass(DependentPK.class)
@NamedQueries({
    @NamedQuery(name = "Dependent.findAll", query = "SELECT d FROM Dependent d"),
    @NamedQuery(name = "Dependent.findByEssn", query = "SELECT d FROM Dependent d WHERE d.essn = :essn"),
    @NamedQuery(name = "Dependent.findByDependentName", query = "SELECT d FROM Dependent d WHERE d.dependentName = :dependentName"),
    @NamedQuery(name = "Dependent.findBySex", query = "SELECT d FROM Dependent d WHERE d.sex = :sex"),
    @NamedQuery(name = "Dependent.findByBdate", query = "SELECT d FROM Dependent d WHERE d.bdate = :bdate"),
    @NamedQuery(name = "Dependent.findByRelationship", query = "SELECT d FROM Dependent d WHERE d.relationship = :relationship"),
    @NamedQuery(name = "Dependent.findByUserid", query = "SELECT d FROM Dependent d WHERE d.userid = :userid")})
public class Dependent implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    //@EmbeddedId
    //protected DependentPK dependentPK;
    @Id
    @Basic(optional = false)
    @Column(name = "ESSN")
    private String essn;
    @Id
    @Basic(optional = false)
    @Column(name = "DEPENDENT_NAME")
    private String dependentName;
    @Column(name = "SEX")
    private Character sex;
    @Column(name = "BDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bdate;
    @Column(name = "RELATIONSHIP")
    private String relationship;
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userid;

    public Dependent() {
    }

    /*public Dependent(DependentPK dependentPK) {
        this.dependentPK = dependentPK;
    }*/
    
    public Dependent(String essn, String dependentName) {
        this.essn = essn;
        this.dependentName = dependentName;
    }

    public Dependent(String essn, String dependentName, String userid) {
        this.essn = essn;
        this.dependentName = dependentName;
        this.userid = userid;
    }

    /*public Dependent(String essn, String dependentName) {
        this.dependentPK = new DependentPK(essn, dependentName);
    }*/

    /*public DependentPK getDependentPK() {
        return dependentPK;
    }

    public void setDependentPK(DependentPK dependentPK) {
        this.dependentPK = dependentPK;
    }*/

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        Character oldSex = this.sex;
        this.sex = sex;
        changeSupport.firePropertyChange("sex", oldSex, sex);
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        Date oldBdate = this.bdate;
        this.bdate = bdate;
        changeSupport.firePropertyChange("bdate", oldBdate, bdate);
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        String oldRelationship = this.relationship;
        this.relationship = relationship;
        changeSupport.firePropertyChange("relationship", oldRelationship, relationship);
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
        this.essn = essn;
    }

    public String getDependentName() {
        return dependentName;
    }

    public void setDependentName(String dependentName) {
        this.dependentName = dependentName;
    }
    
    /*@Override
    public int hashCode() {
        int hash = 0;
        hash += (dependentPK != null ? dependentPK.hashCode() : 0);
        return hash;
    }*/
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (essn != null ? essn.hashCode() : 0);
        hash += (dependentName != null ? dependentName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dependent)) {
            return false;
        }
        Dependent other = (Dependent) object;
        /*if ((this.dependentPK == null && other.dependentPK != null) || (this.dependentPK != null && !this.dependentPK.equals(other.dependentPK))) {
            return false;
        }*/
        if ((this.essn == null && other.essn != null) || (this.essn != null && !this.essn.equals(other.essn))) {
            return false;
        }
        if ((this.dependentName == null && other.dependentName != null) || (this.dependentName != null && !this.dependentName.equals(other.dependentName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracleapp.DependentPK[ essn=" + essn + ", dependentName=" + dependentName + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
