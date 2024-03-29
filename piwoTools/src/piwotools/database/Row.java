package piwotools.database;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import piwotools.data.SimpleDate;

public class Row {

	HashMap<String,Object> data = new HashMap<String,Object>();
	
	public void setValue(String key, Object value) {
		data.put(key, value);
	}
	
	public Object getValue(String key) {
		return data.get(key);
	}

	public String getValueAsString(String key) {
		if(data.get(key) == null) {
			return null;
		}
		return data.get(key).toString();
	}
	
	public String getValueAsStringNotNull(String key) {
		if(data.get(key) == null) {
			return "";
		}
		return data.get(key).toString();
	}

	public Boolean getValueAsBoolean(String key) {
		if(data.get(key) == null) {
			return null;
		}
		return (Boolean)data.get(key);
	}

	public Long getValueAsLong(String key) {
		if(data.get(key) == null) {
			return null;
		}
		return (Long)data.get(key);
	}
	
	public Integer getValueAsInt(String key) {
		if(data.get(key) == null) {
			return null;
		}
		return (Integer)data.get(key);
	}
	
	public Date getValueAsDate(String key) {
		if(data.get(key) == null) {
			return null;
		}
		return (Date)data.get(key);
	}
	
	public Timestamp getValueAsTimestamp(String key) {
		if(data.get(key) == null) {
			return null;
		}
		return (Timestamp)data.get(key);
	}
	
	public SimpleDate getValueAsSimpleDate(String key) {
		if(data.get(key) == null) {
			return null;
		}
		if(data.get(key) instanceof Date) {
			return SimpleDate.fromDate((Date)data.get(key));
		}
		return (SimpleDate)data.get(key);
	}

	@Override
	public String toString() {
		return "Row=" + data + ";";
	}
	
	
}
