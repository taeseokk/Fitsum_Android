package org.fitsum.Dto;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class BoardDto {
    // 게시글 작성 (BoardActivity)
    public static class BoardCreateDto {

        @SerializedName("user_no")
        private Long user_no;

        @SerializedName("title")
        private String title;

        @SerializedName("content")
        private String content;




        public Long getUser_no() {
            return user_no;
        }

        public void setUser_no(Long user_no) {
            this.user_no = user_no;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        @Override
        public String toString() {
            return "BoardCreateDto{" +
                    "user_no=" + user_no +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    //게시글 확인(Fragment 3)
    public static class BoardViewDto{

        @SerializedName("boardId")          //프라이머리 키 정수형 아이디
        private Long boardId;

        @SerializedName("userId")           //유저 아이디
        private String userId;

        @SerializedName("loginId")          //?
        private String loginId;

        @SerializedName("nickName")         //닉네임
        private String nickName;

        @SerializedName("title")            //제목
        private String title;

        @SerializedName("content")          //내용
        private String content;

        @SerializedName("liked")            //좋아요
        private Boolean liked;

        @SerializedName("writeDate")        //작성날짜
        private String writeDate;


        public Long getboardId() {
            return boardId;
        }

        public void setboardId(Long boardId) {
            this.boardId = boardId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Boolean getLiked() {
            return liked;
        }

        public void setLiked(Boolean liked) {
            this.liked = liked;
        }

        public Long getBoardId() {
            return boardId;
        }

        public void setBoardId(Long boardId) {
            this.boardId = boardId;
        }

        public String getWriteDate() {
            return writeDate;
        }

        public void setWriteDate(String writeDate) {
            this.writeDate = writeDate;
        }

        @Override
        public String toString() {
            return "BoardViewDto{" +
                    "boardId=" + boardId +
                    ", userId='" + userId + '\'' +
                    ", loginId='" + loginId + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", liked=" + liked +
                    ", writeDate=" + writeDate +
                    '}';
        }
    }

    // 일기 수정
    public static class updateBoard{

        @SerializedName("content")
        private String content;

        @SerializedName("open")
        private Boolean open;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Boolean getOpen() {
            return open;
        }

        public void setOpen(Boolean open) {
            this.open = open;
        }

        //@@@@@@@@@@@@@@@@@@@@  toString   --------------------------
        @Override
        public String toString() {
            return "updateDiary{" +
                    "content='" + content + '\'' +
                    ", open=" + open +
                    '}';
        }

    }

    // 일기 삭제
    public static class deleteBoard{

    }



}
