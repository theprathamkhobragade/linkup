package com.project.linkup;

public class Posts {
    String PostId, UserId,ImageUri,Caption;
    int Likes, Comments;

    public Posts(){ }

    public Posts(String postId, String userId, String imageUri, String caption, int likes, int comments) {
        this.PostId = postId;
        this.UserId = userId;
        this.ImageUri = imageUri;
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
