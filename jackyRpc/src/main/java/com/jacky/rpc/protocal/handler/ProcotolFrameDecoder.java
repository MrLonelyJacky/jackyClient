package com.jacky.rpc.protocal.handler;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**'
 * tlc粘包半包处理器
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProcotolFrameDecoder() {
        //最大是10k  消息头12byte  四个字节表示消息长度
        this(1024*10, 12, 4, 0, 0);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
