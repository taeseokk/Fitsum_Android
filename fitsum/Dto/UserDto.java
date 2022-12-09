package org.fitsum.Dto;

import com.google.gson.annotations.SerializedName;

public class UserDto {

    //회원가입 Dto
    public static class SignupUserDto{

        @SerializedName("userId")           //아이디
        private String userId;

        @SerializedName("userPw")           //비밀번호
        private String userPw;

        @SerializedName("nickName")         //닉네임
        private String nickName;

        @SerializedName("email")            //이메일
        private String email;

        @SerializedName("userName")         //사용자 이름
        private String userName;

        @SerializedName("userSex")          //성별  남자 = 1 여자 =2
        private Integer userSex;

        @SerializedName("userItem")         //사용자의 옷 상태 남자 1~9  /  여자 10 ~ 18
        private Integer userItem;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserPw() {
            return userPw;
        }

        public void setUserPw(String userPw) {
            this.userPw = userPw;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserSex() {
            return userSex;
        }

        public void setUserSex(int userSex) {
            this.userSex = userSex;
        }

        public Integer getUserItem() {
            return userItem;
        }

        public void setUserItem(Integer userItem) {
            this.userItem = userItem;
        }


        @Override
        public String toString() {
            return "SignupUserDto{" +
                    "userId='" + userId + '\'' +
                    ", userPw='" + userPw + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", email='" + email + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userSex=" + userSex +
                    ", userItem=" + userItem +
                    '}';
        }
    }

    // 비번 찾기
    public static class FindPwDto {

        @SerializedName("userId")
        private String userId;
        @SerializedName("email")
        private String email;
        @SerializedName("userName")
        private String userName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }

    //비번 변경
    public static class ChangePwDto{
        @SerializedName("curPw")
        private String curPw;

        @SerializedName("newPw")
        private String newPw;

        public String getCurPw() {
            return curPw;
        }

        public void setCurPw(String curPw) {
            this.curPw = curPw;
        }

        public String getNewPw() {
            return newPw;
        }

        public void setNewPw(String newPw) {
            this.newPw = newPw;
        }
    }

    // 닉네임 변경
    public static class ChangenickNameDto {
        @SerializedName("curnickName")
        private String curnickName;

        @SerializedName("newnickName")
        private String newnickName;

        public String getcurnickName() {
            return curnickName;
        }

        public void setcurnickName(String curnickName) {
            this.curnickName = curnickName;
        }

        public String getnewnickName() {
            return newnickName;
        }

        public void setnewnickName(String newnickName) {
            this.newnickName = newnickName;
        }
    }

    // 현재 옷 변경
    public static class ChangeUserItemDto {
        @SerializedName("curUserItem")
        private Integer curUserItem;

        @SerializedName("newUserItem")
        private Integer newUserItem;

        public Integer getCurUserItem() {
            return curUserItem;
        }

        public void setCurUserItem(Integer curUserItem) {
            this.curUserItem = curUserItem;
        }

        public Integer getNewUserItem() {
            return newUserItem;
        }

        public void setNewUserItem(Integer newUserItem) {
            this.newUserItem = newUserItem;
        }
    }

    // 현재 코인갯수 변경
    public static class ChangeUserCoinDto {
        @SerializedName("curUserCoin")
        private Integer curUserCoin;

        @SerializedName("newUserCoin")
        private Integer newUserCoin;

        public Integer getCurUserCoin() {
            return curUserCoin;
        }

        public void setCurUserCoin(Integer curUserCoin) {
            this.curUserCoin = curUserCoin;
        }

        public Integer getNewUserCoin() {
            return newUserCoin;
        }

        public void setNewUserCoin(Integer newUserCoin) {
            this.newUserCoin = newUserCoin;
        }
    }

    
}
