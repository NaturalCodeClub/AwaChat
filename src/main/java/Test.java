import org.prawa.awachat.manager.UserEntry;
import org.prawa.awachat.network.codec.JSONMessage;

public class Test {
    public static void main(String[] args) {

        System.out.println("Server messages:");
        JSONMessage userInfo = new JSONMessage("userinfo",1);
        userInfo.setData(0,new UserEntry("username","password"));
        System.out.println("Userinfo response:"+userInfo.buildJson());
        System.out.println("Register response:"+new JSONMessage("reg_response",new String[]{"user_was_exists or finished"},new Object[1]).buildJson());
        System.out.println("Login response:"+new JSONMessage("login_response",new String[]{"no_such_user or wrong_password or finished"},new Object[1]).buildJson());
        System.out.println("Server chat forward:"+new JSONMessage("schat",new String[]{"your chat(chat or private)","your target(When the tag is chat,It can be empty)"},new Object[]{"your chat message"}).buildJson());
        System.out.println("Target channel not found"+new JSONMessage("channel",new String[]{"target_not_found"},new Object[1]).buildJson());
        System.out.println("private chat target channel is null"+new JSONMessage("channel",new String[]{"invalid_target"},new Object[1]).buildJson());
        System.out.println("on server friend request forward"+new JSONMessage("sfriendrequest",new String[]{},new Object[]{"source user","leave word"}).buildJson());
        System.out.println("on friend response not found such request"+new JSONMessage("channel",new String[]{"no_such_request"},new Object[1]).buildJson());
        System.out.println("on channel not logined:"+new JSONMessage("channel",new String[]{"not_login"},new Object[1]).buildJson());
        System.out.println("On channel disconnected:"+new JSONMessage("channel",new String[]{"channel_disconnected"},new Object[]{"name"}).buildJson());
        System.out.println("Client messages:");
        System.out.println("User login request:"+new JSONMessage("login",new String[]{},new Object[]{"your user name","your password"}).buildJson());
        System.out.println("User register request:"+new JSONMessage("register",new String[]{},new Object[]{"your user name","your password"}).buildJson());
        System.out.println("User chat request:"+new JSONMessage("chat",new String[]{"your chat(chat or private)","your target(When the tag is chat,It can be empty)"},new Object[]{"your chat message"}).buildJson());
        System.out.println("Friend request:"+new JSONMessage("friendaddrequest",new String[]{},new Object[]{"your target","your leave word"}).buildJson());
        System.out.println("Friend response:"+new JSONMessage("firendresponse",new String[]{},new Object[]{"Your source"}).buildJson());
        System.out.println("Get user info"+new JSONMessage("getuserinfo",1).buildJson());
    }
}
