package com.chinatour.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chinatour.entity.News;

/**
 * 同行首页news
 * @author chinatour
 *
 */
@Repository
public interface NewsMapper extends BaseMapper<News, String> {
	public List<News> findNewsForPage(int startPage);
	public int findNewsForPageCount();
	public List<News> findNewsList(News n);
}
