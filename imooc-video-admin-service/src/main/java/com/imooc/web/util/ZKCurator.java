package com.imooc.web.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKCurator {
	//zk客户端
	private CuratorFramework client = null;
	
	final static Logger log = LoggerFactory.getLogger(ZKCurator.class);
 
	public ZKCurator(CuratorFramework client) {
		this.client = client;
	}
	
	public void init() {
		client = client.usingNamespace("admin");
		try {
			//判断在admin命名空间下是否有bgm节点 /admin/bgm
			if( client.checkExists().forPath("/bgm") == null ) {
				//对于zk来讲，有两种类型的节点，一种是持久节点（永久存在，除非手动删除），另一种是临时节点（会话断开，自动删除）
				client.create().creatingParentContainersIfNeeded()
				.withMode(CreateMode.PERSISTENT) //持久节点
				.withACL(Ids.OPEN_ACL_UNSAFE)  //匿名权限
				.forPath("/bgm"); 
				log.info("zookeeper客户端连接初始化成功");
				log.info("zookeeper服务端状态：{}",client.isStarted());
			}
		} catch (Exception e) {
			log.error("zookeeper客户端连接初始化失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 增加或者删除Bgm，向zk-server创建子节点，供小程序后端监听
	 * @param bgmId
	 * @param operType
	 */
	public void sendBgmOperator(String bgmId, String operObject) {
		try {
			client.create().creatingParentContainersIfNeeded()
			.withMode(CreateMode.PERSISTENT) //持久节点
			.withACL(Ids.OPEN_ACL_UNSAFE)  //匿名权限
			.forPath("/bgm/" + bgmId, operObject.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
