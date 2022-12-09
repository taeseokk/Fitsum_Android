package org.fitsum.Dto;

import com.google.gson.annotations.SerializedName;

public class BadgeDto {

    public static class CreateBadgeDto{
        @SerializedName("user_no")
        private Long user_no;

        @SerializedName("badge1")
        private Boolean badge1;




        public Long getUser_no() {
            return user_no;
        }
        public void setUser_no(Long user_no) {
            this.user_no = user_no;
        }

        public Boolean getBadge1() {
            return badge1;
        }
        public void setBadge1(Boolean badge1) {
            this.badge1 = badge1;
        }

        @Override
        public String toString() {
            return "CreateBadgeDto{" +
                    "user_no=" + user_no +
                    ", badge1=" + badge1 +
                    '}';
        }
    }

    public static class BadgeViewDto{

        @SerializedName("badgeId")
        private Integer badgeId;

        @SerializedName("badge1")
        private boolean badge1;

        @SerializedName("badge2")
        private boolean badge2;

        @SerializedName("badge3")
        private boolean badge3;

        @SerializedName("badge4")
        private boolean badge4;

        @SerializedName("badge5")
        private boolean badge5;

        @SerializedName("badge6")
        private boolean badge6;

        public Integer getBadgeId() {
            return badgeId;
        }

        public boolean isBadge1() {
            return badge1;
        }

        public void setBadge1(boolean badge1) {
            this.badge1 = badge1;
        }

        public boolean isBadge2() {
            return badge2;
        }

        public void setBadge2(boolean badge2) {
            this.badge2 = badge2;
        }

        public boolean isBadge3() {
            return badge3;
        }

        public void setBadge3(boolean badge3) {
            this.badge3 = badge3;
        }

        public boolean isBadge4() {
            return badge4;
        }

        public void setBadge4(boolean badge4) {
            this.badge4 = badge4;
        }

        public boolean isBadge5() {
            return badge5;
        }

        public void setBadge5(boolean badge5) {
            this.badge5 = badge5;
        }

        public boolean isBadge6() {
            return badge6;
        }

        public void setBadge6(boolean badge6) {
            this.badge6 = badge6;
        }

        // 뱃지 2변경



        @Override
        public String toString() {
            return "BadgeViewDto{" +
                    "badgeId=" + badgeId +
                    ", badge1=" + badge1 +
                    ", badge2=" + badge2 +
                    ", badge3=" + badge3 +
                    ", badge4=" + badge4 +
                    ", badge5=" + badge5 +
                    ", badge6=" + badge6 +
                    '}';
        }
    }

    //첫 운동을 활성화 시키기 위한
    public static class ChangeBadge2 {
        @SerializedName("curBadge2")
        private Boolean curBadge2;

        @SerializedName("newBadge2")
        private Boolean newBadge2;

        public Boolean getcurBadge2() {
            return curBadge2;
        }

        public void setcurBadge2(Boolean curBadge2) {
            this.curBadge2 = curBadge2;
        }

        public Boolean getnewBadge2() {
            return newBadge2;
        }

        public void setnewBadge2(Boolean newBadge2) {
            this.newBadge2 = newBadge2;
        }
    }

    //첫 구매를 활성화 시키기 위한
    public static class ChangeBadge3 {
        @SerializedName("curBadge3")
        private Boolean curBadge3;

        @SerializedName("newBadge3")
        private Boolean newBadge3;

        public Boolean getCurBadge3() {
            return curBadge3;
        }

        public void setCurBadge3(Boolean curBadge3) {
            this.curBadge3 = curBadge3;
        }

        public Boolean getNewBadge3() {
            return newBadge3;
        }

        public void setNewBadge3(Boolean newBadge3) {
            this.newBadge3 = newBadge3;
        }
    }
}