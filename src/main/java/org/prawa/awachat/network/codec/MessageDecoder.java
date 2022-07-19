package org.prawa.awachat.network.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class MessageDecoder extends MessageToMessageDecoder<CharSequence> {
    private String lastRead = null;

    /**
     *Emmmm,我并不确定这完全是对的(((
     * @param channelHandlerContext
     * @param charSequence
     * @param list
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, CharSequence charSequence, List<Object> list){
        if (lastRead.startsWith("head$$") && lastRead.endsWith("end$$")){
            list.add(processMessage(lastRead));
        }
        String stringMessage = (String)charSequence;
        if (stringMessage.startsWith("head$$") && stringMessage.endsWith("end$$")){
            list.add(processMessage(stringMessage));
            return;
        }
        if (stringMessage.startsWith("head$$")){
            if (!stringMessage.endsWith("end$$") && !stringMessage.contains("end$$")){
                lastRead = stringMessage;
                return;
            }
            if (stringMessage.contains("end$$")){
                String[] s = stringMessage.split("end$$");
                list.add(processMessage(s[0]+"end$$"));
                lastRead = s[1];
                return;
            }
        }
        if (!stringMessage.startsWith("head$$")){
            if (lastRead!=null){
                lastRead += stringMessage;
                list.add(lastRead);
                lastRead = null;
                return;
            }
        }
    }

    public String processMessage(String s){
        char[] chars = new char[s.toCharArray().length-11];
        System.arraycopy(s.toCharArray(), 5, chars, 0, s.toCharArray().length - 5 - 5);
        return new String(chars);
    }
}
