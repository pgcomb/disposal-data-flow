package com.github.pgcomb.ddf.sort;

import com.github.pgcomb.ddf.common.WorkCallBack;
import com.github.pgcomb.ddf.common.packagee.InMemoryDataPackage;
import com.github.pgcomb.ddf.exception.WorkSuccessCallBackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * 排序工作单元
 * @param <T> 排序数据
 * @author 王东旭
 */
@SuppressWarnings("unchecked")
public class SortWorker<T extends InMemoryDataPackage<? extends Comparable>> implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SortWorker.class);

    private T bag;

    private WorkCallBack<T> callBack;

    /**
     * 遇到异常停止
     */
    private Consumer<PassiveSorterManager> errorStop;

    public SortWorker(T bag,WorkCallBack<T> callBack,Consumer errorStop) {
        this.bag = bag;
        this.callBack = callBack;
        this.errorStop = errorStop;
    }

    @Override
    public void run() {
        LocalDateTime start = LocalDateTime.now();
        Collections.sort(bag.getData());
        log.info("sort package[serialNumber:{},start:{},end{},size{}]:{}",bag.serialNumber(),
                bag.start(),bag.end(),bag.size(), Duration.between(start,LocalDateTime.now()));
        try {
            callBack.success(bag);
        } catch (WorkSuccessCallBackException e) {
            log.error("sort success call back exception",e);
            errorStop.accept(null);
        }
    }
}
