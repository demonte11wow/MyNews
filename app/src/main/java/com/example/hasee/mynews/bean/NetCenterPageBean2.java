package com.example.hasee.mynews.bean;

import java.util.List;

/**
 * Created by lzq on 2016/10/17.
 */
public class NetCenterPageBean2 {
    private List<NewsCenterPagerData> data;
    private List extend;
    private int recode;

    public List<NewsCenterPagerData> getData() {
        return data;
    }

    public void setData(List<NewsCenterPagerData> data) {
        this.data = data;
    }

    public List getExtend() {
        return extend;
    }

    public void setExtend(List extend) {
        this.extend = extend;
    }

    public int getRecode() {
        return recode;
    }

    public void setRecode(int recode) {
        this.recode = recode;
    }

    public static class NewsCenterPagerData {
        private List<ChildrenData>children;
        private int id;
        private String title;
        private  int type;
        private String url;
        private String url1;
        private String dayurl;
        private String excurl;
        private String weekurl;

        public List<ChildrenData> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenData> children) {
            this.children = children;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getDayurl() {
            return dayurl;
        }

        public void setDayurl(String dayurl) {
            this.dayurl = dayurl;
        }

        public String getExcurl() {
            return excurl;
        }

        public void setExcurl(String excurl) {
            this.excurl = excurl;
        }

        public String getWeekurl() {
            return weekurl;
        }

        public void setWeekurl(String weekurl) {
            this.weekurl = weekurl;
        }

        public static class ChildrenData {
            private int id;
            private String title;
            private  int type;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
