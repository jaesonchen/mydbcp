package com.asiainfo.dbcp.delegate;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * @Description: CallableStatement代理，构建时将CallableStatement加入连接的Trace队列里
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午10:10:53
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class DelegatingCallableStatement extends DelegatingPreparedStatement implements CallableStatement {

	public DelegatingCallableStatement(DelegatingConnection conn, CallableStatement stmt) {
		super(conn, stmt);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        ((CallableStatement) stmt).registerOutParameter(parameterIndex, sqlType);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        ((CallableStatement) stmt).registerOutParameter(parameterIndex, sqlType, scale);
	}

	@Override
	public boolean wasNull() throws SQLException {
        return ((CallableStatement) stmt).wasNull();
	}

	@Override
	public String getString(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getString(parameterIndex);
	}

	@Override
	public boolean getBoolean(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getBoolean(parameterIndex);
	}

	@Override
	public byte getByte(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getByte(parameterIndex);
	}

	@Override
	public short getShort(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getShort(parameterIndex);
	}

	@Override
	public int getInt(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getInt(parameterIndex);
	}

	@Override
	public long getLong(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getLong(parameterIndex);
	}

	@Override
	public float getFloat(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getFloat(parameterIndex);
	}

	@Override
	public double getDouble(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getDouble(parameterIndex);
	}

	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return ((CallableStatement) stmt).getBigDecimal(parameterIndex, scale);
	}

	@Override
	public byte[] getBytes(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getBytes(parameterIndex);
	}

	@Override
	public Date getDate(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getDate(parameterIndex);
	}

	@Override
	public Time getTime(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getTime(parameterIndex);
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getTimestamp(parameterIndex);
	}

	@Override
	public Object getObject(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getObject(parameterIndex);
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getBigDecimal(parameterIndex);
	}

	@Override
	public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return ((CallableStatement) stmt).getObject(parameterIndex, map);
	}

	@Override
	public Ref getRef(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getRef(parameterIndex);
	}

	@Override
	public Blob getBlob(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getBlob(parameterIndex);
	}

	@Override
	public Clob getClob(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getClob(parameterIndex);
	}

	@Override
	public Array getArray(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getArray(parameterIndex);
	}

	@Override
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return ((CallableStatement) stmt).getDate(parameterIndex);
	}

	@Override
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return ((CallableStatement) stmt).getTime(parameterIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return ((CallableStatement) stmt).getTimestamp(parameterIndex, cal);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        ((CallableStatement) stmt).registerOutParameter(parameterIndex, sqlType, typeName);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        ((CallableStatement) stmt).registerOutParameter(parameterName, sqlType);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        ((CallableStatement) stmt).registerOutParameter(parameterName, sqlType, scale);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        ((CallableStatement) stmt).registerOutParameter(parameterName, sqlType, typeName);
	}

	@Override
	public URL getURL(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getURL(parameterIndex);
	}

	@Override
	public void setURL(String parameterName, URL val) throws SQLException {
        ((CallableStatement) stmt).setURL(parameterName, val);
	}

	@Override
	public void setNull(String parameterName, int sqlType) throws SQLException {
        ((CallableStatement) stmt).setNull(parameterName, sqlType);
	}

	@Override
	public void setBoolean(String parameterName, boolean x) throws SQLException {
        ((CallableStatement) stmt).setBoolean(parameterName, x);
	}

	@Override
	public void setByte(String parameterName, byte x) throws SQLException {
        ((CallableStatement) stmt).setByte(parameterName, x);
	}

	@Override
	public void setShort(String parameterName, short x) throws SQLException {
        ((CallableStatement) stmt).setShort(parameterName, x);
	}

	@Override
	public void setInt(String parameterName, int x) throws SQLException {
        ((CallableStatement) stmt).setInt(parameterName, x);
	}

	@Override
	public void setLong(String parameterName, long x) throws SQLException {
        ((CallableStatement) stmt).setLong(parameterName, x);
	}

	@Override
	public void setFloat(String parameterName, float x) throws SQLException {
        ((CallableStatement) stmt).setFloat(parameterName, x);
	}

	@Override
	public void setDouble(String parameterName, double x) throws SQLException {
        ((CallableStatement) stmt).setDouble(parameterName, x);
	}

	@Override
	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        ((CallableStatement) stmt).setBigDecimal(parameterName, x);
	}

	@Override
	public void setString(String parameterName, String x) throws SQLException {
        ((CallableStatement) stmt).setString(parameterName, x);
	}

	@Override
	public void setBytes(String parameterName, byte[] x) throws SQLException {
        ((CallableStatement) stmt).setBytes(parameterName, x);
	}

	@Override
	public void setDate(String parameterName, Date x) throws SQLException {
        ((CallableStatement) stmt).setDate(parameterName, x);
	}

	@Override
	public void setTime(String parameterName, Time x) throws SQLException {
        ((CallableStatement) stmt).setTime(parameterName, x);
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        ((CallableStatement) stmt).setTimestamp(parameterName, x);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        ((CallableStatement) stmt).setAsciiStream(parameterName, x, length);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        ((CallableStatement) stmt).setBinaryStream(parameterName, x, length);
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        ((CallableStatement) stmt).setObject(parameterName, x, targetSqlType, scale);
	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        ((CallableStatement) stmt).setObject(parameterName, x, targetSqlType);
	}

	@Override
	public void setObject(String parameterName, Object x) throws SQLException {
        ((CallableStatement) stmt).setObject(parameterName, x);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        ((CallableStatement) stmt).setCharacterStream(parameterName, reader, length);
	}

	@Override
	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        ((CallableStatement) stmt).setDate(parameterName, x, cal);
	}

	@Override
	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        ((CallableStatement) stmt).setTime(parameterName, x, cal);
	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        ((CallableStatement) stmt).setTimestamp(parameterName, x, cal);
	}

	@Override
	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        ((CallableStatement) stmt).setNull(parameterName, sqlType, typeName);
	}

	@Override
	public String getString(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getString(parameterName);
	}

	@Override
	public boolean getBoolean(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getBoolean(parameterName);
	}

	@Override
	public byte getByte(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getByte(parameterName);
	}

	@Override
	public short getShort(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getShort(parameterName);
	}

	@Override
	public int getInt(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getInt(parameterName);
	}

	@Override
	public long getLong(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getLong(parameterName);
	}

	@Override
	public float getFloat(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getFloat(parameterName);
	}

	@Override
	public double getDouble(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getDouble(parameterName);
	}

	@Override
	public byte[] getBytes(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getBytes(parameterName);
	}

	@Override
	public Date getDate(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getDate(parameterName);
	}

	@Override
	public Time getTime(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getTime(parameterName);
	}

	@Override
	public Timestamp getTimestamp(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getTimestamp(parameterName);
	}

	@Override
	public Object getObject(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getObject(parameterName);
	}

	@Override
	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getBigDecimal(parameterName);
	}

	@Override
	public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return ((CallableStatement) stmt).getObject(parameterName, map);
	}

	@Override
	public Ref getRef(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getRef(parameterName);
	}

	@Override
	public Blob getBlob(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getBlob(parameterName);
	}

	@Override
	public Clob getClob(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getClob(parameterName);
	}

	@Override
	public Array getArray(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getArray(parameterName);
	}

	@Override
	public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return ((CallableStatement) stmt).getDate(parameterName, cal);
	}

	@Override
	public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return ((CallableStatement) stmt).getTime(parameterName, cal);
	}

	@Override
	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return ((CallableStatement) stmt).getTimestamp(parameterName, cal);
	}

	@Override
	public URL getURL(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getURL(parameterName);
	}

	@Override
	public RowId getRowId(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getRowId(parameterIndex);
	}

	@Override
	public RowId getRowId(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getRowId(parameterName);
	}

	@Override
	public void setRowId(String parameterName, RowId x) throws SQLException {
        ((CallableStatement) stmt).setRowId(parameterName, x);
	}

	@Override
	public void setNString(String parameterName, String value) throws SQLException {
        ((CallableStatement) stmt).setNString(parameterName, value);
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        ((CallableStatement) stmt).setNCharacterStream(parameterName, value, length);
	}

	@Override
	public void setNClob(String parameterName, NClob value) throws SQLException {
        ((CallableStatement) stmt).setNClob(parameterName, value);
	}

	@Override
	public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        ((CallableStatement) stmt).setClob(parameterName, reader, length);
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        ((CallableStatement) stmt).setBlob(parameterName, inputStream, length);
	}

	@Override
	public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        ((CallableStatement) stmt).setNClob(parameterName, reader, length);
	}

	@Override
	public NClob getNClob(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getNClob(parameterIndex);
	}

	@Override
	public NClob getNClob(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getNClob(parameterName);
	}

	@Override
	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        ((CallableStatement) stmt).setSQLXML(parameterName, xmlObject);
	}

	@Override
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getSQLXML(parameterIndex);
	}

	@Override
	public SQLXML getSQLXML(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getSQLXML(parameterName);
	}

	@Override
	public String getNString(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getNString(parameterIndex);
	}

	@Override
	public String getNString(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getNString(parameterName);
	}

	@Override
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getNCharacterStream(parameterIndex);
	}

	@Override
	public Reader getNCharacterStream(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getNCharacterStream(parameterName);
	}

	@Override
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
        return ((CallableStatement) stmt).getCharacterStream(parameterIndex);
	}

	@Override
	public Reader getCharacterStream(String parameterName) throws SQLException {
        return ((CallableStatement) stmt).getCharacterStream(parameterName);
	}

	@Override
	public void setBlob(String parameterName, Blob x) throws SQLException {
        ((CallableStatement) stmt).setBlob(parameterName, x);
	}

	@Override
	public void setClob(String parameterName, Clob x) throws SQLException {
        ((CallableStatement) stmt).setClob(parameterName, x);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        ((CallableStatement) stmt).setAsciiStream(parameterName, x, length);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        ((CallableStatement) stmt).setBinaryStream(parameterName, x, length);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        ((CallableStatement) stmt).setCharacterStream(parameterName, reader, length);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        ((CallableStatement) stmt).setAsciiStream(parameterName, x);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        ((CallableStatement) stmt).setBinaryStream(parameterName, x);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        ((CallableStatement) stmt).setCharacterStream(parameterName, reader);
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        ((CallableStatement) stmt).setNCharacterStream(parameterName, value);
	}

	@Override
	public void setClob(String parameterName, Reader reader) throws SQLException {
        ((CallableStatement) stmt).setClob(parameterName, reader);
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        ((CallableStatement) stmt).setBlob(parameterName, inputStream);
	}

	@Override
	public void setNClob(String parameterName, Reader reader) throws SQLException {
        ((CallableStatement) stmt).setNClob(parameterName, reader);
	}

	@Override
	public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        return ((CallableStatement) stmt).getObject(parameterIndex, type);

	}

	@Override
	public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        return ((CallableStatement) stmt).getObject(parameterName, type);
	}
}