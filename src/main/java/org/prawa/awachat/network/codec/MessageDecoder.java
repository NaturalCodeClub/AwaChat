package org.prawa.awachat.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.prawa.awachat.network.ChannelHandler;
import java.util.*;

public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    private int last_length = 0;
    private byte[] datas;
    private boolean isFinished = true;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list){
            if(this.last_length==0)
            {
                int data_length=byteBuf.readInt();
                byte[] data=new byte[data_length];
                if(byteBuf.readableBytes()<data_length)
                {
                    this.isFinished = false;
                    this.last_length = data_length-byteBuf.readableBytes();
                    this.datas = data;
                    return;
                }
                for(int i=0;i<data_length;++i)
                    data[i]= byteBuf.readByte();
                if(byteBuf.isReadable())
                {
                    List<Object> str = new ArrayList<>();
                    decode(channelHandlerContext,byteBuf,str);
                    if(str.size()!=0)
                        ChannelHandler.customRead(channelHandlerContext,(String)str.get(0));
                }
                list.add(new String(data));
            }else{
                byte[] last_data = new byte[last_length];
                for(int i=0;i<last_length;++i)
                    last_data[i] = byteBuf.readByte();
                byte[] last_read = arrayJoin(this.datas,last_data);
                this.isFinished = true;
                ChannelHandler.customRead(channelHandlerContext,new String(last_read));
                if(byteBuf.isReadable())
                {
                    List<Object> str = new ArrayList<>();
                    decode(channelHandlerContext,byteBuf,str);
                    if(str.size()!=0)
                        ChannelHandler.customRead(channelHandlerContext,(String)str.get(0));
                }
            }
    }
    public static byte[] arrayJoin(byte[] a,byte[] b){
        byte[] arr=new byte[a.length+b.length];
        System.arraycopy(a, 0, arr, 0, a.length);
        System.arraycopy(b, 0, arr, a.length, b.length);
        return arr;
    }
}

















