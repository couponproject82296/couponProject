package b.JavaBeans;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * This class defines the Customer java bean
 */

@XmlRootElement
public class Customer implements Serializable {


	private static final long serialVersionUID = 1L;
	private long id;
	private String custName;
	private String password;
	private Collection<Coupon> coupons;

	public Customer() {
	}

	// Since the id is allocated automatically by the database, it was removed
	// from the CTOR herein (the id will receive a temporary default value till
	// the setter method, applied in the CustomerDBDAO, will update its value)

	public Customer(String custName, String password) {
		super();
		this.custName = custName;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	@Transient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Customer [id = " + id + ", customer name = " + custName + ", password = " + password + "]";
	}

}