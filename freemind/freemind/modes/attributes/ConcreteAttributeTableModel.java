/*
 * Created on 18.06.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.modes.attributes;

import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import freemind.main.XMLElement;
import freemind.modes.MindMapNode;
import freemind.modes.XMLElementAdapter;

/**
 * @author Dimitri Polivaev
 * 18.06.2005
 */
public class ConcreteAttributeTableModel extends AbstractTableModel implements AttributeTableModel{
    private MindMapNode node;
    private Vector attributes;
    private AttributeTableLayoutModel layout;
    private static final int CAPACITY_INCREMENT = 10;
    public ConcreteAttributeTableModel(MindMapNode node, int size) {
        super();
        attributes = new Vector(size, CAPACITY_INCREMENT);
        layout = new AttributeTableLayoutModel();
        this.node = node;
    }
    
    public ConcreteAttributeTableModel(MindMapNode node) {
        this(node, 0);
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return attributes.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        Attribute attr = (Attribute)attributes.get(row);
        switch(col){
        case 0: return attr.getName();
        case 1: return attr.getValue();
        }
        return null;
    }

    public void setValueAt(Object o, int row, int col) {
        Attribute attr = (Attribute)attributes.get(row);
        String s = o.toString();
        switch(col){
        case 0: 
            if(attr.getName().equals(s))
                return;
            attr.setName(s);
            break;
        case 1: 
            if(attr.getValue().equals(s))
                return;
            attr.setValue(s);
            break;
        }
        node.getMap().getRegistry().addAttribute(attr);
        fireTableCellUpdated(row, col);
    }
    
    public void insertRow(int index, Attribute newAttribute) {
        attributes.add(index, newAttribute);
        fireTableRowsInserted(index, index);
    }
    public void addRow(Attribute newAttribute) {
        int index = getRowCount();
        attributes.add(newAttribute);
        node.getMap().getRegistry().addAttribute(newAttribute);
        fireTableRowsInserted(index, index);
    }
    public Object removeRow(int index) {
        Object o = attributes.remove(index);
        fireTableRowsDeleted(index, index);
        return o;
    }
    public void  save(XMLElement node) {
        saveLayout(node);
    	for (int i = 0; i < attributes.size(); i++) {
            saveAttribute(node, i);
        }
    }
    private void saveAttribute(XMLElement node, int i) {
        XMLElement attributeElement = new XMLElement();
        attributeElement.setName(XMLElementAdapter.XML_NODE_ATTRIBUTE);
        Attribute attr = (Attribute) attributes.get(i);
        attributeElement.setAttribute("NAME", attr.getName());
        attributeElement.setAttribute("VALUE", attr.getValue());
        node.addChild(attributeElement);
    }

    private void saveLayout(XMLElement node) {
        XMLElement attributeElement = null;
        if(! layout.getViewType().equals(AttributeTableLayoutModel.SHOW_SELECTED)){
            attributeElement = initializeNodeAttributeLayoutXMLElement(attributeElement);
            attributeElement.setAttribute("VIEWTYPE", layout.getViewType());
        }
        if(layout.getColumnWidth(0)!= AttributeTableLayoutModel.DEFAULT_COLUMN_WIDTH){
            attributeElement = initializeNodeAttributeLayoutXMLElement(attributeElement);
            attributeElement.setIntAttribute("NAME_WIDTH", getColumnWidth(0));
        }
        if(layout.getColumnWidth(1)!= AttributeTableLayoutModel.DEFAULT_COLUMN_WIDTH){
            attributeElement = initializeNodeAttributeLayoutXMLElement(attributeElement);
            attributeElement.setIntAttribute("VALUE_WIDTH", layout.getColumnWidth(1));
        }
        if(attributeElement != null){
            node.addChild(attributeElement);
        }
   }

    private XMLElement initializeNodeAttributeLayoutXMLElement(XMLElement attributeElement) {
        if(attributeElement == null){
            attributeElement = new XMLElement();
            attributeElement.setName(XMLElementAdapter.XML_NODE_ATTRIBUTE_LAYOUT);
        }
        return attributeElement;
    }

    public MindMapNode getNode() {
        return node;
    }

    /* (non-Javadoc)
     * @see freemind.modes.attributes.AttributeTableModel#get(int)
     */
    public Attribute getAttribute(int row) {
        return (Attribute)attributes.get(row);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 2;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int arg0, int arg1) {
        return ! node.getMap().isReadOnly();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
     */
    public void addTableModelListener(TableModelListener arg0) {
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
     */
    public void removeTableModelListener(TableModelListener arg0) {
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return "";
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int col) {
        return Object.class;
    }

    public int getColumnWidth(int col) {
        return layout.getColumnWidth(col);
    }
    public void setColumnWidth(int col, int width) {
        layout.setColumnWidth(col, width);
    }
       
    public String getViewType() {
        return layout.getViewType();
    }
    public void setViewType(String viewType) {
        layout.setViewType(viewType);
    }
    public AttributeTableLayoutModel getLayout() {
        return layout;
    }
 }
