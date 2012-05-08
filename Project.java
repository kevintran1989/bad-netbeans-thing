package oracleapp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Nnnu
 */
@Entity
@Table(name = "PROJECT", catalog = "", schema = "A4")
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
    @NamedQuery(name = "Project.findByPname", query = "SELECT p FROM Project p WHERE p.pname = :pname"),
    @NamedQuery(name = "Project.findByPnumber", query = "SELECT p FROM Project p WHERE p.pnumber = :pnumber"),
    @NamedQuery(name = "Project.findByPlocation", query = "SELECT p FROM Project p WHERE p.plocation = :plocation"),
    @NamedQuery(name = "Project.findByDnum", query = "SELECT p FROM Project p WHERE p.dnum = :dnum"),
    @NamedQuery(name = "Project.findByUserid", query = "SELECT p FROM Project p WHERE p.userid = :userid")})
public class Project implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "PNAME")
    private String pname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PNUMBER")
    private BigDecimal pnumber;
    @Column(name = "PLOCATION")
    private String plocation;
    @Basic(optional = false)
    @Column(name = "DNUM")
    private BigInteger dnum;
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userid;

    public Project() {
    }

    public Project(BigDecimal pnumber) {
        this.pnumber = pnumber;
    }

    public Project(BigDecimal pnumber, String pname, BigInteger dnum, String userid) {
        this.pnumber = pnumber;
        this.pname = pname;
        this.dnum = dnum;
        this.userid = userid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        String oldPname = this.pname;
        this.pname = pname;
        changeSupport.firePropertyChange("pname", oldPname, pname);
    }

    public BigDecimal getPnumber() {
        return pnumber;
    }

    public void setPnumber(BigDecimal pnumber) {
        BigDecimal oldPnumber = this.pnumber;
        this.pnumber = pnumber;
        changeSupport.firePropertyChange("pnumber", oldPnumber, pnumber);
    }

    public String getPlocation() {
        return plocation;
    }

    public void setPlocation(String plocation) {
        String oldPlocation = this.plocation;
        this.plocation = plocation;
        changeSupport.firePropertyChange("plocation", oldPlocation, plocation);
    }

    public BigInteger getDnum() {
        return dnum;
    }

    public void setDnum(BigInteger dnum) {
        BigInteger oldDnum = this.dnum;
        this.dnum = dnum;
        changeSupport.firePropertyChange("dnum", oldDnum, dnum);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        String oldUserid = this.userid;
        this.userid = userid;
        changeSupport.firePropertyChange("userid", oldUserid, userid);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pnumber != null ? pnumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.pnumber == null && other.pnumber != null) || (this.pnumber != null && !this.pnumber.equals(other.pnumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracleapp.Project[ pnumber=" + pnumber + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
