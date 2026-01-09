package model;

import java.sql.Timestamp;

public class Comment {
    private int commentId;
    private Product product;
    private User user;
    private String content;
    private Timestamp created_at;
    private Integer parentCommentId;

    public Comment() {
    }

    public Comment(int commentId, Product product, User user, String content, Timestamp created_at, Integer parentCommentId) {
        this.commentId = commentId;
        this.product = product;
        this.user = user;
        this.content = content;
        this.created_at = created_at;
        this.parentCommentId = parentCommentId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getcreatedAt() {
        return created_at;
    }

    public void setcreatedAt(Timestamp timestamp) {
        this.created_at = created_at;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", product=" + product +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", timestamp=" + created_at +
                ", parentCommentId=" + parentCommentId +
                '}';
    }
}
