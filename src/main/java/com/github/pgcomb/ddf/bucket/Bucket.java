package com.github.pgcomb.ddf.bucket;

import com.github.pgcomb.ddf.common.StopForward;

/**
 * 数据桶
 *
 * @author 王东旭
 * @date 2018-07-06
 */
public interface Bucket extends StopForward {

    /**
     * 倾倒数据
     */
    void topple();

}
