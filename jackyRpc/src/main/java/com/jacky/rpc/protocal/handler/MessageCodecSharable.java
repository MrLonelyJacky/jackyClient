package com.jacky.rpc.protocal.handler;

import com.jacky.rpc.common.serialization.JsonSerialization;
import com.jacky.rpc.common.serialization.RpcSerialization;
import com.jacky.rpc.protocal.MsgHeader;
import com.jacky.rpc.protocal.RpcProtocol;
import com.jacky.rpc.protocal.RpcRequest;
import com.jacky.rpc.protocal.RpcResponse;
import com.jacky.rpc.protocal.constant.MsgType;
import com.jacky.rpc.protocal.constant.ProtocolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @Author: jacky
 * @Date:2024/1/19 19:44
 * @Description: 编码解码器 协议格式如下：
 * 头信息：魔术、版本号、消息类型、请求id、消息长度
 * 消息体：发送的内容
 **/
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, RpcProtocol<Object>> {


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
        //序列化 todo 优化复用对象
        RpcSerialization rpcSerialization = new JsonSerialization();
        byte[] bytes = rpcSerialization.serialize(msg.getBody());
        // 7. 长度
        byteBuf.writeInt(bytes.length);
        // 8. 写入内容
        byteBuf.writeBytes(bytes);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        short magicNum = in.readShort();
        if (magicNum != ProtocolConstants.MAGIC) {
            throw new RuntimeException("magic number is wrong!!!");
        }
        byte version = in.readByte();
        byte messageType = in.readByte();
        long sequenceId = in.readLong();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        MsgType byType = MsgType.findByType(messageType);
        RpcSerialization rpcSerialization = new JsonSerialization();
        // 构建消息头
        MsgHeader header = new MsgHeader();
        header.setMagic(magicNum);
        header.setVersion(version);
        header.setRequestId(sequenceId);
        header.setMsgType(messageType);
        //header.setSerializations(bytes);
        header.setMsgLen(length);
        switch (byType){
            //请求消息
            case REQUEST:
                RpcRequest request = rpcSerialization.deserialize(bytes, RpcRequest.class);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
                break;
            // 响应消息
            case RESPONSE:
                RpcResponse response = rpcSerialization.deserialize(bytes, RpcResponse.class);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
            default:
                throw new RuntimeException("无效消息！");
        }

    }
}
