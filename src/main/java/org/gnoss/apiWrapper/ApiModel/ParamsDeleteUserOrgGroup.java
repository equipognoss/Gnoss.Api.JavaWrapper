package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Parameters for delete a user from a organization group
 * @author GNOSS
 */
public class ParamsDeleteUserOrgGroup {
    
    private UUID user_id;
    private String organization_short_name;
    private String group_short_name;
    private String login;
    
    /**
     * User identificator
     * @return user id
     */
    public UUID getUser_id() {
        return user_id;
    }
    
    /**
     * User identificator
     * @param user_id user id
     */
    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }
    
    /**
     * Organization short name
     * @return organization short name
     */
    public String getOrganization_short_name() {
        return organization_short_name;
    }
    
    /**
     * Organization short name
     * @param organization_short_name organization short name
     */
    public void setOrganization_short_name(String organization_short_name) {
        this.organization_short_name = organization_short_name;
    }
    
    /**
     * Group where the user is going to be deleted
     * @return group short name
     */
    public String getGroup_short_name() {
        return group_short_name;
    }
    
    /**
     * Group where the user is going to be deleted
     * @param group_short_name group short name
     */
    public void setGroup_short_name(String group_short_name) {
        this.group_short_name = group_short_name;
    }
    
    /**
     * User email or short name
     * @return login
     */
    public String getLogin() {
        return login;
    }
    
    /**
     * User email or short name
     * @param login login
     */
    public void setLogin(String login) {
        this.login = login;
    }
}