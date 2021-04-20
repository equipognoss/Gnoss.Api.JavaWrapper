package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters for the login password
 * @author Andrea
 *
 */
public class ParamsLoginPassword {
	private String login;
	private String password;
	
	/**
	 * Login or email of the user
	 * Example: fer123
	 * @return login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * Login or email of the user 
	 * Example:fer123
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * Password of the user
	 * @return password 
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Password of the user
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	

}
