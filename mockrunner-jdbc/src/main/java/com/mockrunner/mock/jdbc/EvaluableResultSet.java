package com.mockrunner.mock.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Radim Vansa &lt;rvansa@redhat.com&gt;
 */
public class EvaluableResultSet extends MockResultSet {

   public EvaluableResultSet(String id) {
      super(id);
   }

   public EvaluableResultSet(String id, String cursorName) {
      super(id, cursorName);
   }

   @Override
   public MockResultSet evaluate(String sql, MockParameterMap parameters) {
      try {
         MockResultSet newResultSet = new MockResultSet(getId(), getCursorName());
         ResultSetMetaData metaData = getMetaData();
         for (int i = 1; i <= getColumnCount(); ++i) {
            String columnName = metaData.getColumnName(i);
            List<Object> values = getColumn(columnName);
            List<Object> newValues = new ArrayList<Object>(values.size());
            int row = 0;
            for (Object value : values) {
               if (value instanceof Evaluable) {
                  newValues.add(((Evaluable) value).evaluate(sql, parameters, columnName, row));
               } else {
                  newValues.add(value);
               }
               ++row;
            }
            newResultSet.addColumn(columnName, newValues);
         }
         return newResultSet;
      } catch (SQLException e) {
         throw new RuntimeException("Never triggered", e);
      }
   }

   /**
    * Implementation of this interface will be stored in the result set.
    */
   public interface Evaluable {
      Object evaluate(String sql, MockParameterMap parameters, String columnName, int row);
   }
}
