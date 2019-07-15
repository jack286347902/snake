package org.snake.zookeeper.zookeeper;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.snake.zookeeper.manager.zookeeper.ServerData;
import org.snake.zookeeper.manager.zookeeper.ZkManager;
import org.snake.zookeeper.servers.Servers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * zookeeper客户端
 */

@Configuration()
@EnableConfigurationProperties({ZookeeperProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
        value = {"zookeeper.enabled"},
        matchIfMissing = false
)
@Log4j2
public class ZkClient {
	
	public static final String ROOT_PATH = "/";

    private CuratorFramework client;
    private TreeCache cache;
    
    private final ZookeeperProperties zookeeperProperties;
    
    @Setter
    private Servers servers;
    
    @Setter
    private ZkManager zkManager;
            
    @Autowired
    public ZkClient(ZookeeperProperties zookeeperProperties){
        this.zookeeperProperties = zookeeperProperties;
    }

    /**
     * 初始化zookeeper客户端
     */
    public void start() {
    	
    	try{
    		
    		RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                    zookeeperProperties.getMaxRetries());
    		
            Builder builder   = CuratorFrameworkFactory.builder()
				                    .connectString(zookeeperProperties.getServer())
				                    .retryPolicy(retryPolicy)
				                    .sessionTimeoutMs( zookeeperProperties.getSessionTimeoutMs())
				                    .connectionTimeoutMs( zookeeperProperties.getConnectionTimeoutMs())
				                    .namespace( zookeeperProperties.getNamespace());
            
            if(StringUtils.isNotEmpty( zookeeperProperties.getDigest())){
            	
            	builder.authorization("digest", zookeeperProperties.getDigest().getBytes("UTF-8"));
                builder.aclProvider(new ACLProvider() {
//                    @Override
                    public List<ACL> getDefaultAcl() {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }

//                    @Override
                    public List<ACL> getAclForPath(final String path) {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }
                });
            }
            
            client = builder.build();
            client.start();
            
            initLocalCache(ROOT_PATH);
            
	        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
				public void stateChanged(CuratorFramework curator, ConnectionState state) {
					if (state == ConnectionState.LOST) {
						//连接丢失
						log.info("lost session with zookeeper");
					} else if (state == ConnectionState.CONNECTED) {
						//连接新建
						log.info("connected with zookeeper");
						
						zkManager.registerSelf();
						
					} else if (state == ConnectionState.RECONNECTED) {
						log.info("reconnected with zookeeper");
						
						zkManager.registerSelf();
					}
				}
	        });
	        
	        
    	}catch(Exception e){
    		e.printStackTrace();
    		
    		servers.stop();
    	}
    }
    
    

    /**
     * 初始化本地缓存
     * @param watchRootPath
     * @throws Exception
     */
    private void initLocalCache(String watchRootPath) throws Exception {
    	    	
        cache = new TreeCache(client, watchRootPath);
        
        TreeCacheListener listener = new TreeCacheListener() {
			
//			@Override
			public void childEvent(CuratorFramework curator, TreeCacheEvent event) throws Exception {
				// TODO Auto-generated method stub
				ChildData data = event.getData();
                if(data != null){
                	
                	String jsonData = null;
                	
                    switch (event.getType()) {
                    
                    case CONNECTION_RECONNECTED:

                    case NODE_ADDED:
                    	
                    	jsonData = new String(data.getData());
                    	                    	
                    	if(jsonData.startsWith("{")) {
                    	
	                        JSONObject jsonObject = JSONObject.fromObject(jsonData);
	                        ServerData serverData = (ServerData)JSONObject.toBean(jsonObject, ServerData.class);
	
	                        zkManager.addServer(serverData);
                    		
                    	}
                    	
                    	log.info("[TreeCache]节点增加, path={}, data={}"+ data.getPath()+ jsonData);
                        break;
                        
                    case NODE_REMOVED:
                    	
                    	jsonData = new String(data.getData());
                    	
                    	if(jsonData.startsWith("{")) {
                    	
	                        JSONObject jsonObject = JSONObject.fromObject(jsonData);
	                        ServerData serverData = (ServerData)JSONObject.toBean(jsonObject, ServerData.class);
	
	                        zkManager.serverRemoved(serverData);
                    		
                    	}
                    	
                    	log.info("[TreeCache]节点删除, path={}, data={}"+ data.getPath()+ jsonData);
                    	

                    default:
                        break;
                    }
                }else{
                	log.info("[TreeCache]节点数据为空, path={}");
                }
			}
		};
         
        cache.getListenable().addListener(listener);
        cache.start();
    }


    public void stop() {
    	
    	if(null != client)
    		client.close();
    	
    	if(null != cache)
    		cache.close();
    }

    public CuratorFramework getClient() {
        return client;
    }


    /**
     * 创建节点
     * @param mode       节点类型
     *                   1、PERSISTENT 持久化目录节点，存储的数据不会丢失。
     *                   2、PERSISTENT_SEQUENTIAL顺序自动编号的持久化目录节点，存储的数据不会丢失
     *                   3、EPHEMERAL临时目录节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除
     *                   4、EPHEMERAL_SEQUENTIAL临时自动编号节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除，并且根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名。
     * @param path  节点名称
     * @param nodeData  节点数据
     */
    public void createNode(CreateMode mode, String path , String nodeData) {
        try {
            //使用creatingParentContainersIfNeeded()之后Curator能够自动递归创建所有所需的父节点
            client.create().creatingParentsIfNeeded().withMode(mode).forPath(path,nodeData.getBytes("UTF-8"));
        } catch (Exception e) {
            log.error("注册出错", e);
        }
    }

    /**
     * 创建节点
     * @param mode       节点类型
     *                   1、PERSISTENT 持久化目录节点，存储的数据不会丢失。
     *                   2、PERSISTENT_SEQUENTIAL顺序自动编号的持久化目录节点，存储的数据不会丢失
     *                   3、EPHEMERAL临时目录节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除
     *                   4、EPHEMERAL_SEQUENTIAL临时自动编号节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除，并且根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名。
     * @param path  节点名称
     */
    public void createNode(CreateMode mode,String path ) {
        try {
            //使用creatingParentContainersIfNeeded()之后Curator能够自动递归创建所有所需的父节点
            client.create().creatingParentsIfNeeded().withMode(mode).forPath(path);
        } catch (Exception e) {
            log.error("注册出错", e);
        }
    }

    /**
     * 删除节点数据
     *
     * @param path
     */
    public void deleteNode(final String path) {
        try {
            deleteNode(path,true);
        } catch (Exception ex) {
            log.error("{}",ex);
        }
    }


    /**
     * 删除节点数据
     * @param path
     * @param deleteChildre   是否删除子节点
     */
    public void deleteNode(final String path,Boolean deleteChildre){
        try {
            if(deleteChildre){
                //guaranteed()删除一个节点，强制保证删除,
                // 只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功
                client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            }else{
                client.delete().guaranteed().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置指定节点的数据
     * @param path
     * @param datas
     */
    public void setNodeData(String path, byte[] datas){
        try {
            client.setData().forPath(path, datas);
        }catch (Exception ex) {
            log.error("{}",ex);
        }
    }

    /**
     * 获取指定节点的数据
     * @param path
     * @return
     */
    public byte[] getNodeData(String path){

        try {
            if(cache != null){
                ChildData data = cache.getCurrentData(path);
                if(data != null){
                    return data.getData();
                }
            }
            
            return client.getData().forPath(path);
        }catch (Exception ex) {
            log.error("{}",ex);
        }
        return null;
    }

    /**
     * 获取数据时先同步
     * @param path
     * @return
     */
    public byte[] synNodeData(String path){
        client.sync();
        return getNodeData( path);
    }

    /**
     * 判断路径是否存在
     *
     * @param path
     * @return
     */
    public boolean isExistNode(final String path) {
        client.sync();
        try {
            return null != client.checkExists().forPath(path);
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * 获取节点的子节点
     * @param path
     * @return
     */
    public List<String> getChildren(String path) {
        List<String> childrenList = new ArrayList<String>();
        try {
            childrenList = client.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("获取子节点出错", e);
        }
        return childrenList;
    }

    /**
	 * 随机读取一个path子路径, "/"为根节点对应该namespace
	 * 先从cache中读取，如果没有，再从zookeeper中查询
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public String getRandomData(String path)  {
		try{
			Map<String,ChildData> cacheMap = cache.getCurrentChildren(path);
			if(cacheMap != null && cacheMap.size() > 0) {
				log.debug("get random value from cache,path="+path);
				Collection<ChildData> values = cacheMap.values();
				List<ChildData> list = new ArrayList<ChildData>(values);
				Random rand = new Random();
				byte[] b = list.get(rand.nextInt(list.size())).getData();
				return new String(b,"utf-8");
			}
			if(isExistNode(path)) {
				log.debug("path [{}] is not exists,return null",path);
				return null;
			} else {
				log.debug("read random from zookeeper,path="+path);
				List<String> list = client.getChildren().forPath(path);
				if(list == null || list.size() == 0) {
					log.debug("path [{}] has no children return null",path);
					return null;
				}
				Random rand = new Random();
				String child = list.get(rand.nextInt(list.size()));
				path = path + "/" + child;
				byte[] b = client.getData().forPath(path);
				String value = new String(b,"utf-8");
				return value;
			}
		}catch(Exception e){
			log.error("{}",e);
		}
		return null;

	}

	/**
	 * 可重入共享锁  -- Shared Reentrant Lock
	 * @param lockPath
	 * @param time
	 * @param dealWork 获取
	 * @return
	 */
//	public Object getSRLock(String lockPath,long time, SRLockDealCallback<?> dealWork){
//		InterProcessMutex lock = new InterProcessMutex(client, lockPath);
//		try {
//			if (!lock.acquire(time, TimeUnit.SECONDS)) {
//	            log.error("get lock fail:{}", " could not acquire the lock");
//	            return null;
//	        }
//            log.debug("{} get the lock",lockPath);
//            Object b = dealWork.deal();
//            return b;
//        }catch(Exception e){
//        	log.error("{}", e);
//        }finally{
//        	try {
//				lock.release();
//			} catch (Exception e) {
//				//log.error("{}",e);
//			}
//        }
//		return null;
//	}

    /**
     * 获取读写锁
     * @param path
     * @return
     */
	public InterProcessReadWriteLock getReadWriteLock(String path){
        InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(client, path);
        return readWriteLock;
    }

    /**
     * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
     */
    ExecutorService pool = Executors.newFixedThreadPool(2);

    /**
     * 监听数据节点的变化情况
     * @param watchPath
     * @param listener
     */
    public void watchPath(String watchPath,TreeCacheListener listener){
     //   NodeCache nodeCache = new NodeCache(client, watchPath, false);
        TreeCache cache = new TreeCache(client, watchPath);
        cache.getListenable().addListener(listener,pool);
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}