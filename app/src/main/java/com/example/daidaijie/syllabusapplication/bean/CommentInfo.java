package com.example.daidaijie.syllabusapplication.bean;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/10.
 */
public class CommentInfo {

    /**
     * comment : so？
     * user : {"account":"14xfdeng","nickname":"xiaofud"}
     * post_time : 2016-08-08 01:00:40
     * uid : 1
     * post_id : 503
     * id : 329
     */

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        private String comment;
        /**
         * account : 14xfdeng
         * nickname : xiaofud
         */

        private UserBean user;
        private String post_time;
        private int uid;
        private int post_id;
        private int id;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getPost_time() {
            return post_time;
        }

        public void setPost_time(String post_time) {
            this.post_time = post_time;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getPost_id() {
            return post_id;
        }

        public void setPost_id(int post_id) {
            this.post_id = post_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class UserBean {
            private String account;
            private String nickname;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getNickname() {
                if (nickname == null || nickname.isEmpty()) {
                    return account;
                }
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getName() {
                return getNickname().trim().isEmpty() ? getAccount() : getNickname();
            }
        }
    }
}
