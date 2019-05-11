# snake a netty nio game server framework.

MessageCreator a message pool, how to

1. support types: 
	
	1. base type
	2. self defined object type based on base type

		message base type-------------to------------java type


		required double 	dd;    ---------------------- double
		
		required float 	ff;	----------------------        float
		
		required int8 	i8;	----------------------        byte/char
		
		required int16 	i16;----------------------        short
		
		required int32 	i32;----------------------        int
		
		required int64 	i64;----------------------        long
		
		required bool 	isArray;----------------------    boolean
		
		required string 	name;----------------------   String
		
		repeated string 	l6;----------------------     String[]
	
	
	3. other options
	
		import "testmessage.message"					import objects from file, can has multiple.
		
		import "testmessage2.message"					import objects from file, can has multiple.
		
		java_package = org.snake.testmessage.m2			the package of the messages defined in this file
		
		group_cmd = 2000;								unique cmd start of all file, every cmd is unique
		
	4. some important point
	
		such as in FirstRequest:
		
		only has getter, no setty, cause aitem will be instanced in retainMessage();
		private Item aitem = null; 
		
		final and not setter: created as message FirstRequest created, life cycle equal to FirstRequest
		private final List<Item> al7 = new LinkedList<Item>();
		
		
2. how to create messages:

		org.jack.messagecreator.App.main()
		
		arg[0] src dir of message define file
		arg[1] dest of messages created
		
		if arg[0] not provided, uses current dir
		if arg[1] not provided, uses src dir + "\\src"
		
3. get pooled messsage

	1. parse from netty with zero copy

		buf.skipBytes(CMD_OFFSET);
		short cmd = buf.readShort();

		message = MessagePool.borrowMessage(cmd);
		
		message.parse(buf);
		
	2. write to netty with zero copy
	
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
		
		
	3. get message and write to netty
	
	
		    public Empty createEmptyMessage() {
    	
				Empty message = (Empty)MessagePool.borrowMessage(Empty.CMD_INTEGER);
				
				return message;
			}
	
		    ClientMessageEvent event2 = new ClientMessageEvent();
	    	
	    	event2.setMessage(createEmptyMessage());
	    	
	    	channel.writeAndFlush(event2);