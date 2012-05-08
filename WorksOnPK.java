/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracleapp;

import java.io.Serializable;
import java.math.BigInteger;

public class WorksOnPK implements Serializable {
    private String essn;
    private BigInteger pno;

    public WorksOnPK() {
    }

    public WorksOnPK(String essn, BigInteger pno) {
        this.essn = essn;
        this.pno = pno;
    }

    public String getEssn() {
        return essn;
    }

    public BigInteger getPno() {
        return pno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (essn != null ? essn.hashCode() : 0);
        hash += (pno != null ? pno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorksOnPK)) {
            return false;
        }
        WorksOnPK other = (WorksOnPK) object;
        if ((this.essn == null && other.essn != null) || (this.essn != null && !this.essn.equals(other.essn))) {
            return false;
        }
        if ((this.pno == null && other.pno != null) || (this.pno != null && !this.pno.equals(other.pno))) {
            return false;
        }
        return true;
    }
    
}
