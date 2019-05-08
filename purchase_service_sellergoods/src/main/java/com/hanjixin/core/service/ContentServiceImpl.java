package com.hanjixin.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hanjixin.core.dao.ad.ContentDao;
import com.hanjixin.core.pojo.ad.Content;
import com.hanjixin.core.pojo.ad.ContentQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private ContentDao contentDao;

	@Override
	public List<Content> findAll() {
		List<Content> list = contentDao.selectByExample(null);
		return list;
	}

	@Override
	public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<Content> page = (Page<Content>)contentDao.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void add(Content content) {
		contentDao.insertSelective(content);
	}

	@Autowired
	private RedisTemplate redisTemplate;
	@Override
	public void edit(Content content) {
		//1:判断当前广告对象中的广告类型ID 是不是已经修改了
		//根据当前广告对象的ID 查询数据库中广告类型 跟当前页面传递过来的广告中的类型比对
		Content c = contentDao.selectByPrimaryKey(content.getId());
		if(c.getCategoryId()!=content.getCategoryId()){
			redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		}
		redisTemplate.boundHashOps("content").delete(c.getCategoryId());
		contentDao.updateByPrimaryKeySelective(content);
	}

	@Override
	public Content findOne(Long id) {
		Content content = contentDao.selectByPrimaryKey(id);
		return content;
	}

	@Override
	public void delAll(Long[] ids) {
		if(ids != null){
			for(Long id : ids){
				contentDao.deleteByPrimaryKey(id);
			}
		}
	}

    @Override
    public List<Content> findByCategoryId(Long categoryId) {
		//1:查询缓存
		List<Content> contentList=(List<Content>)redisTemplate.boundHashOps("content").get(categoryId);
		//2:没有 查询数据库 保存缓存一份 （存活时间）
		if(contentList==null||contentList.size()==0){
			ContentQuery contentQuery = new ContentQuery();
			contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
			contentQuery.setOrderByClause("sort_order desc");
			contentList= contentDao.selectByExample(contentQuery);
			//保存一份到缓存中
			redisTemplate.boundHashOps("content").put(categoryId,contentList);
			redisTemplate.boundHashOps("content").expire(4, TimeUnit.HOURS);
		}
		System.out.println(redisTemplate.boundHashOps("content").getExpire());
		return contentList;
    }

}