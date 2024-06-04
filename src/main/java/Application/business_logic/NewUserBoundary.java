package Application.business_logic;

import Application.DataAccess.EntityUser;

public class NewUserBoundary {

    private String email;
    private RoleEnumBoundary role;
    private String userName;
    private String avatar;

    public NewUserBoundary() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(RoleEnumBoundary role) {
        this.role = role;
    }

    public RoleEnumBoundary getRole() {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public EntityUser toEntity() {
    	EntityUser entity = new EntityUser();

        entity.setId(this.getEmail());
        entity.setRole(this.getRole());
        entity.setUserName(this.getUserName() == null ? "Anonymous" : this.getUserName());
        entity.setAvatar(this.getAvatar() == null ? "F" : this.getAvatar());

        return entity;

    }
    public BoundaryUser newUserToUserBoundary() {
    	BoundaryUser  Boundary = new BoundaryUser ();
    	
    	Boundary.setRole(this.getRole());
        Boundary.setUserName(this.getUserName() == null ? "Anonymous" : this.getUserName());
        Boundary.setAvatar(this.getAvatar() == null ? "F" : this.getAvatar());
        Boundary.setUserId(new User_Id("" ,this.getEmail()));

        return Boundary;

    }
    @Override
    public String toString() {
        return "NewUserBoundary{" +
                "email='" + email + '\'' +
                ", role=" + role +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

