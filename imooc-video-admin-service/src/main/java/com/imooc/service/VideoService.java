package com.imooc.service;

import com.imooc.pojo.Bgm;
import com.imooc.utils.PagedResult;

public interface VideoService {
	void addBgm(Bgm bgm);
	PagedResult queryBgmList(Integer page, Integer pageSize);
	void deleteBgm(String id);
	PagedResult queryReportList(Integer page, Integer pageSize);
	void updateVideoStatus(String videoId, Integer status);
}
