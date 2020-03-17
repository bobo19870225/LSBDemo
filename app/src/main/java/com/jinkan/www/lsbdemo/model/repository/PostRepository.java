package com.jinkan.www.lsbdemo.model.repository;

/**
 * Created by Sampson on 2019/2/26.
 * FastAndroid
 */
public interface PostRepository<T> {
    Listing<T> post(Integer pageSize);
}
