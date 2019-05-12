# snake a netty nio game server framework.

MessageCreator 是一个Java对象池，自动生成对象，用户要手动调用release()来归还对象到对象池


1. 支持的类型: 
	
	1. 基本类型
	2. 自定义类型

		消息类型-------------to------------java 类型


		required double 	dd;    ---------------------- double
		
		required float 	ff;	----------------------        float
		
		required int8 	i8;	----------------------        byte/char
		
		required int16 	i16;----------------------        short
		
		required int32 	i32;----------------------        int
		
		required int64 	i64;----------------------        long
		
		required bool 	isArray;----------------------    boolean
		
		required string 	name;----------------------   String
		
		repeated string 	l6;----------------------     String[]
	
	
	3. 其他选项
	
		import "testmessage.message"					引入testmessage.message文件中定义的对象，可以有多个引入文件
		
		import "testmessage2.message"					引入testmessage2.message文件中定义的对象，可以有多个引入文件
		
		java_package = org.snake.testmessage.m2			本文件中对象所在的包
		
		group_cmd = 2000;								本文件中的组消息CMD开始，文件中的消息CMD由组CMD自动增长，
														所有消息的CMD都是唯一的，每个组的CMD不能一样，同时还要有足够的区间
														来使消息的CMD不重复
		
	4. 关键点
	
		比如：FirstRequest:
		
		// 消息中的消息对象只有getter, 没有setter, 因为aitem在retainMessage()中实例化
		private Item aitem = null; 
		
		// 		repeated Item 	al7;
		// 消息对象数组，生成为final List<Item>类型，生命周期和FirstRequest一样
		private final List<Item> al7 = new LinkedList<Item>();
		
		
2. 生成消息:

		org.jack.messagecreator.App.main()
		
		arg[0] 消息定义文件的路径
		arg[1] 生成对象文件路径 
		
		如果 arg[0] 没有提供，则使用当前路径 
		如果 arg[1] 没有提供，，则使用当前路径 + "\\src"
		
3. 得到池化对象

	1. parse()函数从Netty中生成，使用Netty的零拷贝

		buf.skipBytes(CMD_OFFSET);
		short cmd = buf.readShort();

		message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
	2. array()函数写到Netty，使用Netty的零拷贝
	
		// not use any lock
		// this line to next 5 nonempty line must here, 
		// can't move to message.array(buf)
		// if moved into, message.getSize() get wrong size
		// why??????????????????????????????????????

		int totalLen = message.getSize() + HEADER_LENGTH;
		
		if(!buf.isWritable(totalLen))
			buf.ensureWritable(totalLen);
		
		buf.writeInt(totalLen);
		buf.writeShort(message.getCmdShort());
		
		message.array(buf);

		// next nonempty line must here, 
		// can't move to message.array(buf)
		// why??????????????????????????????????????
		message.release();
		
		
	3. MessagePool.borrowMessage()生成对象，并写到Netty中
	
	
		    public Empty createEmptyMessage() {
    	
				Empty message = (Empty)MessagePool.borrowMessage(Empty.CMD_INTEGER);
				
				return message;
			}
	
		    ClientMessageEvent event2 = new ClientMessageEvent();
	    	
	    	event2.setMessage(createEmptyMessage());
	    	
	    	channel.writeAndFlush(event2);