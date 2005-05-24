package com.mockrunner.example.ejb;


import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/*
 * @ejb:bean name="BillEntity"
 *           display-name="BillEntity"
 *           type="CMP"
 *           cmp-version="2.x"
 *           primkey-field="id"
 *           jndi-name="de/test/BillEntity"
 *
 * @ejb:pk class="java.lang.Integer"
 *
 * @ejb:transaction type="Required"
 * 
 * @ejb:finder signature="java.util.Collection findUnpaid()"
 *             query="SELECT OBJECT(t) FROM BillEntity as t WHERE t.paid = false"
 * 
 * @ejb:finder signature="java.util.Collection findAll()"
 *             
 * @jboss:persistence datasource="java:/MySQLDB"
 *                    table-name="BillEntity"
 *                    create-table="true"
 *                    remove-table="false"
 **/
/**
 * This CMP entity bean represents a bill.
 * It has a date, and a marker if it is paid.
 * It has two custom finders, namely
 * <code>findUnpaid()</code> and <code>findAll()</code>.
 */
public abstract class BillEntityBean implements EntityBean
{
	public EntityContext entityContext;
   
   	/*
     * @ejb:interface-method
     * @ejb:persistence
     * @ejb:pk-field
     * @jboss:column-name name="id"
     **/
   	public abstract Integer getId();
   	public abstract void setId(Integer id);
   
    /*
     * @ejb:interface-method
     * @ejb:persistence
     * @jboss:column-name name="date" sql-type="DATETIME" jdbc-type="DATE"
     **/
   	public abstract Date getDate();
   	
	/*
	 * @ejb:interface-method
	 **/
  	public abstract void setDate(Date date);
   
    /*
     * @ejb:interface-method
     * @ejb:persistence
     * @jboss:column-name name="paid"
     **/
   	public abstract boolean getPaid();
   	
	/*
	 * @ejb:interface-method
	 **/
   	public abstract void setPaid(boolean isPaid);
   
	/*
	 * @ejb:create-method
	 **/
	public Integer ejbCreate(Integer id) throws EJBException, CreateException
	{
		setId(id);
		return null;
	}
   
   	public void ejbPostCreate(Integer id)
   	{
   	}
   
   	public void setEntityContext(EntityContext context)
   	{
		entityContext = context;
   	}
   
  	public void unsetEntityContext()
    {
		entityContext = null;
    }
   
    public void ejbActivate()
    {
    }
   
    public void ejbPassivate()
    {
    }
   
    public void ejbLoad()
    {
    }
   
    public void ejbStore()
    {
    }
   
    public void ejbRemove() throws RemoveException
    {
    }
}
