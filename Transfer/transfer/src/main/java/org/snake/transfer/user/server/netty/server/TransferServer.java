package org.snake.transfer.user.server.netty.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snake.transfer.command.zookeeper.ServersImpl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;


public class TransferServer extends Thread {
	
	public static final Logger logger = LogManager.getLogger("Server");
	
	public static final int PORT = 9998;
	private static final int BOSS_THREADS = 1;
	
	private static final boolean IS_DEBUG = true;
	
	private EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_THREADS);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private TransferServer() {
		
	}
	
    private static class ServerInstance{
        private static final TransferServer instance = new TransferServer();
    }
    
    /*
     * singleton instance
     */
    public static TransferServer getInstance(){
        return ServerInstance.instance;
    }
    
    
	@Override
	public void run() {

        try {
//        	
//        	if(IS_DEBUG)
//        		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        	
            ServerBootstrap bootstrap = new ServerBootstrap();
            
            bootstrap.group(bossGroup, workerGroup)
             		 .channel(NioServerSocketChannel.class)
             		 // .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
             		 // .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
             		 .childHandler(new TransferServerInitializer())
            
             		 // 某个服务器进程占用了TCP的80端口进行监听，此时再次监听该端口就会返回错误，
             		 // 使用该参数就可以解决问题，该参数允许共用该端口
             		 .option(ChannelOption.SO_REUSEADDR, true) 
             		 
             		 // 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，
             		 // 多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
             		 .option(ChannelOption.SO_BACKLOG, 1024)
             		              		 
             		 // 该参数用于设置TCP连接，当设置该选项以后，连接会测试链接的状态，这个选项用于可能长时间没有数据交流的
             		 // 连接。当设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文。
             		 .childOption(ChannelOption.SO_KEEPALIVE, true)
             		 
             		 // Nagle算法是将小的数据包组装为更大的帧然后进行发送，而不是输入一次发送一次,
             		 // 因此在数据包不足的时候会等待其他数据的到了，组装成大的数据包进行发送，虽然该方式有效提高网络的有效
             		 // 负载，但是却造成了延时，而该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输
             		 .childOption(ChannelOption.TCP_NODELAY, true);
             		 

            ChannelFuture future =  bootstrap.bind(PORT).sync();
            
            logger.trace("Server: netty server started with: BOSS_THREADS={}, PORT={}, IS_DEBUG={}", BOSS_THREADS, PORT, IS_DEBUG);
                        
            future.channel().closeFuture().sync();
       
        } catch (Exception e) {

        	logger.error("Server: netty server will shutdown with error: {}", e.getMessage());
        	
			e.printStackTrace();
		} finally {
			
			ServersImpl.getInstance().stop();
        }
		
	}
	
	public void shutdown() {
		
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
        
		logger.error("Server: netty server shutdown");
		
	}


}
