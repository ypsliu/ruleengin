/**
 * 
 */
package cn.rongcapital.ruleengine.model;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 规则的数据源配置
 * 
 * @author shangchunming@rongcapital.cn
 *
 */
public final class Datasource extends BaseEntity {

	/**
	 * 驱动类名
	 */
	@NotNull
	@JsonProperty("driver_class")
	private String driverClass;

	/**
	 * JDBC URL
	 */
	@NotNull
	private String url;

	/**
	 * 用户名
	 */
	@NotNull
	private String user;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 连接池最大连接数
	 */
	@JsonProperty("max_pool_size")
	private int maxPoolSize = 5;

	/**
	 * 校验连接用的SQL
	 */
	@JsonProperty("validation_sql")
	private String validationSql = "select 1";

	/**
	 * @return the driverClass
	 */
	public String getDriverClass() {
		return driverClass;
	}

	/**
	 * @param driverClass
	 *            the driverClass to set
	 */
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the maxPoolSize
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * @param maxPoolSize
	 *            the maxPoolSize to set
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return the validationSql
	 */
	public String getValidationSql() {
		return validationSql;
	}

	/**
	 * @param validationSql
	 *            the validationSql to set
	 */
	public void setValidationSql(String validationSql) {
		this.validationSql = validationSql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((driverClass == null) ? 0 : driverClass.hashCode());
		result = prime * result + maxPoolSize;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((validationSql == null) ? 0 : validationSql.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Datasource other = (Datasource) obj;
		if (driverClass == null) {
			if (other.driverClass != null) {
				return false;
			}
		} else if (!driverClass.equals(other.driverClass)) {
			return false;
		}
		if (maxPoolSize != other.maxPoolSize) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		if (validationSql == null) {
			if (other.validationSql != null) {
				return false;
			}
		} else if (!validationSql.equals(other.validationSql)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Datasource [driverClass=" + driverClass + ", url=" + url + ", user=" + user + ", password=" + password
				+ ", maxPoolSize=" + maxPoolSize + ", validationSql=" + validationSql + ", code=" + code + ", name="
				+ name + ", comment=" + comment + ", creationTime=" + creationTime + ", updateTime=" + updateTime + "]";
	}

}
