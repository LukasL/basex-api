package org.basex.api.xmldb;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * Implementation of the Database Interface for the XMLDB:API
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Andreas Weiler
 */
public class BXDatabaseImpl implements Database {

  /**
   * @see org.xmldb.api.base.Database#acceptsURI(java.lang.String)
   */
  public boolean acceptsURI(String uri) throws XMLDBException {
    throw new XMLDBException();
    //return false;
  }

  /**
   * @see org.xmldb.api.base.Database#getCollection(java.lang.String, java.lang.String, java.lang.String)
   */
  public Collection getCollection(String uri, String username, String password)
      throws XMLDBException {
    // TODO Auto-generated method stub
    throw new XMLDBException();
    //return null;
  }

  /**
   * @see org.xmldb.api.base.Database#getConformanceLevel()
   */
  public String getConformanceLevel() throws XMLDBException {
    // TODO Auto-generated method stub
    throw new XMLDBException();
    //return null;
  }

  /**
   * @see org.xmldb.api.base.Database#getName()
   */
  public String getName() throws XMLDBException {
    // TODO Auto-generated method stub
    throw new XMLDBException();
    //return null;
  }

  /**
   * @see org.xmldb.api.base.Configurable#getProperty(java.lang.String)
   */
  public String getProperty(String name) throws XMLDBException {
    // TODO Auto-generated method stub
    throw new XMLDBException();
    //return null;
  }
  
  /**
   * @see org.xmldb.api.base.Configurable#setProperty(java.lang.String, java.lang.String)
   */
  public void setProperty(String name, String value) throws XMLDBException {
    // TODO Auto-generated method stub
    throw new XMLDBException();
  }
  
  

}