package com.imooc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.BGMOperatorTypeEnum;
import com.imooc.mapper.BgmMapper;
import com.imooc.mapper.UsersReportMapperCustom;
import com.imooc.mapper.VideosMapper;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.Videos;
import com.imooc.pojo.vo.Reports;
import com.imooc.service.VideoService;
import com.imooc.utils.PagedResult;
import com.imooc.web.util.ZKCurator;

@Service
public class VideoServiceImpl implements VideoService {
	@Autowired
	private BgmMapper bgmMapper;

	@Autowired
	private Sid sid;
	
	@Autowired
	private UsersReportMapperCustom usersReportMapperCustom;
	
	@Autowired
	private VideosMapper videosMapper;
	
	@Autowired
	private ZKCurator zKCurator;
	@Override
	public void addBgm(Bgm bgm) {
		String id = sid.nextShort();
		bgm.setId(id);
		bgmMapper.insert(bgm);
		Map<String, String> map = new HashMap<>();
		map.put("operType", BGMOperatorTypeEnum.ADD.type);
		map.put("path", bgm.getPath());
		zKCurator.sendBgmOperator(id, JSONUtils.toJSONString(map));
	}
	@Override
	public PagedResult queryBgmList(Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<Bgm> list = bgmMapper.selectByExample(null);
		PageInfo<Bgm> pageList = new PageInfo<Bgm>(list);
		PagedResult result = new PagedResult();
		result.setTotal(pageList.getPages());
		result.setRows(list);
		result.setPage(page);
		result.setRecords(pageList.getTotal());
		return result;
	}
	@Override
	public void deleteBgm(String id) {
		Bgm bgm = bgmMapper.selectByPrimaryKey(id);
		
		bgmMapper.deleteByPrimaryKey(id);
		Map<String, String> map = new HashMap<>();
		map.put("operType", BGMOperatorTypeEnum.DELETE.type);
		map.put("path", bgm.getPath());
		zKCurator.sendBgmOperator(id, JSONUtils.toJSONString(map));
	}
	@Override
	public PagedResult queryReportList(Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);

		List<Reports> reportsList = usersReportMapperCustom.selectAllVideoReport();

		PageInfo<Reports> pageList = new PageInfo<Reports>(reportsList);

		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(reportsList);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
	}
	
	@Override
	public void updateVideoStatus(String videoId, Integer status) {
		
		Videos video = new Videos();
		video.setId(videoId);
		video.setStatus(status);
		videosMapper.updateByPrimaryKeySelective(video);
	}

	
}
