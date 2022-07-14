//
// Created by Ingil Ying on 2022/7/14.
//

#include "Message.h"

Message::Message(QObject *parent) : QObject(parent)
{

}

Message::~Message()
{

}

Message* Message::Create(QString msg_sender, bool type, QString text)
{
    Message *msg=new Message();
    msg->text=text;
    msg->msg_sender=msg_sender;
    msg->type=type;
    return msg;
}

