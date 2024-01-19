package com.jacky.rpc.protocal.codec;

import com.jacky.rpc.protocal.MsgHeader;
import com.jacky.rpc.protocal.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @Author: jacky
 * @Date:2024/1/19 19:44
 * @Description:
 **/
public class MessageCodecSharable extends MessageToMessageCodec<RpcProtocol<Object>,RpcProtocol<Object>> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, List<Object> list) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        MsgHeader header = msg.getHeader();

        // 写入魔数(安全校验，可以参考java中的CAFEBABE)
        byteBuf.writeShort(header.getMagic());
        // 写入版本号
        byteBuf.writeByte(header.getVersion());
        // 写入消息类型(接收放根据不同的消息类型进行不同的处理方式)
        byteBuf.writeByte(header.getMsgType());
        //请求id
        byteBuf.writeLong(header.getRequestId());
        //
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
      /*  // 7. 长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);*/
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, RpcProtocol<Object> objectRpcProtocol, List<Object> list) throws Exception {

    }
}
