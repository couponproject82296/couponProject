package b.JavaBeans;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;



/**
 * This class defines the Company java bean
 */
@XmlRootElement
public class Company implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private long id;
	private String compName;
	private String password;
	private String email;
	private Collection<Coupon> coupons;

	public Company() {
	}

	// Since the id is allocated automatically by the database, it was removed
	// from the CTOR herein (the id will receive a temporary default value till
	// the setter method, applied in the CompanyDBDAO, will update its value)

	public Company(String compName, String password, String email) {
		super();
		this.compName = compName;
		this.password = password;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	@Transient
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Company [id = " + id + ", company name = " + compName + ", password = " + password + ", email = "
				+ email + "]";
	}

}