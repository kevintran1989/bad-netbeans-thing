/*
 * OracleView.java
 */
package oracleapp;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.RollbackException;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.PropertyStateEvent;
import java.awt.*;
import java.sql.SQLException;

public class OracleView extends FrameView {
    
    public OracleView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // tracking table selection
        masterTable.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    firePropertyChange("recordSelected", !isRecordSelected(), isRecordSelected());
                }
            });

        // tracking changes to save
        bindingGroup.addBindingListener(new AbstractBindingListener() {
            @Override
            public void targetChanged(Binding binding, PropertyStateEvent event) {
                // save action observes saveNeeded property
                setSaveNeeded(true);
            }
        });

        // have a transaction started
        entityManager.getTransaction().begin();
    }

    public boolean isSaveNeeded() {
        return saveNeeded;
    }

    private void setSaveNeeded(boolean saveNeeded) {
        if (saveNeeded != this.saveNeeded) {
            this.saveNeeded = saveNeeded;
            firePropertyChange("saveNeeded", !saveNeeded, saveNeeded);
        }
    }

    public boolean isRecordSelected() {
        return masterTable.getSelectedRow() != -1;
    }
    
    @Action
    public void newRecord() {
        oracleapp.Employee e = new oracleapp.Employee();
        entityManager.persist(e);
        list.add(e);
        int row = list.size()-1;
        masterTable.setRowSelectionInterval(row, row);
        masterTable.scrollRectToVisible(masterTable.getCellRect(row, 0, true));
        setSaveNeeded(true);
        JFrame mainFrame = OracleApp.getApplication().getMainFrame();
        newEmp ne = new newEmp(mainFrame, false);
        ne.setCurrentEmployee(e);
        ne.setVisible(true);
        if (ne.isEmployeeConfirmed()) {
            save().run();
        } else {
            refresh().run();
        }
    }
    
    @Action
    public void newWORecord() {
        oracleapp.WorksOn w = new oracleapp.WorksOn();
        entityManager.persist(w);
        worksOnList.add(w);
        int row = worksOnList.size()-1;
        woTable.setRowSelectionInterval(row, row);
        woTable.scrollRectToVisible(woTable.getCellRect(row, 0, true));
        JFrame mainFrame = OracleApp.getApplication().getMainFrame();
        newWorksOn nw = new newWorksOn(mainFrame, false);
        nw.setCurrentWorksOn(w);
        nw.setVisible(true);
        if (nw.isWorksOnConfirmed()) {
            save().run();
        } else {
            refresh().run();
        }
    }

    @Action
    public void newDepRecord() {
        oracleapp.Dependent d = new oracleapp.Dependent();
        entityManager.persist(d);
        dependentList.add(d);
        int row = dependentList.size()-1;
        depTable.setRowSelectionInterval(row, row);
        depTable.scrollRectToVisible(depTable.getCellRect(row, 0, true));
        JFrame mainFrame = OracleApp.getApplication().getMainFrame();
        newDependent nd = new newDependent(mainFrame, false);
        nd.setCurrentDependent(d);
        nd.setVisible(true);
        if (nd.isDependentConfirmed()) {
            save().run();
        } else {
            refresh().run();
        }
    }
    
    @Action(enabledProperty = "recordSelected")
    public void deleteRecord() {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Warning",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
        if (n == JOptionPane.YES_OPTION) {
            int[] selected = masterTable.getSelectedRows();
            List<oracleapp.Employee> toRemove = new ArrayList<oracleapp.Employee>(selected.length);
            for (int idx=0; idx<selected.length; idx++) {
                oracleapp.Employee e = list.get(masterTable.convertRowIndexToModel(selected[idx]));
                toRemove.add(e);
                entityManager.remove(e);
            }
            list.removeAll(toRemove);
            save().run();
        } else {
            refresh().run();
        }
    }
    
    @Action
    public void deleteWORecord() {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Warning",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
        if (n == JOptionPane.YES_OPTION) {
            int[] selected = woTable.getSelectedRows();
            List<oracleapp.WorksOn> toRemove = new ArrayList<oracleapp.WorksOn>(selected.length);
            for (int idx=0; idx<selected.length; idx++) {
                oracleapp.WorksOn w = worksOnList.get(woTable.convertRowIndexToModel(selected[idx]));
                toRemove.add(w);
                entityManager.remove(w);
            }
            worksOnList.removeAll(toRemove);
            save().run();
        } else {
            refresh().run();
        }
    }
    
    @Action
    public void deleteDepRecord() {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Warning",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
        if (n == JOptionPane.YES_OPTION) {
            int[] selected = depTable.getSelectedRows();
            List<oracleapp.Dependent> toRemove = new ArrayList<oracleapp.Dependent>(selected.length);
            for (int idx=0; idx<selected.length; idx++) {
                oracleapp.Dependent d = dependentList.get(depTable.convertRowIndexToModel(selected[idx]));
                toRemove.add(d);
                entityManager.remove(d);
            }
            dependentList.removeAll(toRemove);
            save().run();
        } else {
            refresh().run();
        }
    }
    
    @Action(enabledProperty = "saveNeeded")
    public Task save() {
        return new SaveTask(getApplication());
    }

    private class SaveTask extends Task {
        SaveTask(org.jdesktop.application.Application app) {
            super(app);
        }
        @Override protected Void doInBackground() {
            try {
                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            } catch (RollbackException rex) {
                //rex.printStackTrace();
                String strerror = rex.toString();
                int n = JOptionPane.showConfirmDialog(null, strerror, "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
                entityManager.getTransaction().begin();
                List<oracleapp.Employee> merged = new ArrayList<oracleapp.Employee>(list.size());
                for (oracleapp.Employee e : list) {
                    merged.add(entityManager.merge(e));
                }
                list.clear();
                list.addAll(merged);
                List<oracleapp.WorksOn> mergedWO = new ArrayList<oracleapp.WorksOn>(worksOnList.size());
                 for (oracleapp.WorksOn w : worksOnList) {
                    mergedWO.add(entityManager.merge(w));
                }
                worksOnList.clear();
                worksOnList.addAll(mergedWO);
                List<oracleapp.Dependent> mergedDep = new ArrayList<oracleapp.Dependent>(dependentList.size());
                for (oracleapp.Dependent d : dependentList) {
                    mergedDep.add(entityManager.merge(d));
                }
                dependentList.clear();
                dependentList.addAll(mergedDep);
            }
            return null;
        }
        @Override protected void finished() {
            setSaveNeeded(false);
        }
    }

    @Action
    public Task refresh() {
       return new RefreshTask(getApplication());
    }

    private class RefreshTask extends Task {
        RefreshTask(org.jdesktop.application.Application app) {
            super(app);
        }
        @SuppressWarnings("unchecked")
        @Override protected Void doInBackground() {
            entityManager.getTransaction().rollback();
            entityManager.getTransaction().begin();
            java.util.Collection data = query.getResultList();
            for (Object entity : data) {
                entityManager.refresh(entity);
            }
            list.clear();
            list.addAll(data);
            return null;
        }
        /*@Override protected void finished() {
            setMessage("Done.");
            setSaveNeeded(false);
        }*/
    }
    
    @Action
    public void cardSwitcher() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "empCard");
    }
    @Action
    public void cardSwitcher1() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "woCard");
    }
    @Action
    public void cardSwitcher2() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "depCard");
    }
    @Action
    public void cardSwitcher3() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "dlCard");
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        mainPanel = new javax.swing.JPanel();
        cardPanel = new javax.swing.JPanel();
        empPanel = new javax.swing.JPanel();
        masterScrollPane = new javax.swing.JScrollPane();
        masterTable = new javax.swing.JTable();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        woPanel = new javax.swing.JPanel();
        woPane = new javax.swing.JScrollPane();
        woTable = new javax.swing.JTable();
        newWOButton = new javax.swing.JButton();
        saveWOButton = new javax.swing.JButton();
        depPanel = new javax.swing.JPanel();
        depPane = new javax.swing.JScrollPane();
        depTable = new javax.swing.JTable();
        newDEPButton = new javax.swing.JButton();
        saveDEPButton = new javax.swing.JButton();
        dlPanel = new javax.swing.JPanel();
        dlPane = new javax.swing.JScrollPane();
        dlTable = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(oracleapp.OracleApp.class).getContext().getResourceMap(OracleView.class);
        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory(resourceMap.getString("entityManager.persistenceUnit")).createEntityManager(); // NOI18N
        query = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery(resourceMap.getString("query.query")); // NOI18N
        list = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(query.getResultList());
        worksOnQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT w FROM WorksOn w");
        worksOnList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : worksOnQuery.getResultList();
        dependentQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT d FROM Dependent d");
        dependentList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : dependentQuery.getResultList();
        departmentQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT d FROM Department d");
        departmentList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : departmentQuery.getResultList();
        deptLocationsQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT d FROM DeptLocations d");
        deptLocationsList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : deptLocationsQuery.getResultList();
        projectQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT p FROM Project p");
        projectList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : projectQuery.getResultList();

        mainPanel.setName("mainPanel"); // NOI18N

        cardPanel.setName("cardPanel"); // NOI18N
        cardPanel.setLayout(new java.awt.CardLayout());

        empPanel.setName("empPanel"); // NOI18N

        masterScrollPane.setName("masterScrollPane"); // NOI18N

        masterTable.setName("masterTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, list, masterTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fname}"));
        columnBinding.setColumnName("Fname");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${minit}"));
        columnBinding.setColumnName("Minit");
        columnBinding.setColumnClass(Character.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${lname}"));
        columnBinding.setColumnName("Lname");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ssn}"));
        columnBinding.setColumnName("Ssn");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${bdate}"));
        columnBinding.setColumnName("Bdate");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${address}"));
        columnBinding.setColumnName("Address");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sex}"));
        columnBinding.setColumnName("Sex");
        columnBinding.setColumnClass(Character.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${salary}"));
        columnBinding.setColumnName("Salary");
        columnBinding.setColumnClass(java.math.BigInteger.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${superSsn.ssn}"));
        columnBinding.setColumnName("Super Ssn.ssn");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dno}"));
        columnBinding.setColumnName("Dno");
        columnBinding.setColumnClass(java.math.BigInteger.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        masterScrollPane.setViewportView(masterTable);
        masterTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("masterTable.columnModel.title0")); // NOI18N
        masterTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("masterTable.columnModel.title1")); // NOI18N
        masterTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("masterTable.columnModel.title2")); // NOI18N
        masterTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("masterTable.columnModel.title3")); // NOI18N
        masterTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("masterTable.columnModel.title4")); // NOI18N
        masterTable.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("masterTable.columnModel.title5")); // NOI18N
        masterTable.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("masterTable.columnModel.title6")); // NOI18N
        masterTable.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("masterTable.columnModel.title7")); // NOI18N
        masterTable.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("masterTable.columnModel.title8")); // NOI18N
        masterTable.getColumnModel().getColumn(9).setHeaderValue(resourceMap.getString("masterTable.columnModel.title9")); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(oracleapp.OracleApp.class).getContext().getActionMap(OracleView.class, this);
        newButton.setAction(actionMap.get("newRecord")); // NOI18N
        newButton.setName("newButton"); // NOI18N

        deleteButton.setAction(actionMap.get("deleteRecord")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N

        javax.swing.GroupLayout empPanelLayout = new javax.swing.GroupLayout(empPanel);
        empPanel.setLayout(empPanelLayout);
        empPanelLayout.setHorizontalGroup(
            empPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(newButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addContainerGap(332, Short.MAX_VALUE))
            .addGroup(empPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(empPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(masterScrollPane)
                    .addContainerGap()))
        );

        empPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteButton, newButton});

        empPanelLayout.setVerticalGroup(
            empPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, empPanelLayout.createSequentialGroup()
                .addContainerGap(382, Short.MAX_VALUE)
                .addGroup(empPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newButton)
                    .addComponent(deleteButton))
                .addContainerGap())
            .addGroup(empPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(empPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(masterScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(48, Short.MAX_VALUE)))
        );

        cardPanel.add(empPanel, "empCard");

        woPanel.setName("woPanel"); // NOI18N

        woPane.setName("woPane"); // NOI18N

        woTable.setName("woTable"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, worksOnList, woTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${essn}"));
        columnBinding.setColumnName("Essn");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${pno}"));
        columnBinding.setColumnName("Pno");
        columnBinding.setColumnClass(java.math.BigInteger.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${hours}"));
        columnBinding.setColumnName("Hours");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        woPane.setViewportView(woTable);
        woTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("woTable.columnModel.title0")); // NOI18N
        woTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("woTable.columnModel.title1")); // NOI18N
        woTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("woTable.columnModel.title2")); // NOI18N

        newWOButton.setAction(actionMap.get("newWORecord")); // NOI18N
        newWOButton.setText(resourceMap.getString("newWOButton.text")); // NOI18N
        newWOButton.setName("newWOButton"); // NOI18N

        saveWOButton.setAction(actionMap.get("deleteWORecord")); // NOI18N
        saveWOButton.setText(resourceMap.getString("saveWOButton.text")); // NOI18N
        saveWOButton.setName("saveWOButton"); // NOI18N

        javax.swing.GroupLayout woPanelLayout = new javax.swing.GroupLayout(woPanel);
        woPanel.setLayout(woPanelLayout);
        woPanelLayout.setHorizontalGroup(
            woPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(woPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newWOButton)
                .addGap(10, 10, 10)
                .addComponent(saveWOButton)
                .addContainerGap(334, Short.MAX_VALUE))
            .addGroup(woPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(woPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(woPane, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        woPanelLayout.setVerticalGroup(
            woPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, woPanelLayout.createSequentialGroup()
                .addContainerGap(382, Short.MAX_VALUE)
                .addGroup(woPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveWOButton)
                    .addComponent(newWOButton))
                .addContainerGap())
            .addGroup(woPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(woPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(woPane, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(53, Short.MAX_VALUE)))
        );

        cardPanel.add(woPanel, "woCard");

        depPanel.setName("depPanel"); // NOI18N

        depPane.setName("depPane"); // NOI18N

        depTable.setName("depTable"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, dependentList, depTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${essn}"));
        columnBinding.setColumnName("Essn");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dependentName}"));
        columnBinding.setColumnName("Dependent Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sex}"));
        columnBinding.setColumnName("Sex");
        columnBinding.setColumnClass(Character.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${bdate}"));
        columnBinding.setColumnName("Bdate");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${relationship}"));
        columnBinding.setColumnName("Relationship");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        depPane.setViewportView(depTable);
        depTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("depTable.columnModel.title0")); // NOI18N
        depTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("depTable.columnModel.title1")); // NOI18N
        depTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("depTable.columnModel.title2")); // NOI18N
        depTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("depTable.columnModel.title3")); // NOI18N
        depTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("depTable.columnModel.title4")); // NOI18N

        newDEPButton.setAction(actionMap.get("newDepRecord")); // NOI18N
        newDEPButton.setText(resourceMap.getString("newDEPButton.text")); // NOI18N
        newDEPButton.setName("newDEPButton"); // NOI18N

        saveDEPButton.setAction(actionMap.get("deleteDepRecord")); // NOI18N
        saveDEPButton.setText(resourceMap.getString("saveDEPButton.text")); // NOI18N
        saveDEPButton.setName("saveDEPButton"); // NOI18N

        javax.swing.GroupLayout depPanelLayout = new javax.swing.GroupLayout(depPanel);
        depPanel.setLayout(depPanelLayout);
        depPanelLayout.setHorizontalGroup(
            depPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(depPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newDEPButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveDEPButton)
                .addContainerGap(334, Short.MAX_VALUE))
            .addGroup(depPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, depPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(depPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        depPanelLayout.setVerticalGroup(
            depPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(depPanelLayout.createSequentialGroup()
                .addContainerGap(382, Short.MAX_VALUE)
                .addGroup(depPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newDEPButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveDEPButton))
                .addContainerGap())
            .addGroup(depPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(depPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(depPane, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(46, Short.MAX_VALUE)))
        );

        cardPanel.add(depPanel, "depCard");

        dlPanel.setName("dlPanel"); // NOI18N

        dlPane.setName("dlPane"); // NOI18N

        dlTable.setName("dlTable"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, deptLocationsList, dlTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${department.dname}"));
        columnBinding.setColumnName("Department.dname");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${deptLocationsPK.dlocation}"));
        columnBinding.setColumnName("Dept Locations PK.dlocation");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${department.mgrSsn}"));
        columnBinding.setColumnName("Department.mgr Ssn");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${department.mgrStartDate}"));
        columnBinding.setColumnName("Department.mgr Start Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${department.dnumber}"));
        columnBinding.setColumnName("Department.dnumber");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        dlPane.setViewportView(dlTable);
        dlTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("dlTable.columnModel.title1")); // NOI18N
        dlTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("dlTable.columnModel.title0")); // NOI18N
        dlTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("dlTable.columnModel.title3")); // NOI18N
        dlTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("dlTable.columnModel.title2")); // NOI18N
        dlTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("dlTable.columnModel.title4")); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setName("jTable1"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, projectList, jTable1);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${pname}"));
        columnBinding.setColumnName("Pname");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${pnumber}"));
        columnBinding.setColumnName("Pnumber");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${plocation}"));
        columnBinding.setColumnName("Plocation");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dnum}"));
        columnBinding.setColumnName("Dnum");
        columnBinding.setColumnClass(java.math.BigInteger.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N
        jTable1.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable1.columnModel.title2")); // NOI18N
        jTable1.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable1.columnModel.title3")); // NOI18N

        javax.swing.GroupLayout dlPanelLayout = new javax.swing.GroupLayout(dlPanel);
        dlPanel.setLayout(dlPanelLayout);
        dlPanelLayout.setHorizontalGroup(
            dlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(dlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dlPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dlPanelLayout.setVerticalGroup(
            dlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(dlPane, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardPanel.add(dlPanel, "dlCard");

        jButton1.setAction(actionMap.get("cardSwitcher")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setAction(actionMap.get("cardSwitcher1")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton3.setAction(actionMap.get("cardSwitcher2")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        jButton4.setAction(actionMap.get("cardSwitcher3")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setComponent(mainPanel);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane depPane;
    private javax.swing.JPanel depPanel;
    private javax.swing.JTable depTable;
    private java.util.List<oracleapp.Department> departmentList;
    private javax.persistence.Query departmentQuery;
    private java.util.List<oracleapp.Dependent> dependentList;
    private javax.persistence.Query dependentQuery;
    private java.util.List<oracleapp.DeptLocations> deptLocationsList;
    private javax.persistence.Query deptLocationsQuery;
    private javax.swing.JScrollPane dlPane;
    private javax.swing.JPanel dlPanel;
    private javax.swing.JTable dlTable;
    private javax.swing.JPanel empPanel;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.util.List<oracleapp.Employee> list;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane masterScrollPane;
    private javax.swing.JTable masterTable;
    private javax.swing.JButton newButton;
    private javax.swing.JButton newDEPButton;
    private javax.swing.JButton newWOButton;
    private java.util.List<oracleapp.Project> projectList;
    private javax.persistence.Query projectQuery;
    private javax.persistence.Query query;
    private javax.swing.JButton saveDEPButton;
    private javax.swing.JButton saveWOButton;
    private javax.swing.JScrollPane woPane;
    private javax.swing.JPanel woPanel;
    private javax.swing.JTable woTable;
    private java.util.List<oracleapp.WorksOn> worksOnList;
    private javax.persistence.Query worksOnQuery;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    private boolean saveNeeded;
}
