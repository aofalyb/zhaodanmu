package com.zhaodanmu.douyu.server;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RoomDetail {


        /**
         * room_id : 911
         * gift : [{"gx":20000,"pc":2000,"mimg":"http://gfs-op.douyucdn.cn/dygift/2018/10/25/3def9ef3cad9ce370e136570562beb1b.png","intro":"巅峰之战，星耀荣光！","name":"盛典星耀超火","id":"2051","himg":"http://gfs-op.douyucdn.cn/dygift/2018/10/25/1d1f1d880b1b06300d57c1be09c3da9e.gif","type":"2","desc":"赠送网站广播并派送鱼丸宝箱"},{"gx":5000,"pc":500,"mimg":"http://gfs-op.douyucdn.cn/dygift/2018/10/25/22fa2680917a67d38b4cf882ecebe1be.png","intro":"我们的征途是年度巅峰！","name":"盛典火箭","id":"2050","himg":"http://gfs-op.douyucdn.cn/dygift/2018/10/25/8efe57836755112788ffbceb48640446.gif","type":"2","desc":"赠送网站广播并派送出神秘宝箱"},{"gx":1000,"pc":100,"mimg":"http://gfs-op.douyucdn.cn/dygift/1809/dbe2902cc30b20b73805e7750ebec470.png","intro":"主播带我直飞盛典","name":"盛典飞机","id":"1952","himg":"http://gfs-op.douyucdn.cn/dygift/1809/71c13122257fd4b667404a0a13aecf04.gif","type":"2","desc":"赠送房间广播并赠送道具宝箱"},{"gx":60,"pc":6,"mimg":"http://gfs-op.douyucdn.cn/dygift/1809/dd4114f67a0f5cda41936378c69bd143.png","intro":"老板，来张盛典VIP金卡吧！","name":"盛典办卡","id":"1951","himg":"http://gfs-op.douyucdn.cn/dygift/1809/9c103a5dc4b4f920dda1248ee03a140e.gif","type":"2","desc":""},{"gx":2,"pc":0.2,"mimg":"http://gfs-op.douyucdn.cn/dygift/2018/10/24/9d0695e765c5f104b3dd4d00a2353ab1.png","intro":"冲鸭冲鸭冲鸭","name":"冲鸭","id":"2048","himg":"http://gfs-op.douyucdn.cn/dygift/2018/10/24/04182c0f6eb845106b586c3bef99fc3c.gif","type":"2","desc":""},{"gx":1,"pc":0.1,"mimg":"http://gfs-op.douyucdn.cn/dygift/1809/efceffc8d33f16ce302035d256b17b02.png","intro":"UPUP","name":"典赞","id":"1949","himg":"http://gfs-op.douyucdn.cn/dygift/1809/e5f3dc0e580a5c916c2a45aea1fb9c73.gif","type":"2","desc":""},{"gx":1,"pc":100,"mimg":"http://gfs-op.douyucdn.cn/dygift/1809/a28f20c08c2f7c06ee9a5c662ec6f7a3.png","intro":"定制水晶鱼丸","name":"特供鱼丸","id":"1948","himg":"http://gfs-op.douyucdn.cn/dygift/1809/f6c79291381fc40fd19c4baa8422c19d.gif","type":"1","desc":""},{"gx":10,"pc":1,"mimg":"http://gfs-op.douyucdn.cn/dygift/1808/1b5908f436be1fdcca0e152c8e09c081.png","intro":"贡献+1 经验+1 亲密度+1","name":"小飞碟","id":"1859","himg":"http://gfs-op.douyucdn.cn/dygift/1808/86836850f26b97523b70800382d22a01.gif","type":"2","desc":"送1个小飞碟可获赠1张探险券"}]
         * owner_name : 骚白
         * hn : 0
         * room_thumb : https://rpic.douyucdn.cn/asrpic/181103/911_0041_0.jpg/dy1
         * cate_id : 181
         * avatar : https://apic.douyucdn.cn/upload/avanew/face/201801/25/00/f4412efb21d9f7a8f7dffbe54034b2f7_big.jpg
         * fans_num : 10501477
         * room_status : 2
         * owner_weight : 0
         * room_name : 骚白三排系列
         * start_time : 2018-11-02 19:12:44
         * cate_name : 王者荣耀
         * online : 0
         */
        private String room_id;
        private List<GiftEntity> gift;
        private String owner_name;
        private int hn;
        private String room_thumb;
        private String cate_id;
        private String avatar;
        private String fans_num;
        private String room_status;
        private String owner_weight;
        private String room_name;
        private String start_time;
        private String cate_name;
        private int online;
        private Map<String,GiftEntity> giftMap;

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public void setGift(List<GiftEntity> gift) {
            this.gift = gift;
        }

        public void setOwner_name(String owner_name) {
            this.owner_name = owner_name;
        }

        public void setHn(int hn) {
            this.hn = hn;
        }

        public void setRoom_thumb(String room_thumb) {
            this.room_thumb = room_thumb;
        }

        public void setCate_id(String cate_id) {
            this.cate_id = cate_id;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setFans_num(String fans_num) {
            this.fans_num = fans_num;
        }

        public void setRoom_status(String room_status) {
            this.room_status = room_status;
        }

        public void setOwner_weight(String owner_weight) {
            this.owner_weight = owner_weight;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getRoom_id() {
            return room_id;
        }

        public List<GiftEntity> getGift() {
            return gift;
        }

        public String getOwner_name() {
            return owner_name;
        }

        public int getHn() {
            return hn;
        }

        public String getRoom_thumb() {
            return room_thumb;
        }

        public String getCate_id() {
            return cate_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getFans_num() {
            return fans_num;
        }

        public String getRoom_status() {
            return room_status;
        }

        public String getOwner_weight() {
            return owner_weight;
        }

        public String getRoom_name() {
            return room_name;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getCate_name() {
            return cate_name;
        }

        public int getOnline() {
            return online;
        }

        public GiftEntity getGiftInfo(long giftId) {
            if(giftMap != null) {
                return giftMap.get(String.valueOf(giftId));
            }
            giftMap = getGift().stream().collect(Collectors.toMap((GiftEntity::getId), a -> a));
            return getGiftInfo(giftId);
        }



        public class GiftEntity {
            /**
             * gx : 20000
             * pc : 2000
             * mimg : http://gfs-op.douyucdn.cn/dygift/2018/10/25/3def9ef3cad9ce370e136570562beb1b.png
             * intro : 巅峰之战，星耀荣光！
             * name : 盛典星耀超火
             * id : 2051
             * himg : http://gfs-op.douyucdn.cn/dygift/2018/10/25/1d1f1d880b1b06300d57c1be09c3da9e.gif
             * type : 2
             * desc : 赠送网站广播并派送鱼丸宝箱
             */
            private int gx;
            private int pc;
            private String mimg;
            private String intro;
            private String name;
            private String id;
            private String himg;
            private String type;
            private String desc;

            public void setGx(int gx) {
                this.gx = gx;
            }

            public void setPc(int pc) {
                this.pc = pc;
            }

            public void setMimg(String mimg) {
                this.mimg = mimg;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setHimg(String himg) {
                this.himg = himg;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getGx() {
                return gx;
            }

            public int getPc() {
                return pc;
            }

            public String getMimg() {
                return mimg;
            }

            public String getIntro() {
                return intro;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }

            public String getHimg() {
                return himg;
            }

            public String getType() {
                return type;
            }

            public String getDesc() {
                return desc;
            }
        }




}
