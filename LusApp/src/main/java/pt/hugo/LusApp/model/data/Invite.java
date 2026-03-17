package pt.hugo.LusApp.model.data;

public class Invite {
    private String inviteID;
    private String username;
    private String status;
    private String profileImage;


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getInviteID() {
        return inviteID;
    }

    public void setInviteID(String inviteID) {
        this.inviteID = inviteID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
