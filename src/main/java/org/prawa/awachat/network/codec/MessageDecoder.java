package org.prawa.awachat.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.prawa.awachat.network.ChannelHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    int last_length=0;
    static byte[] datas;
    public static boolean isFinish=true;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            if(last_length==0)
            {
                int data_length=byteBuf.readInt();
                byte[] data=new byte[data_length];
                if(byteBuf.readableBytes()<data_length)
                {
                    isFinish=false;
                    last_length=data_length-byteBuf.readableBytes();
                    datas=data;
                    return;
                }
                for(int i=0;i<data_length;++i)
                    data[i]= byteBuf.readByte();
                if(byteBuf.isReadable())
                {
                    List<Object> str=new ArrayList<>();
                    decode(channelHandlerContext,byteBuf,str);
                    if(str.size()!=0)
                        ChannelHandler.customRead(channelHandlerContext,(String)str.get(0));
                }
                list.add((Object) new String(data));
                return;
            }else{
                byte[] last_data=new byte[last_length];
                for(int i=0;i<last_length;++i)
                    last_data[i]=byteBuf.readByte();
                byte[] last_readata=arrayJoin(datas,last_data);
                isFinish=true;
                ChannelHandler.customRead(channelHandlerContext,new String(last_readata));
                if(byteBuf.isReadable())
                {
                    List<Object> str=new ArrayList<>();
                    decode(channelHandlerContext,byteBuf,str);
                    if(str.size()!=0)
                        ChannelHandler.customRead(channelHandlerContext,(String)str.get(0));
                }
                return;
            }
    }
    public static byte[] arrayJoin(byte[] a,byte[] b){
        byte[] arr=new byte[a.length+b.length];
        for(int i=0;i<a.length;i++){
            arr[i]=a[i];
        }
        for(int j=0;j<b.length;j++){
            arr[a.length+j]=b[j];
        }
        return arr;
    }
}

















