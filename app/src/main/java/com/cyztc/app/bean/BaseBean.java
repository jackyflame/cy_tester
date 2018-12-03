package com.cyztc.app.bean;




import com.cyztc.app.utils.BeanUtil;

import java.io.Serializable;

/**
 * Created by ywl on 2016/1/30.
 */
public class BaseBean implements Serializable {

    public static final long serialVersionUID = -316172390920775219L;

    @Override
    public String toString() {
        return BeanUtil.bean2string(this);
    }
}
