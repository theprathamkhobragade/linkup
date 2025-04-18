package com.project.linkup;

public class GridItem {
    String PostId, UserId,ImageUri, Username,Caption;
    int Likes, Comments;

    public GridItem(){ }

    public GridItem(String postId, String userId, String imageUri, String username, String caption, int likes, int comments) {
        this.PostId = postId;
        this.UserId = userId;
        this.ImageUri = imageUri;
        this.Username = username;
        this.Caption = caption;
        this.Likes = likes;
        this.Comments = comments;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getComments() {
        return Comments;
    }

    public void setComments(int comments) {
        Comments = comments;
    }

}
