package org.prawa.awachat.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class MessageEncoder extends MessageToMessageEncoder<CharSequence> {
    private static final Logger LOGGER = LogManager.getLogger();
    private final int maxLength;

    public MessageEncoder(int maxLength){
        this.maxLength = maxLength;
    }

    public MessageEncoder(){
        this.maxLength = 32768;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CharSequence s, List<Object> list){
        if (s!=null && list!=null) {
            if (s.length() > this.maxLength){
                LOGGER.info("Large message detected!Discarded");
                return;
            }
            final String head = "head$$";
            final String end = "end$$";
            String body = String.valueOf(s.length())+"#"+s;
            ByteBuf buffer = ByteBufUtil.encodeString(channelHandlerContext.alloc(), CharBuffer.wrap(head +body+end), Charset.defaultCharset());
            list.add(buffer);
        }
    }
}
