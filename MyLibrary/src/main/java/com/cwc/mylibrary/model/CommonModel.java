package com.cwc.mylibrary.model;

import java.util.List;

/**
 * Created by MarkFrank01
 * description :
 */
public class CommonModel {

    /**
     * status : success
     * info : 获取信息成功
     * data : [{"ad_id":"188","title":"款式34","link_url":"http://baidu.com","photo":"http://yimeiguanjian.oss-cn-shenzhen.aliyuncs.com/images/5ab368bc076be.jpg"},{"ad_id":"187","title":"款式3","link_url":"http://www.cyiwei.com/","photo":"http://yimeiguanjian.oss-cn-shenzhen.aliyuncs.com/images/5ab367e9c5a71.jpg"},{"ad_id":"186","title":"123","link_url":"http://baidu.com","photo":"http://yimeiguanjian.oss-cn-shenzhen.aliyuncs.com/images/5ab367f919e02.jpg"},{"ad_id":"216","title":"广告","link_url":"http://www.baidu.com/","photo":"http://yimeiguanjian.oss-cn-shenzhen.aliyuncs.com/images/5ab61471de5f1.jpg"}]
     */

    private String status;
    private String info;
    private Object data;

    public CommonModel(){}

    public CommonModel(String data){
        this.data=data;
    }

    public String  getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
